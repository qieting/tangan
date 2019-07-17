package com.example.hsy.tangan;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class BookActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);
        Book book =((App)getApplication()).getBook(getIntent().getIntExtra("id",0));
        WebView myWebView =findViewById(R.id.web);
//        if (Build.VERSION.SDK_INT >= 19) {
//            myWebView.getSettings().setLoadsImagesAutomatically(true);
//        } else {
//            myWebView.getSettings().setLoadsImagesAutomatically(false);
//        }
        myWebView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        myWebView.getSettings().setJavaScriptEnabled(true);//可加载js方法
 //       myWebView.getSettings().setBuiltInZoomControls(false);
        myWebView.getSettings().setBlockNetworkImage(false);
        myWebView.loadUrl(book.getContent());

    }
}
