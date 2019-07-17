package com.example.hsy.tangan;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;

import java.util.Calendar;

/**
 * Created by hsy on 2019/2/13.
 */

public class TanganDialog extends Dialog {

    int da=1;
    Context mContext;
    public TanganDialog(@NonNull Context context) {
        super(context);
        mContext=context;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.add);
        setCanceledOnTouchOutside(true);
        this.setTitle(null);

        final EditText xuetang=findViewById(R.id.xuetang);



        final RadioGroup mRadioGroup =findViewById(R.id.rg_sex);//默认选中
        ((RadioButton)mRadioGroup.getChildAt(0)).setChecked(true);//默认选中
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){

            @Override
            public void onCheckedChanged(RadioGroup rg, int checkedId) {
                if(checkedId == R.id.rb_man){
                    da=1;
                }else if(checkedId == R.id.rb_woman){
                   da=2;
                }else{
                    da=3;
                }
            }
        });

        TextView time =findViewById(R.id.time);
        Calendar date =Calendar.getInstance();

        time.setText( date.get(Calendar.YEAR)+"年"+(date.get(Calendar.MONTH)+1)+"月"+date.get(Calendar.DAY_OF_MONTH)+"日");
        final TextView textView=findViewById(R.id.xuetangE) ;
      //  final EditText yaowu =findViewById(R.id.yaowu);
        Button submmit=findViewById(R.id.submmit);

        xuetang.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    textView.setVisibility(View.INVISIBLE);
                    return;
                }
                try {
                    Float q =Float.parseFloat(xuetang.getText().toString());
                    if(q>10){
                        textView.setVisibility(View.VISIBLE);
                    }else {
                        textView.setVisibility(View.INVISIBLE);
                    }
                }catch (Exception e){
                    textView.setVisibility(View.INVISIBLE);
                }


            }
        });
        submmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    try {
                        Tangan tangan = new Tangan();

                        tangan.setZao(da);
                        tangan.setXuetang(Float.parseFloat(xuetang.getText().toString()));

                        EventBus.getDefault().post(new TEvent(tangan));

                        TanganDialog.this.dismiss();
                    }catch (Exception e){
                        Toast.makeText(mContext,"数据格式错误",Toast.LENGTH_SHORT).show();
                    }

            }
        });
    }
}
