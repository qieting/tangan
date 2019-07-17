package com.example.hsy.tangan;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.hsy.tangan.ui.TitleLayout;

import org.greenrobot.eventbus.EventBus;

public class FoodActivity extends AppCompatActivity {

    RecyclerView recyclerView ;
    FoodAdapter foodAdapter;
    int da=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food);
        da=getIntent().getIntExtra("id",1);
        recyclerView=findViewById(R.id.re);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        foodAdapter=new FoodAdapter(this);
        ((App)getApplication()).setFoods(null);
        recyclerView.setAdapter(foodAdapter);
        TitleLayout titleLayout =findViewById(R.id.sssss);
        titleLayout.changeIs(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((App)getApplication()).setFoods(foodAdapter.re());
                EventBus.getDefault().post(new DDEvent(da));
                finish();

            }
        });
    }
}
