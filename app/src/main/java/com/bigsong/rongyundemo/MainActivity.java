package com.bigsong.rongyundemo;

import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.MessageContent;
import io.rong.imlib.model.UserInfo;
import io.rong.message.TextMessage;

public class MainActivity extends AppCompatActivity {

    private String rongToken;

    private Button changeUserBtn,connectBtn, joinChatBtn, sendMessageBtn;

    private TextView recevierTv;

    private android.os.Handler handler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RongIMClient.setOnReceiveMessageListener(new MyReceiveMessageListener());
//        rongToken = "fDYFZdElBIpwTxD4MueoCce0bHg6y2GXBjQ2Czf2wkS6dEeAg5QjiPTQiEl1g6dgsz5aBDXEnenWfDrtjc2lqg==";
//        rongToken = "hoo0EfPaZUZajgexQ2BIxMY4u4zqvI4hcF4Vn+py+t2XZ1OEEmq708FS2lJhcy4ebs3OoyKmXKI=";
        changeUserBtn = (Button) findViewById(R.id.btn_changeuser);
        connectBtn = (Button) findViewById(R.id.btn_connect);
        joinChatBtn = (Button) findViewById(R.id.btn_joinchat);
        sendMessageBtn = (Button) findViewById(R.id.btn_sendmessage);
        recevierTv = (TextView) findViewById(R.id.tv_recevier);
        connectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                connect(rongToken);
            }
        });
        joinChatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                joinChatRoom("123456", 10);
            }
        });
        changeUserBtn.setOnClickListener(new View.OnClickListener() {
            int i =0;
            @Override
            public void onClick(View v) {
                if (i%2==0){
                    rongToken = "fDYFZdElBIpwTxD4MueoCce0bHg6y2GXBjQ2Czf2wkS6dEeAg5QjiPTQiEl1g6dgsz5aBDXEnenWfDrtjc2lqg==";
                    Toast.makeText(MainActivity.this,"User 1",Toast.LENGTH_SHORT).show();
                }else {
                    rongToken = "hoo0EfPaZUZajgexQ2BIxMY4u4zqvI4hcF4Vn+py+t2XZ1OEEmq708FS2lJhcy4ebs3OoyKmXKI=";
                    Toast.makeText(MainActivity.this,"User 2",Toast.LENGTH_SHORT).show();
                }
                i++;
            }
        });
        sendMessageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage("你好","Id","名字",Uri.parse("http://www.baidu.com"));
            }
        });
    }

    /**
     * 建立与融云服务器的连接
     *
     * @param token
     */
    private void connect(String token) {

        if (getApplicationInfo().packageName.equals(App.getCurProcessName(getApplicationContext()))) {

            /**
             * IMKit SDK调用第二步,建立与服务器的连接
             */
            RongIMClient.connect(token, new RongIMClient.ConnectCallback() {

                /**
                 * Token 错误，在线上环境下主要是因为 Token 已经过期，您需要向 App Server 重新请求一个新的 Token
                 */
                @Override
                public void onTokenIncorrect() {

                    Log.d("LoginActivity", "--onTokenIncorrect");
                }

                /**
                 * 连接融云成功
                 * @param userid 当前 token
                 */
                @Override
                public void onSuccess(String userid) {

                    Log.d("LoginActivity", "--Connect--onSuccess---" + userid);
                }

                /**
                 * 连接融云失败
                 * @param errorCode 错误码，可到官网 查看错误码对应的注释
                 */
                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {

                    Log.d("LoginActivity", "--Connect--onError" + errorCode);
                }
            });
        }
    }

    /**
     *
     */
    public void joinChatRoom(String roomId, int defMessageCount) {
        RongIMClient.getInstance().joinChatRoom(roomId, defMessageCount, new RongIMClient.OperationCallback() {
            @Override
            public void onSuccess() {
                Log.d("LoginActivity", "--joinChatRoom--onSuccess---");
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                Log.d("LoginActivity", "--joinChatRoom--onError" + errorCode);
            }
        });
    }

    public void sendMessage(String sendText,String userId,String nickName,Uri headImgUrl) {

        TextMessage textMessage = TextMessage.obtain(sendText);
        UserInfo userInfo = new UserInfo(userId,nickName,headImgUrl);
        textMessage.setUserInfo(userInfo);

        RongIMClient.getInstance().sendMessage(Conversation.ConversationType.CHATROOM, "123456", textMessage, null, null, new RongIMClient.SendMessageCallback() {
            @Override
            public void onSuccess(Integer integer) {
                Log.d("LoginActivity", "--sendMessage--onSuccess---" + integer);
            }

            @Override
            public void onError(Integer integer, RongIMClient.ErrorCode errorCode) {
                Log.d("LoginActivity", "--sendMessage--onError" + errorCode);
            }
        });
    }

    private class MyReceiveMessageListener implements RongIMClient.OnReceiveMessageListener {

        @Override
        public boolean onReceived(io.rong.imlib.model.Message message, int i) {
            Log.d("LoginActivity", "MyReceiveMessageListener===onReceived =" + message.toString() + "===left=====" + i);
            MessageContent messageContent = message.getContent();
            if (messageContent instanceof TextMessage) {
                TextMessage textMessage = (TextMessage) messageContent;
                Log.d("LoginActivity", "MyReceiveMessageListener===content==onReceived =" + textMessage.getContent());
                Log.d("LoginActivity", "MyReceiveMessageListener===userId===onReceived =" + textMessage.getUserInfo().getUserId());
                Log.d("LoginActivity", "MyReceiveMessageListener===nickName===onReceived =" + textMessage.getUserInfo().getName());
                Log.d("LoginActivity", "MyReceiveMessageListener===headImg===onReceived =" + textMessage.getUserInfo().getPortraitUri());
            }
            return false;
        }
    }

    public void sendMessage() {

    }
}
