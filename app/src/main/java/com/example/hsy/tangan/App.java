package com.example.hsy.tangan;

import android.app.Application;


import org.litepal.LitePalApplication;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import cn.bmob.v3.Bmob;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;


/**
 * Created by hsy on 2018/9/15.
 */

public class App extends LitePalApplication {

    OkHttpClient client;
    private final static String u = "http://47.106.225.178:8080/xuean_war/";
    private List<Tangan> tangans = new ArrayList<>();
    private People p;
    private List<Book> books =new ArrayList<>();
    private  List<Food> foods;
    public List<Food> getFoods(){
        return  foods;
    }
    public void  setFoods(List<Food> foods){
        this.foods=foods;
    }
    public void addBook(Book book){
        books.add(book);
    }

    public Book getBook(int i){
        return books.get(i);
    }

    public void cle(){
        if(tangans!=null)
        tangans.clear();
        else
            tangans=new ArrayList<>();
    }

    public void paixu(){
        Collections.sort(tangans, new Comparator<Tangan>() {
            @Override
            public int compare(Tangan o1, Tangan o2) {
                return o1.getTime().compareTo(o2.getTime());
            }
        });
    }
    public List<Tangan> getTangans() {
        return tangans;
    }

    public void addTangan(Tangan tangan) {
        tangans.add(tangan);
    }

    public void setTangans(List<Tangan> tangans) {
        this.tangans = tangans;
    }

    public OkHttpClient getClient() {
        if (client == null) {
            OkHttpClient.Builder client1 = new OkHttpClient.Builder();

            client1.cookieJar(new CookieJar() {
                private List<Cookie> cookies;

                @Override
                public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                    this.cookies = cookies;
                }

                @Override
                public List<Cookie> loadForRequest(HttpUrl url) {
                    if (cookies != null) {
                        return cookies;
                    } else {
                        return new ArrayList<Cookie>();
                    }
                }

            });

            client = client1.build();
        }
        return client;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        //初始化Bmob

        Bmob.initialize(this, "a5b90ee9a94eed7e7a9f9b1b231de856");
        // 注:自v3.5.2开始，数据sdk内部缝合了统计sdk，开发者无需额外集成，传渠道参数即可，不传默认没开启数据统计功能
        //Bmob.initialize(this, "Your Application ID","bmob");


    }

    public String getU() {
        return u;
    }

    public People getP() {
        return p;
    }

    public void setP(People p) {
        this.p = p;
    }
}
