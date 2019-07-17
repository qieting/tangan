package com.example.hsy.tangan;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class BigImageActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_big_image);
        String url =getIntent().getStringExtra("url");
        ImageView imageView =findViewById(R.id.image);
        Glide.with(this)
                .load(url)
                .placeholder(R.drawable.place)
                .crossFade()
                .into(imageView);
        imageView.setClickable(true);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
