package com.brightcns.testmoudle;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.brightcns.blelibrary.BleStatus;
import com.brightcns.blelibrary.SendBleBroadcast;
import com.brightcns.textlibrary.Test;
import com.brightcns.textlibrary.User;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView textView=findViewById(R.id.test);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                test();
                ble();
            }
        });
    }
    private void test(){
        Log.e("=====1","=====");
        Test.printSomeThing();
        User user=new User.UserBuilder("aaaa","111").age("121").phone("222").address("1111").build();
        Log.e("=====3",user.getAddress());
        Log.e("=====3",user.getAge());
    }


    private void ble(){
        BleStatus.with().setListener(new BleStatus.BleStatusCallBack() {
            @Override
            public void onSuccess() {
                // TODO: 28/3/18  蓝牙开启 可进行广播开启操作
                Log.e("test","蓝牙开启 可进行广播开启操作");
                SendBleBroadcast.with().setListener(new SendBleBroadcast.SendBleBroadcastCallBack() {
                    @Override
                    public void onSuccess() {
                        Log.e("test3","广播开启成功");
                    }

                    @Override
                    public void onFail(int code, String msg) {
                        Log.e("test4",code+"\t"+msg);
                    }

                    @Override
                    public void onConnected(String address) {
                        Log.e("test5",address);
                    }

                    @Override
                    public void onDisConnected() {
                        Log.e("test6","断开连接");

                    }
                },MainActivity.this).builder();
            }

            @Override
            public void onFail(int code, String msg) {
                Log.e("test",code+"\t"+msg);
            }

            @Override
            public void connectStatus(int code, String msg) {
                Log.e("test2",code+"\t"+msg);
            }
        }).build();
    }
}
