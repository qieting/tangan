package com.example.hsy.tangan;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

public class FriendActivity extends AppCompatActivity {


    People p;


    RecyclerView recyclerView;
    FriendAdapter friendAdapter;
    List<String> f=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend);

        recyclerView = (RecyclerView) findViewById(R.id.i_recycle);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        App app=(App)getApplication();
        String s =app.getP().getFriends();
        if(s!=null&&!s.equals("")){
            String qq[]=s.split(";");
            for (String sss:qq
                    ) {
                f.add(sss);
            }
        }

        friendAdapter =new FriendAdapter(this,f);
        recyclerView.setAdapter(friendAdapter);

    }
}
