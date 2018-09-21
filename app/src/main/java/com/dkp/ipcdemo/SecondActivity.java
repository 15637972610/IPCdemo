package com.dkp.ipcdemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

/**
 * Created by dkp on 2018/9/19.
 */

public class SecondActivity extends AppCompatActivity {
    private static final String TAG ="SecondActivity" ;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG,"y="+MyApplication.type);
    }
}
