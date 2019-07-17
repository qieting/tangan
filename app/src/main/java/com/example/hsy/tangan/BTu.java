package com.example.hsy.tangan;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class BTu extends AppCompatActivity {

    List<Tangan> tangans;
    PieChart mPieChart;
    String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_btu);
        //饼状图

        tangans=((App)getApplication()).getTangans();
        mPieChart = (PieChart) findViewById(R.id.mPieChart);
        mPieChart.setUsePercentValues(true);
        mPieChart.getDescription().setEnabled(false);
        mPieChart.setExtraOffsets(5, 10, 5, 5);

        mPieChart.setDragDecelerationFrictionCoef(0.95f);

        mPieChart.setDrawHoleEnabled(true);
        mPieChart.setHoleColor(Color.WHITE);

        mPieChart.setTransparentCircleColor(Color.YELLOW);
        mPieChart.setTransparentCircleAlpha(110);

        mPieChart.setHoleRadius(58f);
        mPieChart.setTransparentCircleRadius(61f);

        mPieChart.setDrawCenterText(true);

        mPieChart.setRotationAngle(0);
        // 触摸旋转
        mPieChart.setRotationEnabled(true);
        mPieChart.setHighlightPerTapEnabled(true);



        //设置数据
        if (getIntent().getStringExtra("id") != null) {
            id = getIntent().getStringExtra("id");
            setDate(id);
        }else
        setData();

        mPieChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);

        Legend l = mPieChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);

        // 输入标签样式
        mPieChart.setEntryLabelColor(Color.WHITE);
        mPieChart.setEntryLabelTextSize(12f);

    }
    public void setData(){
        //模拟数据
        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();

        Tangan tangan =null;
       for(int i=tangans.size()-1;i>=0;i--){
           if(tangans.get(i).getDanbaizhi()>0)
           tangan=tangans.get(i);
       }
       if(tangan==null){
           Toast.makeText(BTu.this,"暂时没有饮食数据",Toast.LENGTH_SHORT).show();
           finish();
           return;
       }
        entries.add(new PieEntry(tangan.getDanbaizhi(), "蛋白质"));
        entries.add(new PieEntry(tangan.getZhifang(), "脂肪"));
        entries.add(new PieEntry(tangan.getTanglei(), "糖类"));
        PieDataSet dataSet = new PieDataSet(entries, tangans.get(0).getDate()+"食物成分图（g)");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);

        //数据和颜色
        ArrayList<Integer> colors = new ArrayList<Integer>();
        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);
        colors.add(ColorTemplate.getHoloBlue());
        dataSet.setColors(colors);
        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);
        mPieChart.setData(data);
        mPieChart.highlightValues(null);
        //刷新
        mPieChart.invalidate();

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
                    tangans.clear();
                    if (jsonArray != null)
                        for (int i = 0, ii = jsonArray.length(); i < ii; i++) {
                            Tangan food = new Gson().fromJson(jsonArray.getString(i), Tangan.class);
                           if(food.getDanbaizhi()>0)
                               tangans.add(food);
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                setData();
                            }
                        });

                        setData();

                } catch (
                        JSONException e)

                {
                    e.printStackTrace();
                }


            }
        });


        //此处为登录

    }

}
