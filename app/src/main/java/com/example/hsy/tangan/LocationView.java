package com.example.hsy.tangan;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * Created by hsy on 2018/11/20.
 */

public class LocationView extends View implements View.OnClickListener {

    boolean get = false;
    String l = "未定位";
    Context context;

    public LocationView(Context context){
        this(context,null);
    }

    public LocationView(Context context, @Nullable AttributeSet attrs) {

        super(context, attrs);
        this.context = context;
        this.setOnClickListener(this);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!get) {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.location);
            Matrix matrix1 = new Matrix();
 matrix1.setTranslate(200, 0);
            matrix1.preScale(0.13f, 0.13f);
            canvas.drawBitmap(bitmap, matrix1, new Paint());
        } else {
            Paint textPaint = new Paint();          // 创建画笔
            textPaint.setColor(Color.RED);// 设置颜色
            textPaint.setStyle(Paint.Style.FILL);// 设置样式

            textPaint.setTextSize(40);
            canvas.drawText(l, 10, 60, textPaint);
        }

    }

    private int getMySize(int defaultSize, int measureSpec) {
        int mySize = defaultSize;

        int mode = MeasureSpec.getMode(measureSpec);
        int size = MeasureSpec.getSize(measureSpec);

        switch (mode) {
            case MeasureSpec.UNSPECIFIED: {//如果没有指定大小，就设置为默认大小
                mySize = defaultSize;
                break;
            }
            case MeasureSpec.AT_MOST: {//如果测量模式是最大取值为size
                //我们将大小取最大值,你也可以取其他值
                mySize = defaultSize;
                break;
            }
            case MeasureSpec.EXACTLY: {//如果是固定的大小，那就不要去改变它
                mySize = size;
                break;
            }
        }
        return mySize;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getMySize(100, widthMeasureSpec);
        int height = getMySize(100, heightMeasureSpec);
        setMeasuredDimension(width, height);
    }


    @Override
    public void onClick(View v) {
        Log.e("Location", "点击了");
        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
        alphaAnimation.setDuration(800);
        alphaAnimation.setRepeatCount(1);
        alphaAnimation.setRepeatMode(Animation.REVERSE);
        alphaAnimation.setFillAfter(true);
        alphaAnimation.start();
        startAnimation(alphaAnimation);
        if (!get) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(2000);

                        while (l.equals("未定位")) {
                            Thread.sleep(500);
                        }
                        setAlpha(1);
                        get = !get;
                        postInvalidate();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
            getPosition();

        } else {
            get = !get;
            l = "未定位";
            postInvalidate();
        }

    }

    public String getLocation() {
        if(get)
        return l;
        else return "未定位";
    }



    public void getPosition() {
       final double c[] = new double[2];

        if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {//未开启定位权限
                //开启定位权限,200是标识码
                ActivityCompat.requestPermissions((Activity)context, new String[]{ Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1312);
                l = "无权限";
                return;
            }

        }
        //获取地理位置管理器
        LocationManager locationManager = (LocationManager)(context.getApplicationContext()).getSystemService(Context.LOCATION_SERVICE);

        String locationProvider;
        //获取所有可用的位置提供器
        List<String> providers = locationManager.getProviders(true);

        if (providers.contains(LocationManager.GPS_PROVIDER)) {
            //如果是GPS
            locationProvider = LocationManager.GPS_PROVIDER;
        } else if (providers.contains(LocationManager.NETWORK_PROVIDER)) {
            //如果是Network
            locationProvider = LocationManager.NETWORK_PROVIDER;
        } else {
            Toast.makeText(context, "没有适当的位置提供器", Toast.LENGTH_LONG).show();
            l = "无定位器";
            return;
        }


        Location location =locationManager.getLastKnownLocation(locationProvider);

        if(location==null) {
            locationProvider = LocationManager.NETWORK_PROVIDER;
            location = locationManager.getLastKnownLocation(locationProvider);
        }
        if (location != null) {
            //不为空,显示地理位置经纬度

            c[0] = location.getLatitude();

            c[1] = location.getLongitude();

            Log.e("经度" + c[1], "纬度" + c[0]);
        }
        else {
            l = "无定位能力";
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {


                try {
                    URL url = new URL("http://api.map.baidu.com/geocoder?output=json&location=" + c[0] + "," + c[1] + "&key=37492c0ee6f924cb5e934fa08c6b1676"); // 打开一个HttpURLConnection连接
                    HttpURLConnection urlConn = null;
                    urlConn = (HttpURLConnection) url.openConnection();
// 设置连接主机超时时间
                    urlConn.setConnectTimeout(5 * 1000);
// 设置从主机读取数据超时
                    urlConn.setReadTimeout(5 * 1000);
// 设置是否使用缓存 默认是true
                    urlConn.setUseCaches(true);
                    //设置为Post请求
                    urlConn.setRequestMethod("GET");
                    // urlConn设置请求头信息
                    // 设置请求中的媒体类型信息。
                    urlConn.setRequestProperty("Content-Type", "application/json");
// 设置客户端与服务连接类型
                    urlConn.addRequestProperty("Connection", "Keep-Alive");
// 开始连接
                    urlConn.connect();
// 判断请求是否成功
                    if (urlConn.getResponseCode() == 200) { // 获取返回的数据
                        InputStream is = urlConn.getInputStream();
                        ByteArrayOutputStream result = new ByteArrayOutputStream();
                        byte[] buffer = new byte[1024];
                        int length;
                        while ((length = is.read(buffer)) != -1) {
                            result.write(buffer, 0, length);
                        }
                        String r = result.toString("UTF-8");

                        l = new JSONObject(r).getJSONObject("result").getJSONObject("addressComponent").getString("city");
                        l=l+new JSONObject(r).getJSONObject("result").getJSONObject("addressComponent").getString("district");
                        Log.e("LocationView",l);
                    } else {
                        l = "网络错误";
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    l = "定位错误";
                }


            }
        }).start();
    }
}
