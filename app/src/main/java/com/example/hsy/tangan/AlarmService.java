package com.example.hsy.tangan;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;

public class AlarmService extends Service {
    public static String ACTION_ALARM = "action_alarm";
    private Handler mHanler = new Handler(Looper.getMainLooper());

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mHanler.post(new Runnable() {
            @Override
            public void run() {


                EventBus.getDefault().post(new NEvent());
           //     Log.e("tongzhi","通知了");
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
                NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);


                NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext());

                builder.setChannelId("note")
                        .setDefaults(Notification.DEFAULT_SOUND)
                        .setContentTitle("该服药啦")
                        .setTicker("该吃药了") //通知首次出现在通知栏，带上升动画效果的
                        .setContentText("点击查看详细服药信息")
                        //设置通知显示一种大图
                        .setPriority(NotificationCompat.PRIORITY_MAX)
                        //设置通知的重要性    如果级别低了，不会在屏幕上显示
                        .setWhen(System.currentTimeMillis())
                        .setContentIntent(pendingIntent)
                        .setVibrate(new long[]{0, 1000, 1000, 1000})//设置震动
                        .setLights(Color.BLUE, 1000, 1000)//设置LED灯
                        // .setDefaults(NotificationCompat.DEFAULT_ALL)//采用系统默认设置
                        .setSmallIcon(R.drawable.tang)
                .setChannelId("note")
                ;
                manager.notify(1845,builder.build());


            }
        });
        return super.onStartCommand(intent, flags, startId);
    }

}