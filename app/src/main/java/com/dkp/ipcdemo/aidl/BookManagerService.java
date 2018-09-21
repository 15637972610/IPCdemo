package com.dkp.ipcdemo.aidl;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.support.annotation.Nullable;

import com.dkp.ipcdemo.aidl.demo2.Book2;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by dkp on 2018/9/20.
 * CopyOnWriteArrayList支持并发读写，自动线程同步
 */

public class BookManagerService extends Service {
    private static final String TAG = "BookManagerService";
    private CopyOnWriteArrayList <Book> mBookList = new CopyOnWriteArrayList<Book>();
    private Binder mBinder = new IBookManager.Stub() {
        @Override
        public List<Book> getBookList() throws RemoteException {
            return mBookList;
        }


        @Override
        public void addBook(Book book) throws RemoteException {
            mBookList.add(book);

        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        mBookList.add(new Book(1,"java"));
        mBookList.add(new Book(2,"c++"));
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }


}
