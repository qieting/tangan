package com.example.hsy.tangan;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;


import java.util.List;

/**
 * Created by hsy on 2018/11/17.
 */

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {

    List<Comment> comments;
    Context context;
//    RequestOptions mRequestOptions = RequestOptions.circleCropTransform()
//            .diskCacheStrategy(DiskCacheStrategy.NONE)//不做磁盘缓存
//            .skipMemoryCache(true);//不做内存缓存

    static class ViewHolder extends RecyclerView.ViewHolder {


        ImageButton head;
        TextView name, title, time;



        public ViewHolder(View view) {
            super(view);

            head = view.findViewById(R.id.head);
            name = view.findViewById(R.id.name);
            time = view.findViewById(R.id.time);
            title = view.findViewById(R.id.titl);

        }
    }


    public CommentAdapter(Context context, List<Comment> others) {
        super();
        this.context = context;
        this.comments = others;
    }

    @Override
    public CommentAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_recycle, parent, false);
        CommentAdapter.ViewHolder holder = new CommentAdapter.ViewHolder(view);


        return holder;

    }

    public void onBindViewHolder(CommentAdapter.ViewHolder holder, int position) {
        final Comment p = comments.get(position);
        holder.name.setText(p.getNumber());
        holder.time.setText(p.getCreatedAt());
        holder.title.setText(p.getContent());
//        if (p.getUser().getHead() != null) {
//            Glide.with(context)
//                    .load(p.getUser().getHead().getUrl())
//                    .centerCrop()
//                    .bitmapTransform(new GlideCircleTransform(context))
//                    .crossFade().diskCacheStrategy(DiskCacheStrategy.SOURCE)
//                //    .apply(mRequestOptions)
//                    .into(holder.head);
//        }

//        holder.head.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(context, PMessageActivity.class);
//                intent.putExtra("id", p.getUser().getObjectId());
//                context.startActivity(intent);
//            }
//        });



    }


    @Override
    public int getItemCount() {
        if (comments == null)
            return 0;
        else return comments.size();

    }
}
