package com.example.hsy.tangan;

import android.content.Intent;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by hsy on 2019/2/13.
 */

public class Tangan {
    private float zhifang=-1,tanglei=-1,danbaizhi=-1,xuetang=-1;

    private int nian;
    private int yue;
    private int ri;
    private int yundong;


    public String getA(){
        if(time!=null){
            setDate(time);
        }
        return nian+"-"+yue+"-"+ri;
    }
    public int getZao() {
        return zao;
    }

    public void setZao(int zao) {
        this.zao = zao;
        time= nian+"-"+yue+"-"+ri+"-"+zao;
    }

    private int zao;

    private String time;
    public  Tangan(){

        Calendar date =Calendar.getInstance();
       nian=date.get(Calendar.YEAR);
       yue=date.get(Calendar.MONTH)+1;
       ri=date.get(Calendar.DAY_OF_MONTH);
       zao=1;
       time=getDate();

    }

    public String  getDate(){
       if(time!=null){
           setDate(time);
       }
        return nian+"-"+yue+"-"+ri+"-"+zao;
    }
    public void setDate(String s ){
        String  ss[]=s.split("-");
        nian= Integer.parseInt(ss[0]);
        yue= Integer.parseInt(ss[1]);
        ri= Integer.parseInt(ss[2]);
        zao=Integer.parseInt(ss[3]);
    }
    public float getZhifang() {
        if(zhifang<0)
            return 0;
        return zhifang;
    }

    public void setZhifang(float zhifang) {
        this.zhifang = zhifang;
    }

    public float getTanglei() {
        if(tanglei<0)
            return  0;
        return tanglei;
    }

    public void setTanglei(float tanglei) {
        this.tanglei = tanglei;
    }

    public float getDanbaizhi() {
        if(danbaizhi<0)
            return 0;
        return danbaizhi;
    }

    public void setDanbaizhi(float danbaizhi) {
        this.danbaizhi = danbaizhi;
    }

    public float getXuetang() {
        if(xuetang<0)
            return 0;
        return xuetang;
    }

    public void setXuetang(float xuetang) {
        this.xuetang = xuetang;
    }




    public void setTime(String time) {
        this.time = time;

    }
    public String getTime(){
        return time;
    }

    public int getYundong() {
        if(yundong<0)
            return 0;
        return yundong;
    }

    public void setYundong(int yundong) {
        this.yundong = yundong;
    }
}
