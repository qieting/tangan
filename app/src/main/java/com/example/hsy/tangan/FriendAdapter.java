package com.example.hsy.tangan;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
 * Created by hsy on 2019/2/14.
 */

public class FriendAdapter  extends RecyclerView.Adapter<FriendAdapter.ViewHolder> {

    ArrayList<Note> notes;
    Context context;
   List<String>friends;
    AlertDialog alertDialog;

//    RequestOptions mRequestOptions = RequestOptions.circleCropTransform()
//            .diskCacheStrategy(DiskCacheStrategy.NONE)//不做磁盘缓存
//            .skipMemoryCache(true);//不做内存缓存

    static class ViewHolder extends RecyclerView.ViewHolder {



        TextView number;
        Button delete,xuezhe,yunzhe,bing;

        public ViewHolder(View view) {
            super(view);
            number=view.findViewById(R.id.number);
            delete=view.findViewById(R.id.delete);
            xuezhe=view.findViewById(R.id.see3);
            yunzhe=view.findViewById(R.id.see1);
            bing=view.findViewById(R.id.see2);


        }
    }


    public FriendAdapter(Context context, List<String> f) {
        super();
        this.context = context;
        notes = new ArrayList<>();
        this.friends=f;
    }

    @Override
    public FriendAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.friendsrecycler, parent, false);
        FriendAdapter.ViewHolder holder = new FriendAdapter.ViewHolder(view);

        //  holder.setIsRecyclable(false);
        return holder;

    }


    @Override
    public int getItemCount() {
        return friends.size();
    }

    public void onBindViewHolder(   FriendAdapter.ViewHolder holder, final int position) {

        final String s =friends.get(position);
        holder.number.setText(s);
        holder.xuezhe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(context,Tu.class);
                intent.putExtra("id",s);
                context.startActivity(intent);
            }
        });
        holder.yunzhe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(context,Tu.class);
                intent.putExtra("id",s);
                intent.putExtra("kind",1);
                context.startActivity(intent);
            }
        });
        holder.bing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(context,BTu.class);
                intent.putExtra("id",s);
                context.startActivity(intent);
            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                 AlertDialog.Builder builder= new AlertDialog.Builder(context);
                builder.setTitle("提示");
                builder.setMessage("确认删除该联系人吗");
                builder.setCancelable(true);
                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                       delete(s);
                       friends.remove(s);
                       notifyDataSetChanged();
                       alertDialog.dismiss();
                        // notifyDataSetChanged();
                    }
                });

                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                 alertDialog =builder.create();
                alertDialog.show();
            }
        });
    }

    public void delete(String s){
        RequestBody requestBody = new FormBody.Builder()
                .add("id",s)
                .build();

        //创建/Call
        final App app = (App)context.getApplicationContext();
        Request request = new Request.Builder()
                .url(app.getU() + "delete")//请求的url
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



            }
        });
    }


}
