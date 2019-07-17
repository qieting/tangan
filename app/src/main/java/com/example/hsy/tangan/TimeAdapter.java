package com.example.hsy.tangan;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by hsy on 2019/2/17.
 */

public class TimeAdapter extends RecyclerView.Adapter<TimeAdapter.ViewHolder> {

    List<String> times;
    Context context;

//    RequestOptions mRequestOptions = RequestOptions.circleCropTransform()
//            .diskCacheStrategy(DiskCacheStrategy.NONE)//不做磁盘缓存
//            .skipMemoryCache(true);//不做内存缓存

    static class ViewHolder extends RecyclerView.ViewHolder {


        TextView number;
        RelativeLayout re;

        public ViewHolder(View view) {
            super(view);
            number = view.findViewById(R.id.time);
            re = view.findViewById(R.id.re);


        }
    }


    public TimeAdapter(Context context, List<String> f) {
        super();
        this.context = context;
        times = f;
    }

    @Override
    public TimeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.timerecycle, parent, false);
        TimeAdapter.ViewHolder holder = new TimeAdapter.ViewHolder(view);

        //  holder.setIsRecyclable(false);
        return holder;

    }


    @Override
    public int getItemCount() {
        return times.size();
    }

    public void onBindViewHolder(TimeAdapter.ViewHolder holder, final int position) {

        final String s = times.get(position);
        if (position == 0)
            holder.number.setText(s + "  长按删除");
        else
            holder.number.setText(s);
        holder.re.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                times.remove(position);
                notifyDataSetChanged();
                return false;
            }
        });
    }


}
