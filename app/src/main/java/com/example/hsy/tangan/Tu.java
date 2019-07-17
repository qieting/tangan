package com.example.hsy.tangan;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import lecho.lib.hellocharts.formatter.LineChartValueFormatter;
import lecho.lib.hellocharts.formatter.SimpleLineChartValueFormatter;
import lecho.lib.hellocharts.gesture.ContainerScrollType;
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.view.LineChartView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Tu extends AppCompatActivity {

    LineChartView mLcvDemo;
    ArrayList<Float> x;
    int v;
    String id;
    int kind = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tu);
        x = new ArrayList<>();
        kind = getIntent().getIntExtra("kind", 0);
        if (getIntent().getStringExtra("id") != null) {
            id = getIntent().getStringExtra("id");
            setDate(id);
        } else {
            App app = (App) getApplication();


            for (Tangan tangan : app.getTangans()
                    ) {
                if (kind == 0) {
                    if (tangan.getXuetang() > 0) {
                        x.add(tangan.getXuetang());
                    }
                } else {
                    if (tangan.getYundong() > 0) {
                        x.add((tangan.getYundong() + 0.0f));
                    }
                }
            }
        }


        mLcvDemo = findViewById(R.id.lcv_demo);
        init();

    }

    public void setDate(String id) {
        RequestBody requestBody = new FormBody.Builder()
                .add("id", id)
                .build();

        //创建/Call
        final App app = (App) getApplication();
        Request request = new Request.Builder()
                .url(app.getU() + "getothertangan")//请求的url
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
                try {
                    JSONArray jsonArray = new JSONArray(body);
                    List<Tangan> tangans = new ArrayList<>();
                    if (jsonArray != null)
                        for (int i = 0, ii = jsonArray.length(); i < ii; i++) {
                            Tangan food = new Gson().fromJson(jsonArray.getString(i), Tangan.class);
                            tangans.add(food);

                        }

                    Collections.sort(tangans, new Comparator<Tangan>() {
                        @Override
                        public int compare(Tangan o1, Tangan o2) {
                            return o1.getTime().compareTo(o2.getTime());
                        }
                    });
                    for (Tangan food : tangans) {
                        if (kind == 0 && food.getXuetang() > 0)
                            x.add(food.getXuetang());
                        else if (kind == 1 && food.getYundong() > 0)
                            x.add(food.getYundong() + 0.0f);
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            init();
                        }
                    });

                } catch (
                        JSONException e)

                {
                    e.printStackTrace();
                }


            }
        });


        //此处为登录

    }


    public void init() {

        ArrayList<Line> lines = new ArrayList<Line>();
        ArrayList<PointValue> values = new ArrayList<PointValue>();
        List<AxisValue> mAxisXValues = new ArrayList<AxisValue>();

        SimpleDateFormat format = new SimpleDateFormat("MM-dd");
        Calendar c = Calendar.getInstance();


        //X
        for (int i = 0; i < 7; i++) {
            Date d = c.getTime();
            //  String day = format.format(d);
            mAxisXValues.add(new AxisValue(i).setLabel(i + ""));
            c.add(Calendar.DATE, 1);

        }

        //点
        //   float detail[] =ChangeGet.details;
        for (int i = 0; i < x.size(); i++) {
            values.add(new PointValue(i, x.get(i)));
        }


//        String[] date = {"10-22","11-22","12-22","1-22","6-22","5-23","5-22","6-22","5-23","5-22"};//X轴的标注
//        int[] score= {50,42,90,33,10,74,22,18,79,20};//图表的数据点
/*
添加坐标点值
 */
        Line line = new Line(values).setColor(Color.parseColor("#FFCD41"));  //折线的颜色（橙色）
        // List<Line> lines = new ArrayList<Line>();
        line.setShape(ValueShape.CIRCLE);//折线图上每个数据点的形状  这里是圆形 （有三种 ：ValueShape.SQUARE  ValueShape.CIRCLE  ValueShape.DIAMOND）
        line.setCubic(false);//曲线是否平滑，即是曲线还是折线
        line.setFilled(false);//是否填充曲线的面积
        line.setHasLabels(true);//曲线的数据坐标是否加上备注
//      line.setHasLabelsOnlyForSelected(true);//点击数据坐标提示数据（设置了这个line.setHasLabels(true);就无效）
        line.setHasLines(true);//是否用线显示。如果为false 则没有曲线只有点显示
        line.setHasPoints(true);//是否显示圆点 如果为false 则没有原点只有点显示（每个数据点都是个大的圆点）


        line.setFormatter(new SimpleLineChartValueFormatter(1));
        lines.add(line);
        LineChartData data = new LineChartData();
        data.setLines(lines);


        //坐标轴
        Axis axisX = new Axis(); //X轴
        axisX.setHasTiltedLabels(true);  //X坐标轴字体是斜的显示还是直的，true是斜的显示
        // axisX.setTextColor(Color.WHITE);  //设置字体颜色
        axisX.setName("时间");  //表格名称
        axisX.setTextSize(13);//设置字体大小
        axisX.setMaxLabelChars(10); //最多几个X轴坐标，意思就是你的缩放让X轴上数据的个数7<=x<=mAxisXValues.length
        axisX.setValues(mAxisXValues);  //填充X轴的坐标名称
        data.setAxisXBottom(axisX); //x 轴在底部
        //data.setAxisXTop(axisX);  //x 轴在顶部
        axisX.setHasLines(true); //x 轴分割线

        // Y轴是根据数据的大小自动设置Y轴上限(在下面我会给出固定Y轴数据个数的解决方案)
        Axis axisY = new Axis();  //Y轴

        if (kind == 0)
            axisY.setName("血糖值");//y轴标注
        else axisY.setName("卡路里");
        axisY.setTextSize(13);//设置字体大小
        data.setAxisYLeft(axisY);  //Y轴设置在左边
        //data.setAxisYRight(axisY);  //y轴设置在右边
        //设置行为属性，支持缩放、滑动以及平移
        LineChartView lineChart = mLcvDemo;
        lineChart.setInteractive(true);
        lineChart.setZoomType(ZoomType.HORIZONTAL);
        lineChart.setMaxZoom((float) 2);//最大方法比例
        lineChart.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);
        lineChart.setLineChartData(data);
        //  lineChart.setVisibility(View.VISIBLE);


        // mLcvDemo.setLineChartData(data);


        //   changeAdapter.notifyDataSetChanged();


    }


}
