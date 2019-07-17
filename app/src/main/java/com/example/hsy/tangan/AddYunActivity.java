package com.example.hsy.tangan;

import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

public class AddYunActivity extends AppCompatActivity implements View.OnClickListener {

    int q = 30;
    int qqq = 0;
    BottomSheetDialog bottomSheet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_yun);
        //    RelativeLayout
        ((RelativeLayout) findViewById(R.id.zoulur)).setOnClickListener(this);
        ((RelativeLayout) findViewById(R.id.paobur)).setOnClickListener(this);
        ((RelativeLayout) findViewById(R.id.pingpangr)).setOnClickListener(this);
        ((RelativeLayout) findViewById(R.id.jiawur)).setOnClickListener(this);
        ((RelativeLayout) findViewById(R.id.taijir)).setOnClickListener(this);
        ((RelativeLayout) findViewById(R.id.guangchangwur)).setOnClickListener(this);
        ((RelativeLayout) findViewById(R.id.youyangr)).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.zoulur:
                qqq = 96;
                showBottomSheetDialog();
                break;
            case R.id.paobur:
                qqq = 405;
                showBottomSheetDialog();
                break;
            case R.id.pingpangr:
                qqq = 221;
                showBottomSheetDialog();
                break;
            case R.id.jiawur:
                qqq = 240;
                showBottomSheetDialog();
                break;
            case R.id.taijir:
                qqq = 183;
                showBottomSheetDialog();
                break;
            case R.id.guangchangwur:
                qqq = 264;
                showBottomSheetDialog();
                break;
            case R.id.youyangr:
                qqq = 300;
                showBottomSheetDialog();
                break;
            case R.id.isss:
                bottomSheet.dismiss();
                Tangan tangan = new Tangan();
                tangan.setZao(3);
                tangan.setYundong(qqq * q / 60);
                Log.e(tangan.getYundong()+"aaaa","a");
                EventBus.getDefault().post(new TEvent(tangan));
                finish();


                break;
            default:
                break;
        }

    }

    void showBottomSheetDialog() {
       bottomSheet = new BottomSheetDialog(this);//实例化BottomSheetDialog
        bottomSheet.setCancelable(true);//设置点击外部是否可以取消
        bottomSheet.setContentView(R.layout.select_time);//设置对框框中的布局
        final TextView textView = bottomSheet.findViewById(R.id.time);
        SeekBar seekBar = bottomSheet.findViewById(R.id.seekbar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                q = progress * 60 / 100;
                textView.setText(q + "分钟");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        bottomSheet.findViewById(R.id.isss).setOnClickListener(this);
        bottomSheet.show();//显示弹窗

    }
}
