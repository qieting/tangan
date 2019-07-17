package com.example.hsy.tangan;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.hsy.tangan.ui.TitleLayout;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    RelativeLayout luntanRela, hotpostRela, iRela, postRela,tuRela;
    ImageView luntansImge, hotpostImage, iView, postView,tuView;
    TitleLayout titleLayout;
    NoteFragment noteFragment;
    TanganFragment tanganFragment;
    PostFragment luntanFragment;
    IFragment iFragment;
    TuFragment tuFragment;
    int where = 1;
    App app;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EventBus.getDefault().register(this);

        app=(App)getApplication();
        initialize();


    }

    private void setDefaultFragment() {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        tanganFragment = new TanganFragment();


        transaction.replace(R.id.content_main, tanganFragment);

        transaction.commit();


    }

    private void initialize() {


        titleLayout = (TitleLayout) findViewById(R.id.title_main);
        titleLayout.setTitle("每日糖安");

        luntanRela=findViewById(R.id.luntan_rela);
        luntanRela.setOnClickListener(this);
        luntansImge=findViewById(R.id.luntan_under);


        tuView=findViewById(R.id.guanxuetang_under);
        tuRela=findViewById(R.id.guanxuetang_rela);
        tuRela.setOnClickListener(this);
        iRela = (RelativeLayout) findViewById(R.id.i_rela);
        iView = (ImageView) findViewById(R.id.i_under);
        hotpostRela = (RelativeLayout) findViewById(R.id.hotpost_rela);
        hotpostRela.setOnClickListener(this);
        hotpostImage = (ImageView) findViewById(R.id.hotpost_under);
        iRela.setOnClickListener(this);
        postRela = (RelativeLayout) findViewById(R.id.post_rela);
        postView = (ImageView) findViewById(R.id.post_under);
        postRela.setOnClickListener(this);
     //   postView.setImageResource(R.drawable.post);
        setDefaultFragment();
//        Glide.with(this)
//                .load(R.drawable.note1)
//                .into(hotpostImage);
        titleLayout.plusGone();

    }

    @Override
    public void onClick(View v) {

        //调换fragment，isphoto记录原来的状态，用于更改图标
        switch (v.getId()) {


            case R.id.post_rela:
                if (where == 1)
                    break;
                else {
                    changePhoto(where);
                    titleLayout.plusGone();

                    titleLayout.setTitle("每日糖安");
                    FragmentManager fm = getFragmentManager();
                    FragmentTransaction transaction = fm.beginTransaction();
                    transaction.show(tanganFragment);
                    transaction.commit();
                    postView.setImageResource(R.drawable.start1);
                    where = 1;

                }
                break;

            case R.id.i_rela:
                if (where == 2)
                    break;
                else {


                    FragmentManager fm = getFragmentManager();
                    FragmentTransaction transaction = fm.beginTransaction();

                    if (iFragment == null) {
                        iFragment = new IFragment();

                        transaction.add(R.id.content_main, iFragment);
                    }
                    changePhoto(where);
                    titleLayout.setPlus(1);
                    titleLayout.setTitle("我");

                    transaction.show(iFragment);
                    transaction.commit();
                    Glide.with(this)
                            .load(R.drawable.i)
                            .into(iView);
                    where = 2;

                }
                break;
            case R.id.hotpost_rela:
                if (where == 3)
                    break;
                else {


                    FragmentManager fm = getFragmentManager();
                    FragmentTransaction transaction = fm.beginTransaction();

                    if (noteFragment== null) {
                       noteFragment =new NoteFragment();
                        transaction.add(R.id.content_main, noteFragment);
                    }
                    changePhoto(where);
                    titleLayout.plusGone();
                    titleLayout.setTitle("提醒");

                    transaction.show(noteFragment);
                    transaction.commit();
                    Glide.with(this)
                            .load(R.drawable.mall)
                            .into(hotpostImage);
                    where = 3;

                }
                break;
            case R.id.luntan_rela:
                if (where == 4)
                    break;
                else {


                    FragmentManager fm = getFragmentManager();
                    FragmentTransaction transaction = fm.beginTransaction();

                    if (luntanFragment== null) {

                        luntanFragment =new PostFragment();
                        transaction.add(R.id.content_main, luntanFragment);
                    }
                    changePhoto(where);
                    titleLayout.setPlus(0);
                    titleLayout.setTitle("糖坛");

                    transaction.show(luntanFragment);
                    transaction.commit();
                    Glide.with(this)
                            .load(R.drawable.tea)
                            .into(luntansImge);
                    where = 4;

                }
                break;
            case R.id.guanxuetang_rela:
                if (where ==5)
                    break;
                else {


                    FragmentManager fm = getFragmentManager();
                    FragmentTransaction transaction = fm.beginTransaction();

                    if (tuFragment== null) {
                        tuFragment =new TuFragment();
                        transaction.add(R.id.content_main,tuFragment);
                    }
                   transaction.remove(tuFragment);
                    transaction.commit();


                    fm = getFragmentManager();
                    transaction = fm.beginTransaction();
                    tuFragment =new TuFragment();
                    transaction.add(R.id.content_main,tuFragment);
                    transaction.show(tuFragment);

                    transaction.commit();
                    Glide.with(this)
                            .load(R.drawable.post)
                            .into(tuView);
                    changePhoto(where);
                    titleLayout.plusGone();
                    titleLayout.setTitle("管血糖");

                    where = 5;

                }
                break;
            default:
                break;

        }
    }

    private void changePhoto(int ii) {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        switch (ii) {
            case 1:
                transaction.hide(tanganFragment);
                postView.setImageResource(R.drawable.start);
                break;

            case 2:
                transaction.hide(iFragment);
                Glide.with(this)
                        .load(R.drawable.i1)
                        .into(iView);

                break;
            case 3:
                transaction.hide(noteFragment);
                Glide.with(this)
                        .load(R.drawable.mall1)
                        .into(hotpostImage);

                break;
            case 4 :
                transaction.hide(luntanFragment);
                Glide.with(this)
                        .load(R.drawable.tea1)
                        .into(luntansImge);

                break;
            case 5 :
                transaction.hide(tuFragment);
                Glide.with(this)
                        .load(R.drawable.post1)
                        .into(tuView);

                break;


        }
        transaction.commit();


    }


    public void showToast(String s) {
        Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();
    }
    @Subscribe
    public void onEventMainThread(TEvent event) {

        final ProgressDialog progress;
        progress = new ProgressDialog(this);
        progress.setMessage("正在上传，请稍等");
        progress.setCancelable(false);

        progress.show();
        Tangan tangan=event.getTangan();
        String json =new Gson().toJson(tangan);
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, json);
        app.addTangan(tangan);
        //MediaType  设置Content-Type 标头中包含的媒体类型值
//        RequestBody requestBody = new FormBody.Builder()
//                .add("time", tangan.getDate())
//                .add("zhifang",tangan.getZhifang()+"")
//                .add("danbaizhi",tangan.getDanbaizhi()+"")
//                .add("tanglei",tangan.getDanbaizhi()+"")
//                .add("xuetang",tangan.getXuetang()+"")
//                .add("yaowu",tangan.getYaowu())
//                .build();


        //创建/Call
        final App app =(App) getApplication();
        Request request = new Request.Builder()
                .url(app.getU()+"addtanggna")//请求的url
                .post(body)
                .build();


        Call call = app.getClient().newCall(request);
        //加入队列 异步操作
        call.enqueue(new Callback() {
            //请求错误回调方法
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("登陆失败",e.getMessage());
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String body =response.body().string();
                Log.e("登陆错误",body);
                progress.dismiss();


                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        JSONArray jsonArray = null;
                        try {
                            jsonArray = new JSONArray(body);



                            if (jsonArray != null)
                               app.cle();
                            for (int i = 0, ii = jsonArray.length(); i < ii; i++) {
                                Tangan food = new Gson().fromJson(jsonArray.getString(i), Tangan.class);
                                app.addTangan(food);
                            }
                            app.paixu();
                            tanganFragment.re();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });



            }
        });


    }


}
