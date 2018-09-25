package com.dkp.ipcdemo.binderpool;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

public class BinderPoolService extends Service {
    public static final String TAG = "BinderPoolService";

    private Binder mBinderPool = new BinderPool.BinderPoolImpl();

    public BinderPoolService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinderPool;
    }
}
