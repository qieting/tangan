package com.example.hsy.tangan;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;

import android.content.Intent;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;



/**
 * Created by hsy on 2019/2/13.
 */

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> {

    List<Note> notes;
    Context context;

    static class ViewHolder extends RecyclerView.ViewHolder {



        LinearLayout linearLayout;

        ImageButton button;
        TextView time,nextTime,name;
        public ViewHolder(View view) {
            super(view);

            linearLayout=view.findViewById(R.id.liii);
            time=view.findViewById(R.id.time);
            nextTime=view.findViewById(R.id.next_time);
            name=view.findViewById(R.id.name);

            button=view.findViewById(R.id.de);
        }
    }


    public NoteAdapter(Context context,List<Note> posts) {
        super();
        this.context = context;
        notes = new ArrayList<>();
        notes=posts;
    }
    void setNotes(List<Note> v){
        notes=v;
    }

    @Override
    public NoteAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.noterecycler, parent, false);
        NoteAdapter.ViewHolder holder = new NoteAdapter.ViewHolder(view);


        //  holder.setIsRecyclable(false);
        return holder;

    }


    @Override
    public int getItemCount() {
        return  notes.size();
    }

    public void onBindViewHolder(   NoteAdapter.ViewHolder holder, final int position) {

        final Note note =notes.get(position);
        holder.name.setText(note.getYaoming()+note.getJiliang());


        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                note.delete();
                EventBus.getDefault().post(new NEvent());
            }
        });
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(context,AddNoteActivity.class);
                intent.putExtra("id",note.getId());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
        if(note.isTixing()&&note.getNext()>0) {
            long l = note.getNext();

        //    Log.e("bug分析",l+"");
            holder.time.setText(note.getNextTime());
            if (l > 86400000l) {

                holder.nextTime.setText((l / 86400000l) + "天" + (l % 86400000l / 3600000l) + "时" + (l % 3600000l / 60000) + "分");
            } else {
                holder.nextTime.setText((l / 3600000l) + "时" + (l % 3600000l / 60000) + "分");
            }
        }else {
            holder.time.setText("");
            holder.nextTime.setText("");
        }

    }



}
