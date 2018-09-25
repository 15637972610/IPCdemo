package com.dkp.ipcdemo.binderpool;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;

import java.util.concurrent.CountDownLatch;

/**
 * Created by dkp on 2018/9/25.
 */

public class BinderPool {
    private static final String TAG = "BinderPool";
    public static final int BINDER_NONE = -1;
    public static final int BINDER_COMPUTE = 0;
    public static final int BINDER_SECURITY_CENTER = 1;
    public static volatile BinderPool sInstance;

    private Context mContext;
    private CountDownLatch mConnectBinderPoolCountDownLatch;
    private IBinderPool mBinderPool;


    public BinderPool(Context mContext) {
        this.mContext = mContext.getApplicationContext();
        connectBinderPoolService();
    }

    public static BinderPool getInstance(Context context) {
        if (sInstance == null) {
            synchronized (BinderPool.class) {
                if (sInstance == null) {
                    sInstance = new BinderPool(context);
                }
            }
        }
        return sInstance;
    }

    public IBinder queryBinder(int binderCode){
        IBinder binder = null;
        try {
            if (mBinderPool!=null){
             binder = mBinderPool.queryBinder(binderCode);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        return binder;
    }




    private ServiceConnection mBinderPoolConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            mBinderPool = IBinderPool.Stub.asInterface(iBinder);
            try {
                mBinderPool.asBinder().linkToDeath(mBinderPoolDeathRecipient,0);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            mConnectBinderPoolCountDownLatch.countDown();

        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };

    private IBinder.DeathRecipient mBinderPoolDeathRecipient= new IBinder.DeathRecipient(){

        @Override
        public void binderDied() {

            mBinderPool.asBinder().unlinkToDeath(mBinderPoolDeathRecipient,0);
            mBinderPool=null;
            connectBinderPoolService();
        }
    };

    public synchronized void connectBinderPoolService() {
        mConnectBinderPoolCountDownLatch = new CountDownLatch(1);
        Intent service = new Intent(mContext,BinderPoolService.class);
        mContext.bindService(service,mBinderPoolConnection,Context.BIND_AUTO_CREATE);
        try {
            mConnectBinderPoolCountDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }

    public static class BinderPoolImpl extends IBinderPool.Stub {
        public BinderPoolImpl() {
            super();
        }

        @Override
        public IBinder queryBinder(int binderCode) throws RemoteException {
            IBinder binder = null;
            switch (binderCode) {
                case BINDER_COMPUTE:
                    binder = new ComputeImpl();
                    break;
                case BINDER_SECURITY_CENTER:
                    binder = new SecurityCenterImpl();
                    break;
                default:
                    break;

            }

            return binder;
        }
    }

}
