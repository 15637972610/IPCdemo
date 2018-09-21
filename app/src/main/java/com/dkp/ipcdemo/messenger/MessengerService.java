package com.dkp.ipcdemo.messenger;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by dkp on 2018/9/20.
 * 1.创建MessengerService类
 */

public class MessengerService extends Service {
    public static final String TAG = "MessengerService";

    /**
     * 2.创建MessengerHandler
     */
    private static class MessengerHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MessengerActivity.MSG_FROM_CLIENT:
                    String msgdata = msg.getData().getString(MessengerActivity.MSG);
                    Log.d(TAG, "msg = " + msgdata);

                    //10.回复一条消息给客户端
                    Messenger client =msg.replyTo;
                    Message replyMessage = Message.obtain(null,MessengerActivity.MSG_FROM_SERVICE);
                    Bundle bundle = new Bundle();
                    bundle.putString(MessengerActivity.REPLY,"消息已收到！！");
                    replyMessage.setData(bundle);
                    try {
                        client.send(replyMessage);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }


                    break;
                default:
                    super.handleMessage(msg);
            }

        }
    }

    /**
     * 3.创建Messenger
     */
    private final Messenger mMessenger = new Messenger(new MessengerHandler());

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind");
        /**
         * 4.在onBind方法里返回Messenger里的Binder对象
         */
        return mMessenger.getBinder();
    }
}
