package com.example.hsy.tangan;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hsy.tangan.ui.TitleLayout;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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

/**
 * Created by hsy on 2019/2/21.
 */

public class PageFragment extends android.support.v4.app.Fragment implements View.OnClickListener {

    public static final String ARG_PAGE = "ARG_PAGE";
    private int mPage;
    int da = 1;
    TextView editText;
    RecyclerView recyclerView;
    List<Food> foods;
    Context context;
    FoodChooseAdapter foodChooseAdapter;
    List<Tangan> tangans;
    PieChart mPieChart;
    LineChartView mLcvDemo;;
    List<Float>x ;

    public static PageFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);

        PageFragment pageFragment = new PageFragment();
        pageFragment.setArguments(args);

        return pageFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        mPage = getArguments().getInt(ARG_PAGE);
        da=mPage;
        Log.e("2","初始化"+da);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = null;
        Log.e("3","初始化"+da);
        if (mPage < 4) {
            view = inflater.inflate(R.layout.fragment_page, container, false);

            Button button = view.findViewById(R.id.submmit);
            button.setOnClickListener(this);
            Button add = view.findViewById(R.id.add);
            add.setOnClickListener(this);
            editText = view.findViewById(R.id.text);
            recyclerView = view.findViewById(R.id.recy);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
            recyclerView.setLayoutManager(linearLayoutManager);

            context = view.getContext().getApplicationContext();
        } else if (mPage == 4) {
            view = inflater.inflate(R.layout.activity_btu, container, false);

           view.findViewById(R.id.qqqqq).setVisibility(View.GONE);
            context = view.getContext();
            setBTu(view);
        } else if (mPage == 5) {
            view = inflater.inflate(R.layout.activity_tu, container, false);
            view.findViewById(R.id.qqqqq).setVisibility(View.GONE);
            context = view.getContext();
            setTu(view, 0);
        } else if(mPage==6){
            view = inflater.inflate(R.layout.activity_tu, container, false);
            view.findViewById(R.id.qqqqq).setVisibility(View.GONE);
            context = view.getContext();
            setTu(view, 1);
        }
        return view;
    }

    public void setTu(View view, int kind) {
      x = new ArrayList<>();

        App app = (App)getActivity().getApplication();


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



    mLcvDemo =view.findViewById(R.id.lcv_demo);

    init(kind);


}

    public void init( int kind) {

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


    public void setBTu(View view) {
        tangans = ((App) getActivity().getApplication()).getTangans();
        mPieChart = (PieChart) view.findViewById(R.id.mPieChart);
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

    public void setData() {
        //模拟数据
        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();

        Tangan tangan = null;
        for (int i =tangans.size()-1; i >0; i--) {
            if (tangans.get(i).getDanbaizhi() > 0) {
                tangan = tangans.get(i);
                break;
            }
        }
        if (tangan == null) {
            Toast.makeText(context, "暂时没有饮食数据", Toast.LENGTH_SHORT).show();

            return;
        }
      //  Log.e("选中得tangan得日期",tangan.getDate());
        entries.add(new PieEntry(tangan.getDanbaizhi(), "蛋白质"));
        entries.add(new PieEntry(tangan.getZhifang(), "脂肪"));
        entries.add(new PieEntry(tangan.getTanglei(), "糖类"));
        PieDataSet dataSet = new PieDataSet(entries, tangans.get(0).getDate() + "食物成分图（g)");
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


    public void s(int s) {
        editText.setText("总质量" + s + "g");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submmit:

                if (foodChooseAdapter == null) {
                    Toast.makeText(context, "您还没有选择任何数据", Toast.LENGTH_SHORT).show();
                    break;
                }
                Food food = foodChooseAdapter.sun();

                Tangan tangan = new Tangan();
                tangan.setTanglei(food.tanglei);
                tangan.setDanbaizhi(food.danbaizhi);
                tangan.setZhifang(food.zhifang);
                tangan.setZao(da);
                EventBus.getDefault().post(new TEvent(tangan));

                break;
            case R.id.add:
                Intent intent = new Intent(v.getContext(), FoodActivity.class);
                intent.putExtra("id", da);
                startActivity(intent);
                break;
            default:
                break;
        }

    }

    @Subscribe
    public void onEventMainThread(DDEvent event) {
        int q = event.s;
        if (q == da) {
            if (((App) context).getFoods() != null) {
                foods = ((App) context).getFoods();
                foodChooseAdapter = new FoodChooseAdapter(context, foods, this);
                recyclerView.setAdapter(foodChooseAdapter);
            }
        }
    }
}