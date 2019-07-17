package com.example.hsy.tangan;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.hsy.tangan.ui.TitleLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;

public class PostMessage extends AppCompatActivity implements View.OnClickListener {

    Post post;

    List<Comment> comments = new ArrayList<>();


    ImageButton head;
    TextView name, time, content;
    RelativeLayout comment;

    String number;
    TitleLayout titleLayout;




    @BindView(R.id.photo_message)
    RecyclerView photoRecycle;

    @BindView(R.id.comment_recy)
    RecyclerView commentRecycle;

    @BindView(R.id.commentedit_message)
    EditText commentEdit;

    @BindView(R.id.postbtn_message)
    Button commentBtn;
    @BindView(R.id.titl)
    TextView titl;

    @BindView(R.id.location)
    TextView location;
    CommentAdapter commentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_message);
        number=((App)getApplication()).getP().getPhoneNumber();
        ButterKnife.bind(this);
        commentAdapter = new CommentAdapter(this, comments);
        titleLayout =(TitleLayout) findViewById(R.id.title_postmessage);
        head =(ImageButton) findViewById(R.id.head);
        name = (TextView)findViewById(R.id.name);
        time = (TextView) findViewById(R.id.time);

        comment = (RelativeLayout) findViewById(R.id.comment_rela);

        content = (TextView)findViewById(R.id.content);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        commentRecycle.setLayoutManager(linearLayoutManager);
        commentRecycle.setAdapter(commentAdapter);

        commentBtn.setOnClickListener(this);
        getPost(getIntent().getStringExtra("id"));

    }

    public void init() {
        if (post == null) {
            showToast("错误的帖子id");
            finish();
            return;
        }

//        if (post.getAuthor().getHead() != null) {
////            RequestOptions mRequestOptions = RequestOptions.circleCropTransform()
////                    .diskCacheStrategy(DiskCacheStrategy.NONE)//不做磁盘缓存
////                    .skipMemoryCache(true);//不做内存缓存
//            Glide.with(this)
//                    .load(post.getAuthor().getHead().getUrl())
//                    .centerCrop()
//                    .bitmapTransform(new GlideCircleTransform(this))
//                    .crossFade().diskCacheStrategy(DiskCacheStrategy.SOURCE)
//
//                    //  .apply(mRequestOptions)
//                    .into(head);
//        }
//        head.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(PostMessage.this, PMessageActivity.class);
//                intent.putExtra("id", post.getAuthor().getObjectId());
//                startActivity(intent);
//            }
//        });
        titleLayout.setTitle("查看帖子");
        titleLayout.backAdd();
        titleLayout.plusGone();
        name.setText(post.getNumber());
        time.setText(post.getCreatedAt());
        titl.setText(post.getTitle());
       // kind.setText(post.getKind());
        if(post.getLocation()!=null){
            location.setText(post.getLocation());
        }
        else location.setText("");
        content.setText(post.getContent());
        if (post.getImages() == null) {
            photoRecycle.setVisibility(View.GONE);
        }else {
            PhotoAdapter photoAdapter =new PhotoAdapter(this,post.getImages());
            LinearLayoutManager linearLayoutManager =new LinearLayoutManager(this);
            photoRecycle.setLayoutManager(linearLayoutManager);
            photoRecycle.setAdapter(photoAdapter);

        }


    }


    public void getPost(String id) {
        BmobQuery<Post> query = new BmobQuery<Post>();
      //  query.include("author");// 希望在查询帖子信息的同时也把发布人的信息查询出来
        query.getObject(id, new QueryListener<Post>() {

            @Override
            public void done(Post object, BmobException e) {
                if (e == null) {

                    post = object;

                    init();
                } else {

                }
            }

        });

        BmobQuery<Comment> query1 = new BmobQuery<Comment>();
//用此方式可以构造一个BmobPointer对象。只需要设置objectId就行
        Post post1 = new Post();
        post1.setObjectId(id);
        query1.addWhereEqualTo("post", new BmobPointer(post1));
//希望同时查询该评论的发布者的信息，以及该帖子的作者的信息，这里用到上面`include`的并列对象查询和内嵌对象的查询
      //  query1.include("user");
        query1.findObjects(new FindListener<Comment>() {

            @Override
            public void done(List<Comment> objects, BmobException e) {
                if(objects!=null) {
                    comments.addAll(objects);

                    commentAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    public void showToast(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.postbtn_message:
                if (commentEdit.getText().toString().trim().length() == 0)
                    return;
           //     P user = P.getCurrentUser(P.class);
                final Comment comment = new Comment();
                comment.setContent(commentEdit.getText().toString().trim());
                comment.setPost(post);
                comment.setNumber(number);
                comment.save(new SaveListener<String>() {

                    @Override
                    public void done(String objectId, BmobException e) {
                        if (e == null) {
                            showToast("评论成功");
                            commentEdit.setText("");
                            ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(PostMessage.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                            comments.add(comment);
                            commentAdapter.notifyDataSetChanged();
                        } else {
                            Log.i("bmob", "失败：" + e.getMessage());
                        }
                    }

                });

                break;
        }
    }

    public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.ViewHolder> {

        List<BmobFile> files;
        Context context;

//    RequestOptions mRequestOptions = RequestOptions.circleCropTransform()
//            .diskCacheStrategy(DiskCacheStrategy.NONE)//不做磁盘缓存
//            .skipMemoryCache(true);//不做内存缓存

        public   class ViewHolder extends RecyclerView.ViewHolder {


            BindImage image1;

            public ViewHolder(View view) {
                super(view);
                image1=view.findViewById(R.id.image);

            }
        }


        public PhotoAdapter(Context context, List<BmobFile> others) {
            super();
            this.context = context;
            this.files = others;
        }

        @Override
        public PhotoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.photo, parent, false);
            PhotoAdapter.ViewHolder holder = new PhotoAdapter.ViewHolder(view);

            holder.setIsRecyclable(true);
            return holder;

        }

        public void onBindViewHolder(final PhotoAdapter.ViewHolder holder, int position) {

            holder.image1.setBmobFile(files.get(position), BindImage.KIND.BIG);
        }


        @Override
        public int getItemCount() {
            if (files==null)
                return 0;
            else return files.size();

        }
    }
}
