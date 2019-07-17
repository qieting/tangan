package com.example.hsy.tangan;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Calendar;

import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by hsy on 2019/2/14.
 */

public class FriendsDialog extends Dialog {

    Context mContext;

    public FriendsDialog(@NonNull Context context) {
        super(context);
        mContext = context;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.addfriends);
        setCanceledOnTouchOutside(true);
        final EditText number = findViewById(R.id.number);
        Button button = findViewById(R.id.submmit);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFriends(number.getText().toString());
            }
        });
    }

    public void addFriends(String s) {
        if (s.length() != 11) {
            Toast.makeText(mContext, "电话号码必须为11位", Toast.LENGTH_SHORT).show();
            return;
        }
        //MediaType  设置Content-Type 标头中包含的媒体类型值
        RequestBody requestBody = new FormBody.Builder()
                .add("number", s)
                .build();

        //创建/Call
        final App app = (App) getContext().getApplicationContext();
        Request request = new Request.Builder()
                .url(app.getU() + "addFriends")//请求的url
                .post(requestBody)
                .build();


        Call call = app.getClient().newCall(request);
        //加入队列 异步操作
        call.enqueue(new Callback() {
            //请求错误回调方法
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("登陆失败", e.getMessage());

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body().string();
                        if (body.contains("1")) {

                            showToast("设置成功");
                        } else {
                            if (body.contains("3"))
                                showToast("此手机号尚未注册");
                            else
                                showToast("对方已经是您的监护好友了");
                        }




            }
        });


        //此处为登录

    }

    //展示Toast
    public void showToast(final String s) {
        ( (Activity)mContext).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(mContext, s, Toast.LENGTH_SHORT).show();
                FriendsDialog.this.dismiss();
            }
        });

    }
}


