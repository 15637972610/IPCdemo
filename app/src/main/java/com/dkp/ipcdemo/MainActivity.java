package com.dkp.ipcdemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MyApplication.type=2;
        initView();
        Log.d(TAG,"y="+MyApplication.type);

    }

    private void initView() {
        Button btn_01 = (Button) findViewById(R.id.btn_1);
        Button btn_02 = (Button) findViewById(R.id.btn_2);
        btn_01.setOnClickListener(this);
        btn_02.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_1:
                Intent intent1= new Intent(MainActivity.this,FirstActivity.class);
                startActivity(intent1);
                break;
            case R.id.btn_2:
                Intent intent2= new Intent(MainActivity.this,SecondActivity.class);
                startActivity(intent2);
                break;
            default:
                break;
        }
    }
}
