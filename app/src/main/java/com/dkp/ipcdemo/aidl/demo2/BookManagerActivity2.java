package com.dkp.ipcdemo.aidl.demo2;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import com.dkp.ipcdemo.R;

import java.util.List;

/**跨进程通信客户端
 * Created by dkp on 2018/9/20.
 * aidl demo2基于demo1简单的实现进程间的通信,
 * 1.添加观察者接口IOnNewBookArrivedListener.aidl
 * 2.接口注册和解绑的两种方式：
 * 方式一：见BookManagerService2中注释
 * 方式二：见BookManagerService2中注释
 * 3.当客户端或者服务端的方法被跨进程调用的问题，及解决办法：
 * 由于客户端的方法运行在客户端的binder线程池中，服务端的方法运行在服务端的线程池中，
 * 跨进程调用时，如果方法里有大量耗时操作需要异步操作
 * 注意：客户端里的方法有UI操作时结合Handler使用
 * 4.健壮性：Binder是可能意外死亡的，可能是由于服务端进程意外停止，这时我们需要重连服务
 * 方式一：给Binder设置DeathRecipient监听，当Binder死亡时会收到binderDied回调，在这个回调里重连服务
 *
 * 方式二：在onServiceDisconnected中重连服务
 * 二者区别：onServiceDisconnected在客户端的UI线程中回调，binderDied在客户端的Binder线程池中回调不能访问UI
 *
 * 5.权限检查的两种方式
 * 方式一：服务端的onBind方法中
 *
 * 方式二：服务端的onTransact方法中
 *  服务端不会终止aidl中的方法从而保护服务端
 *
 *
 */

public class BookManagerActivity2 extends Activity {
    private static final String TAG = "BookManagerActivity2";
    private static final int MESSAGE_NEW_BOOK_ARRIVED = 1;
    private IBookManager2 mRomoteBookManger;

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            IBookManager2 bookManager = IBookManager2.Stub.asInterface(iBinder);
            try {
                mRomoteBookManger = bookManager;
                List<Book2> list = bookManager.getBookList();
                Log.d(TAG, "list type = " + list.getClass().getCanonicalName());
                Log.d(TAG, "list = " + list.toString());
                Book2 newBook = new Book2(3,"php高级进阶");
                bookManager.addBook(newBook);
                Log.d(TAG, "addbook = " + newBook);
                List<Book2> mList = bookManager.getBookList();
                Log.d(TAG,"query book list ="+mList.toString());

                bookManager.registerListener(mOnNewBookArrivedListener);



            } catch (RemoteException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            //service 断开的时候重新连接
            Intent intent = new Intent(BookManagerActivity2.this, BookManagerService2.class);
            bindService(intent, mConnection, Context.BIND_AUTO_CREATE);

        }
    };

    private IOnNewBookArrivedListener mOnNewBookArrivedListener = new IOnNewBookArrivedListener.Stub() {
        @Override
        public void onNewBookArrived(Book2 newBook) throws RemoteException {
            mHandler.obtainMessage(MESSAGE_NEW_BOOK_ARRIVED,newBook).sendToTarget();
        }
    };


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_NEW_BOOK_ARRIVED:

                    break;
                default:
                    super.handleMessage(msg);

            }
        }
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aidl);
        Intent intent = new Intent(BookManagerActivity2.this, BookManagerService2.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);


    }

    @Override
    protected void onDestroy() {
        if (mRomoteBookManger!=null && mRomoteBookManger.asBinder().isBinderAlive()){
            try {
                Log.d(TAG,"unregister listener = "+mOnNewBookArrivedListener);
                mRomoteBookManger.unregisterListener(mOnNewBookArrivedListener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        unbindService(mConnection);
        super.onDestroy();
    }
}
