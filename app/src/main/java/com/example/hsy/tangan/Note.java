package com.example.hsy.tangan;

import android.util.Log;

import org.litepal.crud.DataSupport;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by hsy on 2019/2/13.
 */

public class Note extends DataSupport {
    private String yaoming;
    private String jiliang;
    private boolean tixing;
    private String beizhu;

    private int id;
    private String time;

    private int da;
    private long start;

    public int getDa() {
        return da;
    }

    Note() {
    }

    public long getNext() {
        if (time == null || time.length() < 1) {

                        return -1;
        }
        Calendar date = Calendar.getInstance();
        int hour = date.get(Calendar.HOUR_OF_DAY);
        int mini = date.get(Calendar.MINUTE);

        Calendar calendar =Calendar.getInstance();
        calendar.setTimeInMillis(start+da*86400000l);
      //  Log.e("bug查找"+start,da+"空");
        if(calendar.before(date)){

//            Log.e(calendar.getTimeInMillis()+"",date.getTimeInMillis()+"时间·"+(start+da*86400000l));
            return  -1;
        }

        String a = null, b = null;
        boolean cc = false;
        String mm[] = time.split(";");
        calendar=Calendar.getInstance();
        for (String s : mm) {

            String tim[] = s.split(":");
            if (Integer.parseInt(tim[0]) >= hour) {
                if (Integer.parseInt(tim[1]) > mini) {
                   calendar.add(Calendar.HOUR_OF_DAY,Integer.parseInt(tim[0])-hour);
                   calendar.add(Calendar.MINUTE,Integer.parseInt(tim[1]) - mini);
                   return calendar.getTimeInMillis()-date.getTimeInMillis();
                }
            }

        }
        String tim[] = mm[0].split(":");
        calendar.add(Calendar.HOUR_OF_DAY,Integer.parseInt(tim[0])+24-hour);
        calendar.add(Calendar.MINUTE,Integer.parseInt(tim[1]) - mini);
        return calendar.getTimeInMillis()-date.getTimeInMillis();

    }

    public String getNextTime() {
        if (time == null || time.length() < 1) {
            return null;
        }
        Calendar date = Calendar.getInstance();
        int hour = date.get(Calendar.HOUR_OF_DAY);
        int mini = date.get(Calendar.MINUTE);
        String mm[] = time.split(";");
        for (String s : mm) {

            String tim[] = s.split(":");
            if (Integer.parseInt(tim[0]) >= hour) {
                if (Integer.parseInt(tim[1]) > mini) {
                    return s;
                }
            }

        }
        return mm[0];


    }

    public String getYaoming() {
        return yaoming;
    }

    public void setYaoming(String yaoming) {
        this.yaoming = yaoming;
    }

    public String getJiliang() {
        return jiliang;
    }

    public void setJiliang(String jiliang) {
        this.jiliang = jiliang;
    }


    public boolean isTixing() {
        if (Calendar.getInstance().getTimeInMillis() < start+da*8640000000l)
            return tixing;
        else return false;
    }

    public void setTixing(boolean tixing) {
        this.tixing = tixing;
    }


    public String getBeizhu() {
        return beizhu;

    }

    public void setBeizhu(String beizhu) {
        this.beizhu = beizhu;
    }


    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public void setDa(int da) {
        this.da = da;
    }
}
