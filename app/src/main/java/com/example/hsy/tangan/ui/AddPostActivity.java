package com.example.hsy.tangan.ui;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hsy.tangan.App;
import com.example.hsy.tangan.ChangeEvent;
import com.example.hsy.tangan.ChosePhotoEvent;
import com.example.hsy.tangan.HeadAdapter;
import com.example.hsy.tangan.LocationView;
import com.example.hsy.tangan.Post;
import com.example.hsy.tangan.R;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;

public class AddPostActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.titl_addpost)
    TextView head;

    @BindView(R.id.content_addpost)
    TextView content;
    @BindView(R.id.photo_addpost)
    RecyclerView photoRecycleView;

    @BindView(R.id.locationview)
    LocationView locationView;
    ProgressDialog progress;
    int hasUp=0;
    TitleLayout titleLayout;
    private static final int REQUEST_CODE_CHOOSE=1255;
    ArrayList<Uri> strings = new ArrayList<Uri>();
    HeadAdapter headAdapter;
    String number;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activaty_addpost);
        number=((App)getApplication()).getP().getPhoneNumber();
        EventBus.getDefault().register(this);
        ButterKnife.bind(this);
        titleLayout = (TitleLayout)findViewById(R.id.title_addpost);
        titleLayout.setTitle("发布新贴");
        titleLayout.changeIs(this);
        titleLayout.backAdd();

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        photoRecycleView.setLayoutManager(gridLayoutManager);
        headAdapter = new HeadAdapter(this, strings);
        photoRecycleView.setAdapter(headAdapter);





    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.is_title:
                upPost();
                break;

            default:
                break;

        }
    }

    public void upPost() {
        if(head.getText().toString().trim().length()==0){
            showToast("标题长度为空");
            return;
        }
        if(head.getText().toString().length()>20){
            showToast("标题长度不合格");
            return;
        }
        if(content.getText().toString().length()>300){
            showToast("内容不得超过300字，当前字数"+content.getText().toString().length());
            return;
        }
        if(head.getText().toString().trim().length()==0){
            showToast("内容不能为空");
            return;
        }
        titleLayout.changeIs(null);
        progress = new ProgressDialog(this);
        progress.setMessage("正在上传，请稍等");
        progress.setCancelable(false);

        progress.show();

        final Post post = new Post();
        post.setContent(content.getText().toString());
//添加一对一关联

        if(locationView.getLocation().length()>4){
            post.setLocation(locationView.getLocation());
        }

        final List<BmobFile> photos =new ArrayList();
        if(strings.size()!=0) {
            for (Uri s : strings) {

                String  url  =uriToURL(s);
                File file =new File(url);
                if(file.exists()) {
                    final BmobFile bmobFile = new BmobFile(file);
                    bmobFile.upload(new UploadFileListener() {
                        @Override
                        public void done(BmobException e) {
                            photos.add(bmobFile);
                            hasUp++;
                        }
                    });
                }
            }
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (hasUp<strings.size()) {
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progress.setMessage("当前进度: "+hasUp+"/"+strings.size());
                            }
                        });
                    }
                    post.setImages(photos);

                    post.setTitle(head.getText().toString());
                    post.setNumber(number);
                    post.save(new SaveListener<String>() {

                        @Override
                        public void done(String objectId, BmobException e) {
                            if (e == null) {
                                Log.i("bmob", "保存成功");

                                EventBus.getDefault().post(new ChangeEvent());
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        showToast("发送成功");
                                        progress .dismiss();
                                        finish();

                                    }
                                });

                            } else {
                                Log.i("bmob", "保存失败：" + e.getMessage());
                                showToast("发送失败" + e.getMessage());
                            }
                        }
                    });
                }
            }).start();
        }
        else
        {

            post.setTitle(head.getText().toString());
            post.setNumber(number);
            post.save(new SaveListener<String>() {

                @Override
                public void done(String objectId, BmobException e) {
                    if (e == null) {
                        Log.i("bmob", "保存成功");
                        showToast("发送成功");
                        progress .dismiss();
                        EventBus.getDefault().post(new ChangeEvent());
                        finish();
                    } else {
                        Log.i("bmob", "保存失败：" + e.getMessage());
                        showToast("发送失败" + e.getMessage());
                    }
                }
            });
        }
    }

    public void showToast(final String s) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Subscribe
    public void onEventMainThread(ChosePhotoEvent event) {
        pickPhoto(event.getNum());
    }

    public void  pickPhoto(int i){

        Matisse.from(this)
                .choose(MimeType.allOf()) // 选择 mime 的类型
                .countable(true)
                .maxSelectable(i) // 图片选择的最多数量
                //    .gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)

                .thumbnailScale(0.85f) // 缩略图的比例
                .imageEngine(new GlideEngine()) // 使用的图片加载引擎
                .forResult(REQUEST_CODE_CHOOSE); // 设置作为标记的请求码



    }

    List<Uri> mSelected;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
            mSelected = Matisse.obtainResult(data);
            strings.addAll(mSelected);
            headAdapter.notifyDataSetChanged();

            Log.d("Matisse", "mSelected: " + mSelected);
        }
    }

    public  String uriToURL(Uri uri){

        if (Build.VERSION.SDK_INT >= 19) {
            return  handleImageOnkitKat(uri);
            //4.4以上
        } else {
            return  handleImageBeforeKitKat(uri);
        }

    }
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public String  handleImageOnkitKat(Uri uri) {
        String imagePath = null;
        if (DocumentsContract.isDocumentUri(this, uri)) {
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals((uri.getAuthority()))) {
                String id = docId.split(":")[1];
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads.documents".equals((uri.getAuthority()))) {
                Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            imagePath = getImagePath(uri, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            imagePath = uri.getPath();
        }
        return imagePath;

    }
    private String  handleImageBeforeKitKat(Uri uri) {
        String imagepath = getImagePath(uri, null);
        return imagepath;
    }

    private String getImagePath(Uri uri, String selection) {
        String path = null;
        Cursor cursor = getBaseContext().getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()/*判断是否为空*/) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }


}
