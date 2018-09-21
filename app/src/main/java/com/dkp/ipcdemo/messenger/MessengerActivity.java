package com.dkp.ipcdemo.messenger;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.dkp.ipcdemo.R;

/**
 * Created by dkp on 2018/9/20.
 * <p>
 * 原理：Messenger:信使，通过它可以在不同进程间传递Message对象。主要是通过Messenger.send方法发送消息给另一个进程
 * 在Message中存入我们需要传递的数据，实现进程间通信。
 * 实质：通过查看Messenger的构造函数可以知道，Messenger底层实现是AIDL
 * 特点：由于Messenger一次处理一个请求,因此在服务端不用考虑线程同步问题。
 * 缺点：
 * 1.大量消息同时发送到服务端，服务端依然只能一个个的处理，有并发需求的时候不适用。
 * 2.当我们需要跨进程的调用服务端的方法的时候不适用，例如项目里的通话中的红包功能
 * 实现步骤：
 * 1.创建MessengerService类
 * 2.MessengerService里创建MessengerHandler
 * 3.MessengerService里创建Messenger
 * 4.在MessengerService的onBind方法里返回Messenger里的Binder对象
 * 5.Manifest里注册这个service
 * 6.MessengerActivity里创建ServiceConnection
 * 7.在ServiceConnection里根据服务端返回的binder对象创建Messenger对象，并使用这个对象向服务端发送消息
 * 8.MessengerActivity的onCreate里绑定MessengerService
 * 9.onDestroy中unbindService
 *
 * 注：步骤1-9实现客户端进程想服务端进程传递数据；步骤10-12实现服务端响应数据给客户端
 *
 * 10.MessengerService的MessengerHandler里回复一条消息给客户端
 * 11.为了接受服务端返回的响应信息，客户端也需要准备一个接受消息的Messenger 和 Handler
 * 12.接受服务端响应至关重要的一步！！需要把接收服务端回应的Messenger通过Message的replyTo参数传递给服务端
 */

public class MessengerActivity extends Activity {

    private static final String TAG = "MessengerActivity";
    public static final String MSG = "msg";
    public static final String REPLY = "reply";
    public static final int MSG_FROM_CLIENT = 0;
    public static final int MSG_FROM_SERVICE = 1;
    private Messenger mService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messenger);
        // 8.客户端里绑定MessengerService,至此，客户端便可以向服务端发送消息hello this is from client
        Intent mIntent = new Intent(MessengerActivity.this, MessengerService.class);
        bindService(mIntent, mConnection, Context.BIND_AUTO_CREATE);

    }


    //6.MessengerActivity里创建ServiceConnection
    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            //7. 根据服务端返回的binder对象创建Messenger对象并使用这个对象向服务端发送消息
            mService = new Messenger(iBinder);
            Message msg = Message.obtain(null, MSG_FROM_CLIENT);
            Bundle data = new Bundle();
            data.putString(MSG, "hello this is from client");
            msg.setData(data);
            //12.接受服务端响应至关重要的一步！！需要把接收服务端回应的Messenger通过Message的replyTo参数传递给服务端
            msg.replyTo=mReplyMessenger;

            try {
                mService.send(msg);
            } catch (RemoteException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };


    @Override
    protected void onDestroy() {
        //9.onDestroy中unbindService
        unbindService(mConnection);
        super.onDestroy();

    }

    //11.为了接受服务端返回的响应信息，客户端也需要准备一个接受消息的Messenger 和 Handler
    private Messenger mReplyMessenger = new Messenger(new MessengerHandler());

    private static class MessengerHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_FROM_SERVICE:
                    String reply = msg.getData().getString(REPLY);
                    Log.d(TAG,reply);
                    break;
                default:
                    super.handleMessage(msg);
            }


        }
    }


}
