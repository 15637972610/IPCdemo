package com.dkp.ipcdemo.provider;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.dkp.ipcdemo.R;
import com.dkp.ipcdemo.aidl.Book;

/**
 * Created by Administrator on 2018/9/21.
 */

public class ProviderActivity extends Activity {
    private static final String TAG = "ProviderActivity";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider);
        //book的测试
        Uri bookUri = Uri.parse("content://com.dkp.ipcdemo.provider/book");
        ContentValues values = new ContentValues();
        values.put("_id",6);
        values.put("name","Android开发艺术");
        getContentResolver().insert(bookUri,values);
        Cursor bookCurssor = getContentResolver().query(bookUri,new String[]{"_id","name"},null,null,null);
       while (bookCurssor.moveToNext()){
           Book3 book = new Book3();
           book.bookId = bookCurssor.getInt(0);
           book.bookName=bookCurssor.getString(1);
           Log.d(TAG,"query bookid ="+book.bookId +"bookName ="+ book.bookName);

       }
       bookCurssor.close();
       //user的测试省略

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
