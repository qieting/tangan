package com.example.hsy.tangan;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.hsy.tangan.ui.TitleLayout;

import org.greenrobot.eventbus.EventBus;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class AddNoteActivity extends AppCompatActivity implements View.OnClickListener, TimePickerDialog.OnTimeSetListener {

    EditText yaoming, jiliang, day, beizhu;
    RelativeLayout addtime;
    List<String> times;
    RecyclerView recyclerView;
    TitleLayout titleLayout;
    TimeAdapter timeAdapter;
    int i;

    boolean se = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        i = getIntent().getIntExtra("id", -1);

        yaoming = findViewById(R.id.yaoming);
        jiliang = findViewById(R.id.jiliang);
        day = findViewById(R.id.day);
        beizhu = findViewById(R.id.beizhu);
        addtime = findViewById(R.id.addtime);
        recyclerView = findViewById(R.id.recycler);
        times = new ArrayList<>();
        addtime.setOnClickListener(this);
        final Button button = findViewById(R.id.kai);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (se)
                    button.setBackgroundResource(R.drawable.btn_normal);
                else {
                    button.setBackgroundResource(R.drawable.btn_pressed);
                }
                se = !se;
            }
        });

        if (i != -1) {
            Note note = DataSupport.find(Note.class, i);
            yaoming.setText(note.getYaoming());
            jiliang.setText(note.getJiliang());

            beizhu.setText(note.getBeizhu());
            times = new ArrayList<>(Arrays.asList(note.getTime().split(";")));
            se = note.isTixing();
            if (!se) {
                day.setText("0");
                button.setBackgroundResource(R.drawable.btn_normal);
            } else {
                day.setText(note.getDa() + "");
                button.setBackgroundResource(R.drawable.btn_pressed);

            }
        }
        timeAdapter = new TimeAdapter(this, times);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(timeAdapter);
        titleLayout = findViewById(R.id.titlelayout);
        titleLayout.changeIs(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (yaoming.getText() == null || yaoming.getText().length() == 0) {
                    showToast("药名不能为空");
                    return;
                }
                if (jiliang.getText() == null || jiliang.getText().length() == 0) {
                    showToast("剂量不能为空");
                    return;
                }
                if (day.getText() == null || day.getText().length() == 0) {
                    showToast("服药天数不能为空");
                    return;
                }
                if (times.size() == 0) {
                    showToast("必须设置提醒时间");
                    return;
                }
                Note note = new Note();

                note.setYaoming(yaoming.getText().toString());
                note.setJiliang(jiliang.getText().toString());
                int ddd = Integer.parseInt(day.getText().toString());
                note.setDa(ddd);
                Calendar calendar = Calendar.getInstance();
                note.setStart(calendar.getTimeInMillis());

                if (beizhu.getText().toString() != null) {
                    note.setBeizhu(beizhu.getText().toString());
                }
                Collections.sort(times);
                StringBuffer stringBuffer = new StringBuffer();
                for (String s : times
                        ) {
                    stringBuffer.append(s).append(";");

                }
                note.setTime(stringBuffer.toString());
                Log.e("是否提醒", se + "");
                if (se)
                    note.setTixing(se);
                else {
                    note.setToDefault("tixing");
                }
                if (i != -1) {
                    try {


                            DataSupport.delete(Note.class,i);

                            note.save();


                    } catch (Exception e) {
                        e.printStackTrace();
                        showToast("您没有做任何改动");
                    }
                } else
                    note.save();


                EventBus.getDefault().post(new NEvent());
                finish();


            }
        });
    }

    @Override
    public void onClick(View v) {

        TimePickerDialog t = new TimePickerDialog(this, this, 0, 0, true);
        t.show();
    }

    public void showToast(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        Log.e("设置时间",hourOfDay + ":" + minute);
        if (!times.contains(hourOfDay + ":" + minute))
            times.add(hourOfDay + ":" + minute);

        else  if (Build.VERSION.SDK_INT > 20) {
            showToast("此时间您已经选择过了");
        }

        timeAdapter.notifyDataSetChanged();

    }
}
