package com.example.hsy.tangan;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;


/**
 * Created by Administrator on 2017/10/21.
 */

public class PostFragment extends Fragment {


    PostRecyclerAdapter postsAdapter;
    RecyclerView recyclerView;
    List<Post> posts = new ArrayList<Post>();
    SwipeRefreshLayout swipeRefreshLayout;

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View view = inflater.inflate(R.layout.post_fragment, container, false);
        EventBus.getDefault().register(this);

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_post);

            swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {


                    getPosts();
                    swipeRefreshLayout.setRefreshing(false);
                }
            });

        recyclerView = (RecyclerView) view.findViewById(R.id.posts_recycle);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        postsAdapter = new PostRecyclerAdapter(view.getContext(), posts,((App)getActivity().getApplication()).getP().getPhoneNumber());
        recyclerView.setAdapter(postsAdapter);
        getPosts();
        return view;

    }



    public void getPosts()  {


        BmobQuery<Post> query = new BmobQuery<Post>();
        if(posts!=null&&posts.size()>0) {
            String createdAt = posts.get(0).getCreatedAt();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date createdAtDate = null;
            try {
                createdAtDate = sdf.parse(createdAt);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            BmobDate bmobCreatedAtDate = new BmobDate(createdAtDate);
            query.addWhereGreaterThan("createdAt", bmobCreatedAtDate);
        }
        query.order("-createdAt");
        //query.include("author");// 希望在查询帖子信息的同时也把发布人的信息查询出来
        query.findObjects(new FindListener<Post>() {
            @Override
            public void done(List<Post> object, BmobException e) {
                if (e == null) {
                    if (object != null && object.size() > 0) {



                        if(posts.size()>0&&object.get(object.size()-1).getObjectId().equals(posts.get(0).getObjectId())){
                            object.remove(object.size()-1);
                        }
                        posts.addAll(0,object);


                        postsAdapter.notifyDataSetChanged();


                    } else {

                    }
                }
            }
        });
    }

    @Subscribe
    public void onEventMainThread(ChangeEvent event) {
       getPosts();
    }


}
