package com.example.hsy.tangan;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/10/5.
 */

public class IFragment extends Fragment implements View.OnClickListener {





    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View view = inflater.inflate(R.layout.i_fragment, container, false);

        view.findViewById(R.id.tuichu).setOnClickListener(this);
        view.findViewById(R.id.jiankang).setOnClickListener(this);
        view.findViewById(R.id.qinyou).setOnClickListener(this);

        ((TextView)view.findViewById(R.id.phone)).setText(((App)getActivity().getApplication()).getP().getPhoneNumber());

        return view;

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tuichu:
                Intent intent = new Intent(v.getContext(), LoginActivity.class);
                intent.putExtra("in", 1);

                startActivity(intent);
                getActivity().finish();
                break;

            case R.id.qinyou:
                Intent intent1 = new Intent(v.getContext(),FriendActivity.class);
                startActivity(intent1);

                break;
            case R.id.jiankang:
                Intent intent2 = new Intent(v.getContext(),BaogaoActivity.class);
                startActivity(intent2);

                break;

        }
    }
}