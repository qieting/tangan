package com.example.hsy.tangan.ui;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.v7.widget.PopupMenu;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.example.hsy.tangan.FriendsDialog;
import com.example.hsy.tangan.R;


/**
 * Created by Administrator on 2017/9/16.
 */

public class TitleLayout extends LinearLayout implements View.OnClickListener {
    TextView title;
    ImageButton back, plus;

    Button is;
    Context context;
    int i = 1;

    public TitleLayout(Context context, @Nullable AttributeSet attrs) {

        this(context, attrs, 0);

    }

    public TitleLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        /**
         * 获取我们自定义的属性
         */
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.title_layout, this);
        title = (TextView) findViewById(R.id.title);
        back = (ImageButton) findViewById(R.id.back_title);
        plus = (ImageButton) findViewById(R.id.plus_title);
        is = findViewById(R.id.is_title);
        back.setOnClickListener(this);
        plus.setOnClickListener(this);
        backGone();
        isGone();
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.TitleLayout, defStyleAttr, 0);
        int arrayCount = array.getIndexCount();
        for (int i = 0; i < arrayCount; i++) {
            int index = array.getIndex(i);
            switch (index) {
                case R.styleable.TitleLayout_title:
                    //getColor(int index，int defaultValue)
                    title.setText(array.getText(R.styleable.TitleLayout_title));
                    break;
                case R.styleable.TitleLayout_back:
                    if (array.getBoolean(R.styleable.TitleLayout_back, false)) {
                        back.setVisibility(VISIBLE);
                    } else {
                        back.setVisibility(GONE);
                    }
                    break;
                case R.styleable.TitleLayout_plus:
                    if (!array.getBoolean(R.styleable.TitleLayout_plus, false)) {
                        plus.setVisibility(GONE);
                    }
            }
        }
        array.recycle();
    }


    public void setTitle(String s) {
        title.setText(s);
    }

    public void backGone() {
        back.setVisibility(GONE);
    }

    public void backAdd() {
        back.setVisibility(VISIBLE);
    }


    public void plusGone() {
        plus.setVisibility(GONE);
    }

    public void setPlus(int i) {
        plus.setVisibility(VISIBLE);

        is.setVisibility(GONE);
        this.i = i;

    }

    public void isGone() {
        is.setVisibility(INVISIBLE);
    }

    public void changeIs(OnClickListener onClickListener) {
        plusGone();
        is.setVisibility(VISIBLE);
        is.setOnClickListener(onClickListener);

    }

    public void changeIsName(String name) {

        is.setVisibility(VISIBLE);
        is.setText(name);

    }

    private Activity getActivity() {
        // Gross way of unwrapping the Activity so we can get the FragmentManager
        while (context instanceof ContextWrapper) {
            if (context instanceof Activity) {
                return (Activity)context;
            }
            context = ((ContextWrapper)context).getBaseContext();
        }
        throw new IllegalStateException("The MediaRouteButton's Context is not an Activity.");
    }

    @Override
    public void onClick(final View v) {
        switch (v.getId()) {
            case R.id.back_title:
                getActivity().finish();
                break;
            case R.id.plus_title:


                PopupMenu popup = new PopupMenu(getContext(), plus);

                //Inflating the Popup using xml file
                if (i == 1) {
                    popup.getMenuInflater()
                            .inflate(R.menu.plus_menu, popup.getMenu());
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        public boolean onMenuItemClick(MenuItem item) {

                            new FriendsDialog(context).show();

                            return true;
                        }
                    });
                } else {
                    popup.getMenuInflater()
                            .inflate(R.menu.addluntan, popup.getMenu());
                    //registering popup with OnMenuItemClickListener
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        public boolean onMenuItemClick(MenuItem item) {

                            Intent intent = new Intent(context, AddPostActivity.class);
                            context.startActivity(intent);

                            return true;
                        }
                    });

                }
                popup.show();
                break;


        }

    }
}
