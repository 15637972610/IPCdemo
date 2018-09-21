package com.dkp.ipcdemo;

import android.app.ActivityManager;
import android.app.Application;
import android.util.Log;

import java.util.List;

/**
 * Created by dkp on 2018/9/19.
 */

public class MyApplication extends Application {
    private static final String TAG = "MyApplication";
    public static  int type  = 1;

    @Override
    public void onCreate() {
        super.onCreate();

        int uid = android.os.Process.myUid();
        int pid = android.os.Process.myPid();
        String processName = "";
        ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);

        List<ActivityManager.RunningAppProcessInfo> list = activityManager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo runningAppProcessInfo : list){
            if (runningAppProcessInfo.pid == pid){
                processName = runningAppProcessInfo.processName;
                Log.i(TAG,"当前进程名称:" + processName);
            }
        }
//        Log.i(TAG,"当前进程名称:" + processName);
    }
}
