package com.example.hsy.tangan;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

/**
 * Created by hsy on 2019/2/13.
 */

public class TanganFragment extends Fragment implements View.OnClickListener {



    TextView xue, yun, yin;
    RelativeLayout addxue, addyun, addyin;

    App app;
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View view = inflater.inflate(R.layout.tangan, container, false);
        app=(App)getActivity().getApplication();
        xue = view.findViewById(R.id.xue2);
        yin = view.findViewById(R.id.yin2);
        yun = view.findViewById(R.id.yun2);


        addyun = view.findViewById(R.id.addyun);
        addyin = view.findViewById(R.id.addyin);
        addxue = view.findViewById(R.id.addxue);
        addyun.setOnClickListener(this);
        addyin.setOnClickListener(this);
        addxue.setOnClickListener(this);
        xue.setText("?");
        yin.setText("?");
        yun.setText("?");


re();

TextView textView=view.findViewById(R.id.book1);
textView.setText(app.getBook(0).getTitle());
textView.setOnClickListener(this);
        textView=view.findViewById(R.id.book2);
        textView.setText(app.getBook(1).getTitle());
        textView.setOnClickListener(this);

        return view;

    }

    public void re(){


        List<Tangan> tangan = ((App) getActivity().getApplication()).getTangans();
        if (tangan.size() > 0) {
            Tangan tangan1 = new Tangan();
            Tangan s = tangan.get(tangan.size() - 1);
            if (tangan1.getA().equals(s.getA())) {

                if (s.getYundong() > 0) {
                    yun.setText(s.getYundong() + "");
                }
                if (s.getXuetang() > 0) {
                    xue.setText(s.getXuetang() + "");
                }else
                if(tangan.size()>1){
                    s = tangan.get(tangan.size() - 2);
                    if (tangan1.getA().equals(s.getA())) {

                        if (s.getXuetang() > 0) {
                            xue.setText(s.getXuetang() + "");
                        }else
                        if(tangan.size()>2){
                            s = tangan.get(tangan.size() - 3);
                            if (tangan1.getA().equals(s.getA())) {

                                if (s.getXuetang()> 0) {
                                    xue.setText(s.getXuetang() + "");
                                }

                            }
                        }

                    }
                }
                if (s.getTanglei() > 0) {
                    yin.setText(s.getTanglei() + s.getDanbaizhi() + s.getZhifang() + "");
                }else
                if(tangan.size()>1){
                    s = tangan.get(tangan.size() - 2);
                    if (tangan1.getA().equals(s.getA())) {

                        if (s.getTanglei() > 0) {
                            yin.setText(s.getTanglei() + s.getDanbaizhi() + s.getZhifang() + "");
                        }else
                        if(tangan.size()>2){
                            s = tangan.get(tangan.size() - 3);
                            if (tangan1.getA().equals(s.getA())) {

                                if (s.getTanglei() > 0) {
                                    yin.setText(s.getTanglei() + s.getDanbaizhi() + s.getZhifang() + "");
                                }

                            }
                        }

                    }
                }
            }

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addyin:
                Intent w =new Intent(v.getContext(),AddFood.class);
                startActivity(w);
                break;
            case R.id.addxue:
                new TanganDialog(v.getContext()).show();
                break;
            case R.id.addyun:
                Intent i =new Intent(v.getContext(),AddYunActivity.class);
                startActivity(i);
                break;


            case R.id.book1:
                Intent b =new Intent(v.getContext(),BookActivity.class);
                b.putExtra("id",0);
                startActivity(b);
                break;
            case R.id.book2:
                Intent bb =new Intent(v.getContext(),BookActivity.class);
                bb.putExtra("id",1);
                startActivity(bb);
                break;
            default:
                break;
        }
    }


    @Subscribe
    public void onEventMainThread(TEvent event) {

    }

}
