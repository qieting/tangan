package com.example.hsy.tangan;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import java.util.List;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by hsy on 2018/11/7.
 */

public class PostRecyclerAdapter extends RecyclerView.Adapter<PostRecyclerAdapter.ViewHolder> {

    List<Post> posts;
    Context context;
   String i;

//    RequestOptions mRequestOptions = RequestOptions.circleCropTransform()
//            .diskCacheStrategy(DiskCacheStrategy.NONE)//不做磁盘缓存
//            .skipMemoryCache(true);//不做内存缓存

    static class ViewHolder extends RecyclerView.ViewHolder {

        BindImage image1, image2,image3, head;
        ImageButton delete;
        TextView name, title, time;
        LinearLayout image_linear;
        RelativeLayout comment;
        RelativeLayout posto;

        public ViewHolder(View view) {
            super(view);
            image1 = view.findViewById(R.id.image1);
            image2 = view.findViewById(R.id.image2);
            image3 = view.findViewById(R.id.image3);
            head = view.findViewById(R.id.head);
            name=view.findViewById(R.id.name);
            time = view.findViewById(R.id.time);
            title = view.findViewById(R.id.titl);
            image_linear = view.findViewById(R.id.image_linear);
            comment = view.findViewById(R.id.comment_rela);
          //  kind=view.findViewById(R.id.kind);
            delete=view.findViewById(R.id.delete_post);
posto=view.findViewById(R.id.posto);

        }
    }


    public PostRecyclerAdapter(Context context, List<Post> others,String number) {
        super();
        this.context = context;
        this.posts = others;
        this.i=number;
    }

    @Override
    public PostRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post, parent, false);
        PostRecyclerAdapter.ViewHolder holder = new PostRecyclerAdapter.ViewHolder(view);

      //  holder.setIsRecyclable(false);
        return holder;

    }

    public void onBindViewHolder(PostRecyclerAdapter.ViewHolder holder, final int position) {
        final Post p = posts.get(position);

        holder.name.setText(p.getNumber());
        holder.time.setText(p.getCreatedAt());
        holder.title.setText(p.getTitle());

        List<BmobFile> files = p.getImages();
        if(p.getNumber().equals(i)){
            holder.delete.setVisibility(View.VISIBLE);
            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder= new AlertDialog.Builder(context);
                    builder.setTitle("提示");
                    builder.setMessage("确认删除该帖吗");
                    builder.setCancelable(true);
                    builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            delete(p.getObjectId());
                           posts.remove(position);
                            notifyItemRemoved(position);
                           // notifyDataSetChanged();
                        }
                    });

                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    builder.show();

                }
            });
        }else {
            holder.delete.setVisibility(View.GONE);
        }

//
//            holder.head.setBmobFile(p.getAuthor().getHead(), BindImage.KIND.HEAD);

        if (files != null && files.size() > 0) {
          //  Log.e(p.getTitle(),files.size()+"1");
            if(files.size()==1) {
                holder.image_linear.setVisibility(View.GONE);
                holder.image3.setVisibility(View.VISIBLE);
                holder.image3.setBmobFile(files.get(0), BindImage.KIND.BIG);

            }
          else {
                holder.image3.setVisibility(View.GONE);
                holder.image_linear.setVisibility(View.VISIBLE);
                holder.image1.setBmobFile(files.get(0), BindImage.KIND.BIG);
                holder.image2.setBmobFile(files.get(1), BindImage.KIND.BIG);
            }
        } else {
            holder.image3.setVisibility(View.GONE);
            holder.image_linear.setVisibility(View.GONE);
        }


//        holder.head.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent=new Intent(context,PMessageActivity.class);
//                if(!p.getAuthor().getObjectId().equals(P.getCurrentUser(P.class).getObjectId()))
//                intent.putExtra("id",p.getAuthor().getObjectId());
//                context.startActivity(intent);
//            }
//        });
        holder.posto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,PostMessage.class);
                intent.putExtra("id",p.getObjectId());
                context.startActivity(intent);
            }
        });
        holder.comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,PostMessage.class);
                intent.putExtra("id",p.getObjectId());
                context.startActivity(intent);
            }
        });

    }


    @Override
    public int getItemCount() {
        if (posts==null)
            return 0;
        else return posts.size();

    }

    public  void delete(String id){
        Post p2 = new Post();
        p2.setObjectId(id);
        p2.delete(new UpdateListener() {

            @Override
            public void done(BmobException e) {
                if(e==null){
                    Toast.makeText(context,"删除成功", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(context,"删除失败"+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

        });
    }
}