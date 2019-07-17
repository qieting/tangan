package com.example.hsy.tangan;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

/**
 * Created by hsy on 2019/2/26.
 */

public class TuFragment extends Fragment  {


    private SimpleFragmentPagerAdapter pagerAdapter;
    private ViewPager viewPager;
    private TabLayout tabLayout;



    App app;
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View view = inflater.inflate(R.layout.tufragment, container, false);
        app=(App)getActivity().getApplication();

        pagerAdapter = new SimpleFragmentPagerAdapter(((FragmentActivity)getActivity()).getSupportFragmentManager(), view.getContext(),1);
        viewPager = (ViewPager) view.findViewById(R.id.tuviewpager);
        tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);


        return view;

    }






}
