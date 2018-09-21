package com.dkp.ipcdemo.aidl.demo2;

import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Binder;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;


import com.dkp.ipcdemo.aidl.Book;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 跨进程通信服务端
 * Created by dkp on 2018/9/20.
 * CopyOnWriteArrayList支持并发读写，自动线程同步
 */

public class BookManagerService2 extends Service {
    private static final String TAG = "BookManagerService2";
    private CopyOnWriteArrayList <Book2> mBookList = new CopyOnWriteArrayList<Book2>();
    //接口注册解绑方式一：
//    private CopyOnWriteArrayList <IOnNewBookArrivedListener> mListenerList= new CopyOnWriteArrayList<IOnNewBookArrivedListener>();

    //接口注册解绑方式二：RemoteCallbackList是系统提供的专门用于删除跨进程listener的接口
    //多次跨进程传输客户端的同一个对象会在服务端生成不同的对象，但是这些对象底层的Binder对象是同一个
    //当客户端解注的时候，只要遍历服务端所有listener，找出具有相同Binder对象的服务端listener,把它删除即可这就是RemoteCallbackList做的事情
    //RemoteCallbackList内部自动实现线程同步，我们在注册和解注的时候不需要额外的线程同步操作
    private RemoteCallbackList<IOnNewBookArrivedListener> mRemoteCallBackList = new RemoteCallbackList<IOnNewBookArrivedListener>();

    private Binder mBinder = new IBookManager2.Stub() {


        //权限验证方式二：服务端的onTransact方法中
        @Override
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {


            int check = checkCallingOrSelfPermission("com.dkp.permission.BOOK");
            if (check == PackageManager.PERMISSION_DENIED) {
                Log.d(TAG,"onTransact 客户端没有权限调用");
                return false;
            }

            String packageName = null;
            String [] packages= getPackageManager().getPackagesForUid(getCallingUid());
            if (packages!=null && packages.length>0){
                packageName=packages[0];
            }
            Log.d(TAG,"packageName = "+packageName);
            if (!packageName.startsWith("com.dkp")){
                return false;
            }

            return super.onTransact(code, data, reply, flags);
        }


        @Override
        public List<Book2> getBookList() throws RemoteException {
            return mBookList;
        }

        @Override
        public void addBook(Book2 book) throws RemoteException {
            mBookList.add(book);
        }

        @Override
        public void registerListener(IOnNewBookArrivedListener listener) throws RemoteException {
            //注册接口监听方式一
//            if (!mListenerList.contains(listener)){
//                mListenerList.add(listener);
//            }else{
//                Log.d(TAG,"Already Exists");
//            }
//            Log.d(TAG,"registerListener size = "+mListenerList.size());

            //注册接口监听方式二
            mRemoteCallBackList.register(listener);
            final int N = mRemoteCallBackList.beginBroadcast();
            Log.d(TAG,"registerListener mRemoteCallBackList size ="+ N);
            mRemoteCallBackList.finishBroadcast();

        }


        @Override
        public void unregisterListener(IOnNewBookArrivedListener listener) throws RemoteException {

            //解绑接口监听方式一
//            if (!mListenerList.contains(listener)){
//                mListenerList.remove(listener);
//                Log.d(TAG,"unregister Succeed");
//            }else{
//                Log.d(TAG,"not found cant unregister");
//            }
//            Log.d(TAG,"unregisterListener size = "+mListenerList.size());

            //解绑方式二
            mRemoteCallBackList.unregister(listener);
            final int N = mRemoteCallBackList.beginBroadcast();
            Log.d(TAG,"unregister mRemoteCallBackList size ="+ N);
            mRemoteCallBackList.finishBroadcast();
        }
    };
    private boolean IsServiceDestroyed=false;

    @Override
    public void onCreate() {
        super.onCreate();
        mBookList.add(new Book2(1,"java"));
        mBookList.add(new Book2(2,"c++"));
        new Thread(new ServiceWork()).start();
    }



    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        //权限检查方式一：onBind  checkCallingOrSelfPermission方法允许自检注意区分checkCallingPermission，应用内绑定服务不需要下列代码，只需要在Manifest文件声明权限，
        // 或者使用checkCallingOrSelfPermission，此处如果使用checkCallingPermission  check 会一直等于-1，避免这个坑
//        int check = checkCallingOrSelfPermission("com.dkp.permission.BOOK");
//        if (check == PackageManager.PERMISSION_DENIED) {
//            Log.d(TAG,"客户端没有权限调用");
//            return null;
//        }

        return mBinder;
    }



    /**
     * 新书提醒逻辑
     */
    private class ServiceWork implements Runnable {
        @Override
        public void run() {
            while (!IsServiceDestroyed){
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                int bookId = mBookList.size()+1;
                Book2 newBook = new Book2(bookId,"NEW "+bookId);
                try {
                    onNewBookArrived(newBook);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }

            }                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                  
        }
    }

    @Override
    public void onDestroy() {
        IsServiceDestroyed=true;
        super.onDestroy();
    }

    private void onNewBookArrived(Book2 newBook) throws RemoteException {
        mBookList.add(newBook);
        //注册解绑方式一，发送新书通知
//        Log.d(TAG,"onNewBookArrived notify listener size =" +mListenerList.size());
//        for (int i = 0;i<mListenerList.size();i++){
//            IOnNewBookArrivedListener listener = mListenerList.get(i);
//            Log.d(TAG,"onNewBookArrived notify listener  =" +listener);
//            listener.onNewBookArrived(newBook);
//        }
        //注册解绑方式二，发送新书通知
        final int N = mRemoteCallBackList.beginBroadcast();
        for (int i =0; i<N;i++){
            IOnNewBookArrivedListener l = mRemoteCallBackList.getBroadcastItem(i);
            if (l!= null){
                try {
                    l.onNewBookArrived(newBook);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            mRemoteCallBackList.finishBroadcast();
        }
    }
}
