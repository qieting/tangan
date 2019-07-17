package com.example.hsy.tangan;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by hsy on 2019/1/13.
 */

@SuppressLint("AppCompatCustomView")
public class BindImage extends ImageView {

    BmobFile bmobFile;
    Context context;

    public enum KIND {HEAD,  BIG;}

    public BindImage(Context context) {
        super(context);
        this.context = context;
    }

    public BindImage(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context=context;
    }

    public BindImage(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context=context;
    }


    public void setBmobFile(final BmobFile bmobFile, KIND kind) {
        this.bmobFile = bmobFile;


        switch (kind) {
            case BIG:
                Glide.with(context)
                        .load(bmobFile.getUrl()+"!/fwfh/600x600")
                        .placeholder(R.drawable.place)
                        .crossFade()
                        .into(this);
                this.setClickable(true);
                this.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent =new Intent(context,BigImageActivity.class);
                        intent.putExtra("url",bmobFile.getFileUrl());
                        context.startActivity(intent);
                    }
                });
                break;
            case HEAD:
                Glide.with(context)
                        .load(bmobFile.getUrl()+"!/fwfh/200x200")
                        .centerCrop()
                        .bitmapTransform(new GlideCircleTransform(context))
                        .crossFade()
                        .into(this);
                break;
            default:
                break;
        }

    }


}
