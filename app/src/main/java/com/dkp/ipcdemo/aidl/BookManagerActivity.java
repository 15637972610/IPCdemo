package com.dkp.ipcdemo.aidl;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import com.dkp.ipcdemo.R;

import java.util.List;

/**
 * Created by dkp on 2018/9/20.
 * aidl demo1简单的实现进程间的通信
 */

public class BookManagerActivity extends Activity {
    private static final String TAG = "BookManagerActivity";

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            IBookManager bookManager =IBookManager.Stub.asInterface(iBinder);
            try {
                List<Book> list = bookManager.getBookList();
                Log.d(TAG,"list type = " +list.getClass().getCanonicalName());
                Log.d(TAG,"list = " + list.toString());


            } catch (RemoteException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aidl);
        Intent intent = new Intent(BookManagerActivity.this,BookManagerService.class);
        bindService(intent,mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        unbindService(mConnection);
        super.onDestroy();
    }
}
