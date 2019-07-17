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
 * Created by hsy on 2019/2/21.
 */

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.ViewHolder> {


    Context context;
    List<Food>     friends=new ArrayList<>();
    boolean s[];

//    RequestOptions mRequestOptions = RequestOptions.circleCropTransform()
//            .diskCacheStrategy(DiskCacheStrategy.NONE)//不做磁盘缓存
//            .skipMemoryCache(true);//不做内存缓存

    static class ViewHolder extends RecyclerView.ViewHolder {


        TextView a, b;
        RelativeLayout r;

        public ViewHolder(View view) {
            super(view);

            a = view.findViewById(R.id.name);
            b = view.findViewById(R.id.is);
            r=view.findViewById(R.id.qq);

        }
    }


    public FoodAdapter(Context context) {
        super();
        this.context = context;
        friends.add(new Food("面食",70,5,15));
        friends.add(new Food("米饭",80,5,5));
        friends.add(new Food("水果",20,5,5));
        friends.add(new Food("油炸零食",30,40,30));
        friends.add(new Food("可乐汽水",5,5,5));
        friends.add(new Food("素菜",20,5,5));
        friends.add(new Food("荤菜",20,25,25));
        friends.add(new Food("豆类",10,35,25));

        friends.add(new Food("鱼类",10,15,30));

        s=new boolean[friends.size()];
        for(int i=0;i<s.length;i++){
            s[i]=false;
        }

    }

    @Override
    public FoodAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.food, parent, false);
        FoodAdapter.ViewHolder holder = new FoodAdapter.ViewHolder(view);


        //  holder.setIsRecyclable(false);
        return holder;

    }

    public List<Food> re(){
        List<Food> sss =new ArrayList<>();
        for(int i=0 ;i<s.length;i++){
            if(s[i]){
                sss.add(friends.get(i));
            }

        }
        return sss;
    }


    @Override
    public int getItemCount() {
        return friends.size();
    }

    public void onBindViewHolder(final FoodAdapter.ViewHolder holder, final int position) {

        Food food=friends.get(position);
        holder.a.setText(food.name);
        if(s[position]){
            holder.b.setVisibility(View.VISIBLE);
        }else {
            holder.b.setVisibility(View.GONE);
        }
        holder.r.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                s[position]=!s[position];
                if(s[position]){
                    holder.b.setVisibility(View.VISIBLE);
                }else {
                    holder.b.setVisibility(View.GONE);
                }
            }
        });
    }


}
