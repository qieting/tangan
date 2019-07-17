package com.example.hsy.tangan;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.example.hsy.tangan.ui.TitleLayout;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SignActivity extends AppCompatActivity implements Runnable {

    @BindView(R.id.tel_sign)
    EditText tel;
    @BindView(R.id.pass_sign)
    EditText pass;
    @BindView(R.id.newpass_sign1)
    EditText newPass1;
    @BindView(R.id.newpass_sign2)
    EditText newPass2;
    @BindView(R.id.pass_btn)
    Button passbtn;
    @BindView(R.id.register)
    Button register;
    boolean find = false;
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1234:
                    passbtn.setText("重新发送（" + msg.arg1 + "s)");
                    break;
                case 1235:
                    passbtn.setText("获取短信验证码");
                    passbtn.setEnabled(true);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);

        ButterKnife.bind(this);
        initialize();
    }

    private void initialize() {
        TitleLayout titleLayout = (TitleLayout) findViewById(R.id.title_sign);
        titleLayout.backAdd();
        titleLayout.plusGone();
        find = getIntent().getBooleanExtra("find", false);
        if (find) {
            titleLayout.setTitle("找回密码");
            newPass1.setHint("请输入新密码");
            newPass2.setHint("请再次输入新密码");
        } else {
            titleLayout.setTitle("验证手机号");
            newPass1.setHint("请输入密码");
            newPass2.setHint("请再次输入密码");
        }
        register.setEnabled(false);
        register.setText("请验证手机号");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @OnClick({R.id.pass_btn, R.id.register})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pass_btn:
                if (!TextUtils.isEmpty(tel.getText())) {
                    if (!isMobile(tel.getText().toString())) {
                        showToast("请输入正确的手机号");
                    } else {
                        if(find){
                            getMessage(tel.getText().toString());
                            return;
                        }


                        RequestBody requestBody = new FormBody.Builder()
                                .add("number", tel.getText().toString())
                                .add("password", "汉")
                                .build();

                        //创建/Call
                        App app = (App) getApplication();
                        Request request = new Request.Builder()
                                .url(app.getU() + "register")//请求的url
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
                                Log.e("登陆错误", body);
                                if (body.equals("2")) {
                                    getMessage(tel.getText().toString());
                                } else {

                                    showToast("账号已经被注册");
                                }
                            }
                        });
//                        BmobQuery<P> query = new BmobQuery<P>();
//                        //查询playerName叫“比目”的数据
//                        query.addWhereEqualTo("username", tel.getText().toString());
//                        //返回50条数据，如果不加上这条语句，默认返回10条数据
//                        query.setLimit(1);
//                        //执行查询方法
//                        query.findObjects(new FindListener<P>() {
//                            @Override
//                            public void done(List<P> list, cn.bmob.v3.exception.BmobException e) {
//                                if (e == null) {
//
//                                    if (list.isEmpty()) {
//                                        if (find)
//                                            showToast("当前手机号还没有注册");
//                                        else getMessage(tel.getText().toString());
//                                    } else {
//                                        if (find)
//                                            getMessage(tel.getText().toString());
//                                        else showToast("手机号已经注册，您可以选择找回密码");
//                                    }
//                                }
//                            }
//                        });
                    }
                } else showToast("手机号不能为空");
                break;
            case R.id.register:
                if (find)
                    testMessage(tel.getText().toString(), pass.getText().toString());
                else {
                    if (!TextUtils.isEmpty(tel.getText()) && !TextUtils.isEmpty(pass.getText())) {
                        if (!isMobile(tel.getText().toString())) {
                            showToast("请输入正确的手机号");
                        } else if (pass.getText().toString().length() != 6)
                            showToast("验证码错误");
                        else testMessage(tel.getText().toString(), pass.getText().toString());
                    } else showToast("手机号或验证码不能为空");
                }
                break;

        }
    }

    private void testMessage(final String te, String message) {
        BmobSMS.verifySmsCode(te, message, new UpdateListener() {
            @Override
            public void done(BmobException e) {

                // TODO Auto-generated method stub
                if (e == null) {//短信验证码已验证成功
                    showToast("验证成功");
                    changePaaWord();

                } else {
                    Log.e("短信错误",e.getMessage());
                    showToast("验证码输入错误");
                }
            }
        });

    }

    public static String md5(String string) {
        if (TextUtils.isEmpty(string)) {
            return "";
        }
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
            byte[] bytes = md5.digest(string.getBytes());
            String result = "";
            for (byte b : bytes) {
                String temp = Integer.toHexString(b & 0xff);
                if (temp.length() == 1) {
                    temp = "0" + temp;
                }
                result += temp;
            }
            return result;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }


    private void changePaaWord() {
        if (newPass1.getText().toString().equals(newPass2.getText().toString()) && pass.length() == 6) {

            if (newPass1.getText().length() < 7 || newPass2.getText().length() > 10 || newPass1.getText().toString().contains("12345678")
                    || newPass1.getText().toString().contains("123456789") || newPass1.getText().toString().contains("0123456789")
                    || newPass1.getText().toString().contains("1234567890") || newPass1.getText().toString().contains("12345")
                    || newPass1.getText().toString().contains("1111")) {
                showToast("密码需要8到10位，且强度不能太低");
                return;
            }
            String p =md5(newPass1.getText().toString());
            RequestBody requestBody = new FormBody.Builder()
                    .add("number", tel.getText().toString())
                    .add("password", p)
                    .build();

            //创建/Call
            App app = (App) getApplication();
            Request request;
            if(find){
              request = new Request.Builder()
                        .url(app.getU() + "changePassword")//请求的url
                        .post(requestBody)
                        .build();
            }else
            request = new Request.Builder()
                    .url(app.getU() + "register")//请求的url
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
                    Log.e("登陆错误", body);
                    if (body.equals("1")) {
                        Intent intent1 = new Intent();


                        intent1.putExtra("username", tel.getText().toString());
                        intent1.putExtra("password", newPass1.getText().toString());
                        setResult(RESULT_OK, intent1);
                        finish();
                    } else {

                        showToast("账号已经被注册");
                    }
                }
            });


        } else showToast("密码不一致");
    }


    private void getMessage(final String sss) {


        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                register.setEnabled(true);
                register.setText("注册");
                passbtn.setEnabled(false);

                new Thread(SignActivity.this).start();
                BmobSMS.requestSMSCode(sss, "注册", new QueryListener<Integer>() {

                    @Override
                    public void done(Integer smsId, BmobException ex) {
                        if (ex == null) {//验证码发送成功
//						toast("验证码发送成功，短信id："+smsId);//用于查询本次短信发送详情
                            showToast("验证码已发送");
                            Log.e("验证码发送成功", smsId + "");
                        } else {
                            showToast("验证码发送失败，" + ex.getMessage());
                            Log.e("验证码发送失败", ex.getMessage());
                        }
                    }
                });
            }
        });


    }

    public void showToast(final String s) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
            }
        });

    }

    //判断是否是手机号
    public static boolean isMobile(String str) {
        Pattern p = null;
        Matcher m = null;
        boolean b = false;
        p = Pattern.compile("^[1][3,5,6,7,8,8][0-9]{9}$"); // 验证手机号
        m = p.matcher(str);
        b = m.matches();
        return b;
    }

    @Override
    public void run() {

        for (int i = 60; i >= 0; i--) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (i > 0) {
                Message message1 = new Message();
                message1.arg1 = i;
                message1.what = 1234;
                handler.sendMessage(message1);
            } else {
                Message message1 = new Message();
                message1.what = 1235;
                handler.sendMessage(message1);
            }
        }

    }


}
