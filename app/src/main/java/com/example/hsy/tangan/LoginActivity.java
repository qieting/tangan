package com.example.hsy.tangan;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import com.example.hsy.tangan.ui.ClearEditText;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.reactivestreams.Subscriber;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;

import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import io.reactivex.Observer;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;


public class LoginActivity extends AppCompatActivity {


    private static final String TAG = "Login";
    @BindView(R.id.login_login)
    Button loginBtn;
    @BindView(R.id.forget_login)
    Button forgetBtn;
    @BindView(R.id.sign_login)
    Button signBtn;

    @BindView(R.id.password)
    ClearEditText password;
    @BindView(R.id.username)
    ClearEditText username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ((App) getApplication()).client = null;
        ((App) getApplication()).cle();
        ((NotificationManager) getSystemService(NOTIFICATION_SERVICE)).cancel(1845);
        getReadPermission();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "note";
            String channelName = "糖安";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            createNotificationChannel(channelId, channelName, importance);


        }
        if (getIntent().getIntExtra("in", 0) == 0)
            isHaveUser();
        else {

            ButterKnife.bind(this);
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    private void createNotificationChannel(String channelId, String channelName, int importance) {
        NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
        NotificationManager notificationManager = (NotificationManager) getSystemService(
                NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(channel);
    }

    public void getReadPermission() {


        if (Build.VERSION.SDK_INT >= 23) {
            if ((ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.READ_PHONE_STATE)
                    != PackageManager.PERMISSION_GRANTED) || (ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) ||
                    ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1312);
                //  s = null
            }
        }

    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        Log.e("权限长度", grantResults.length + "");
        switch (requestCode) {
            case 1312:

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                            Manifest.permission.READ_PHONE_STATE) && ActivityCompat.shouldShowRequestPermissionRationale(this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        Toast.makeText(getBaseContext(), "请同意上述权限，否则将不能运行", Toast.LENGTH_LONG).show();
                        getReadPermission();
                        // 返回值：
//                          如果app之前请求过该权限,被用户拒绝, 这个方法就会返回true.
//                          如果用户之前拒绝权限的时候勾选了对话框中”Don’t ask again”的选项,那么这个方法会返回false.
//                          如果设备策略禁止应用拥有这条权限, 这个方法也返回false.
                        // 弹窗需要解释为何需要该权限，再次请求授权
                    } else {
                        Toast.makeText(this, "请授权获取手机信息和读写手机存储权限！", Toast.LENGTH_LONG).show();

                        // 帮跳转到该应用的设置界面，让用户手动授权
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivityForResult(intent, 1214);

                    }

                }

                break;

            default:
        }
    }

    //判断是否存有user
    public void isHaveUser() {

        SharedPreferences userSettings = getSharedPreferences("setting", 0);
        String name = userSettings.getString("user", "1");
        String pass = userSettings.getString("password", "1");
        //progress.show();

        if (!name.equals("1") && !pass.equals("1")) {

            login(name, pass);
            return;
        } else {

            ButterKnife.bind(this);
            if (!name.equals("1")) {
                username.setText(name);
            }
        }

        //设置只能输入数字（view属性中已经设置）
        // username.setInputType(123);


    }

    @OnClick({R.id.login_login, R.id.sign_login, R.id.forget_login})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_login:
                if (!TextUtils.isEmpty(password.getText()) && !TextUtils.isEmpty(username.getText())) {
                    if (!isMobile(username.getText().toString())) {
                        showToast("请输入正确的手机号");
                    } else
                        login(username.getText().toString(), password.getText().toString());
                } else showToast("账号密码不能为空");
                break;
            case R.id.sign_login:
                Intent intent = new Intent(LoginActivity.this, SignActivity.class);
                startActivityForResult(intent, 1);
                break;
            case R.id.forget_login:
                Intent intent1 = new Intent(LoginActivity.this, SignActivity.class);
                intent1.putExtra("find", true);
                startActivityForResult(intent1, 1);
                break;
            default:
                break;
        }


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

    private void login(final String name, final String pas) {
        final String pass = md5(pas);
        Log.e("密码", pas);
        final App app = (App) getApplication();
        Retrofit retrofit = new Retrofit.Builder()
                 .client(app.getClient()).baseUrl("http://47.106.225.178:8080/xuean_war/")


                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        retrofit.create(PersonalProtocol.class)
                .getPersonalListInfo(name, pass)//注意看，这里返回的对象时什么，和原生的返回不同，也是我们把上面接口改的原因
                //在子线程取数据
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                //在主线程显示ui
                // compile 'io.reactivex.rxjava2:rxandroid:2.0.1'  这个库下才有AndroidSchedulers.mainThread
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        //请求失败
                        showToast("网络连接错误");
                        ButterKnife.bind(LoginActivity.this);
                    }

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ResponseBody response) {
                        //请求成功
                        try {
                        String body = response.string();
                        Log.e("登陆错误", body);

                            JSONObject jsonObject = new JSONObject(body);
                            String q = jsonObject.getString("status");


                            app.cle();
                            if (q.equals("1")) {
                                SharedPreferences userSettings = getSharedPreferences("setting", 0);
                                SharedPreferences.Editor editor = userSettings.edit();//获取编辑器
                                editor.putString("user", name);
                                editor.putString("password", pas);
                                editor.commit();
                                JSONArray jsonArray;
                                try {
                                    jsonArray = jsonObject.getJSONArray("tangan");


                                    if (jsonArray != null)
                                        for (int i = 0, ii = jsonArray.length(); i < ii; i++) {
                                            Tangan food = new Gson().fromJson(jsonArray.getString(i), Tangan.class);
                                            Log.e("a", food.getDate());
                                            app.addTangan(food);
                                        }


                                    app.paixu();
                                } catch (Exception e) {

                                }

                                People p = new Gson().fromJson(jsonObject.getString("people"), People.class);
                                app.setP(p);

                                jsonArray = jsonObject.getJSONArray("book");


                                if (jsonArray != null)
                                    for (int i = 0, ii = jsonArray.length(); i < ii; i++) {
                                        Book food = new Gson().fromJson(jsonArray.getString(i), Book.class);

                                        app.addBook(food);
                                    }

                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();

                            } else {
                                ButterKnife.bind(LoginActivity.this);
                                if (q.equals("-1")) {
                                    showToast("账号不存在");
                                } else if (q.equals("-2")) {
                                    showToast("账号或密码错误");
                                }


                                SharedPreferences userSettings = getSharedPreferences("setting", 0);
                                SharedPreferences.Editor editor = userSettings.edit();//获取编辑器
                                editor.remove("password");
                                editor.commit();
                            }
                        } catch (Exception e) {
                            ButterKnife.bind(LoginActivity.this);
                            e.printStackTrace();
                        }


                    }
                });


//        //MediaType  设置Content-Type 标头中包含的媒体类型值
//        RequestBody requestBody = new FormBody.Builder()
//                .add("number", name)
//                .add("password", pass)
//                .build();
//
//        //创建/Call
//        final App app = (App) getApplication();
//        Request request = new Request.Builder()
//                .url(app.getU() + "login")//请求的url
//                .post(requestBody)
//                .build();
//
//
//        Call call = app.getClient().newCall(request);
//        //加入队列 异步操作
//        call.enqueue(new Callback() {
//            //请求错误回调方法
//            @Override
//            public void onFailure(Call call, IOException e) {
//             showToast("网络连接错误");
//                ButterKnife.bind(LoginActivity.this);
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                String body = response.body().string();
//                Log.e("登陆错误", body);
//                try {
//                    JSONObject jsonObject = new JSONObject(body);
//                    String q = jsonObject.getString("status");
//
//
//                    app.cle();
//                    if (q.equals("1")) {
//                        SharedPreferences userSettings = getSharedPreferences("setting", 0);
//                        SharedPreferences.Editor editor = userSettings.edit();//获取编辑器
//                        editor.putString("user", name);
//                        editor.putString("password", pas);
//                        editor.commit();
//                        JSONArray jsonArray;
//                        try {
//                            jsonArray = jsonObject.getJSONArray("tangan");
//
//
//                            if (jsonArray != null)
//                                for (int i = 0, ii = jsonArray.length(); i < ii; i++) {
//                                    Tangan food = new Gson().fromJson(jsonArray.getString(i), Tangan.class);
//                                    Log.e("a", food.getDate());
//                                    app.addTangan(food);
//                                }
//
//
//                            app.paixu();
//                        }catch (Exception e){
//
//                        }
//
//                        People p = new Gson().fromJson(jsonObject.getString("people"), People.class);
//                        app.setP(p);
//
//                        jsonArray = jsonObject.getJSONArray("book");
//
//
//                        if (jsonArray != null)
//                            for (int i = 0, ii = jsonArray.length(); i < ii; i++) {
//                                Book food = new Gson().fromJson(jsonArray.getString(i), Book.class);
//
//                                app.addBook(food);
//                            }
//
//                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                        startActivity(intent);
//                        finish();
//
//                    } else {
//                        ButterKnife.bind(LoginActivity.this);
//                        if (q.equals("-1")) {
//                            showToast("账号不存在");
//                        } else if (q.equals("-2")) {
//                            showToast("账号或密码错误");
//                        }
//
//
//                        SharedPreferences userSettings = getSharedPreferences("setting", 0);
//                        SharedPreferences.Editor editor = userSettings.edit();//获取编辑器
//                        editor.remove("password");
//                        editor.commit();
//                    }
//                } catch (JSONException e) {
//                    ButterKnife.bind(LoginActivity.this);
//                    e.printStackTrace();
//                }
//
//
//            }
//        });
//

        //此处为登录

    }


    //展示Toast
    public void showToast(final String s) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(LoginActivity.this, s, Toast.LENGTH_SHORT).show();
            }
        });

    }

    //判断是否是手机号
    public static boolean isMobile(String str) {
        Pattern p = null;
        Matcher m = null;
        boolean b = false;
        p = Pattern.compile("^[1][3,5,6,7,8][0-9]{9}$"); // 验证手机号
        m = p.matcher(str);
        b = m.matches();
        return b;
    }

    //处理返回值
    protected void onActivityResult(int request, int resultCode, Intent data) {
        switch (request) {
            case 1:
                if (resultCode == RESULT_OK) {
                    username.setText(data.getStringExtra("username"));
                    password.setText(data.getStringExtra("password"));
                    login(data.getStringExtra("username"), data.getStringExtra("password"));
                }
                break;
            case 1214:
                getReadPermission();

                break;
        }
    }

}
