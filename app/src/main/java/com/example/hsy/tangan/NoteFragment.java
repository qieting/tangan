package com.example.hsy.tangan;

import android.app.AlarmManager;
import android.app.Fragment;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.litepal.crud.DataSupport;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by hsy on 2019/2/13.
 */

public class NoteFragment extends Fragment {


    Context context;

    NoteAdapter noteAdapter;
    RecyclerView recyclerView;
    List<Note> posts ;
    People people;


    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        final View view = inflater.inflate(R.layout.note, container, false);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        //设置监听
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(MainActivity.this,"FAB clicked",Toast.LENGTH_SHORT).show();
//            }
//        });



        FloatingActionButton fab =view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent( view.getContext(),AddNoteActivity.class);
                startActivity(i);
            }
        });

        recyclerView = (RecyclerView) view.findViewById(R.id.notes_recycle);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        posts= DataSupport.findAll(Note.class);
        context=view.getContext().getApplicationContext();
        noteAdapter =new NoteAdapter(context,posts);

        recyclerView.setAdapter(noteAdapter);
        EventBus.getDefault().register(this);

   setAlarm();
        return view;

    }
    @Subscribe
    public void onEventMainThread(NEvent event) {

        posts.clear();
        posts= DataSupport.findAll(Note.class);

        noteAdapter .setNotes(posts);
        setAlarm();
        noteAdapter.notifyDataSetChanged();
    }

    void setAlarm(){
        if(posts==null||posts.size()==0){
            return;
        }
        long s =9999999999l;
        Calendar calendar =Calendar.getInstance();
        for(Note note:posts){
            if(note.isTixing()&&s>note.getNext()&&note.getNext()>0){
                s=note.getNext();
            }
        }
        if(s==9999999999l)
            return;
        s=s+System.currentTimeMillis();
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmService.class);
        intent.setAction(AlarmService.ACTION_ALARM);
        PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
      //  Log.e("设置提醒",s+"");
        if(Build.VERSION.SDK_INT <= 19){
            am.set(AlarmManager.RTC_WAKEUP, s, pendingIntent);
        }else{
            am.setExact(AlarmManager.RTC_WAKEUP,s,pendingIntent);
        }
    }


}

