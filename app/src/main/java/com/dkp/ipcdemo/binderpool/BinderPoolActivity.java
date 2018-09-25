package com.dkp.ipcdemo.binderpool;

import android.app.Activity;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import com.dkp.ipcdemo.R;

/**
 * Created by dkp on 2018/9/25.
 */

public class BinderPoolActivity extends Activity {
    private static final String TAG = "BinderPoolActivity";
    private ISecurityCenter mSecurityBinder;
    private ICompute mCompute;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider);
//        doWork();
        new Thread(new Runnable() {
            @Override
            public void run() {
                doWork();
            }
        }).start();
    }

    private void doWork() {
        BinderPool binderPool = BinderPool.getInstance(BinderPoolActivity.this);
        IBinder securityBinder = binderPool.queryBinder(BinderPool.BINDER_SECURITY_CENTER);
        mSecurityBinder =(ISecurityCenter) SecurityCenterImpl.asInterface(securityBinder);

        Log.d(TAG,"visit ISecurityCenter");
        String msg ="hello java";
        Log.d(TAG,"content:"+msg);
        try {
            String passWord = mSecurityBinder.encrypt(msg);
            Log.d(TAG,"encrypt:"+passWord);
            Log.d(TAG,"decrypt:"+mSecurityBinder.decrypt(passWord));

        } catch (RemoteException e) {
            e.printStackTrace();
        }

        IBinder computeBinder =binderPool.queryBinder(BinderPool.BINDER_COMPUTE);
        mCompute = (ICompute)ComputeImpl.asInterface(computeBinder);

        try {
            Log.d(TAG,"visit Icompute");
            Log.d(TAG,"3+5 = "+mCompute.add(3,5));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

}
