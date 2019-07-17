package com.example.hsy.tangan;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hsy on 2019/2/21.
 */

public class FoodChooseAdapter extends RecyclerView.Adapter<FoodChooseAdapter.ViewHolder> {


    Context context;
    List<Food> friends=new ArrayList<>();
    int s[];
PageFragment pageFragment;
//    RequestOptions mRequestOptions = RequestOptions.circleCropTransform()
//            .diskCacheStrategy(DiskCacheStrategy.NONE)//不做磁盘缓存
//            .skipMemoryCache(true);//不做内存缓存

    static class ViewHolder extends RecyclerView.ViewHolder {


        TextView a;
        EditText b;
        RelativeLayout r;

        public ViewHolder(View view) {
            super(view);

            a = view.findViewById(R.id.name);
            b = view.findViewById(R.id.s);
            r=view.findViewById(R.id.e);

        }
    }

   public Food sun(){
        int q=0,w=0,e=0;
        for(int i=0 ;i<s.length;i++){
            q=q+s[i]*friends.get(i).tanglei/100;
            w=w+s[i]*friends.get(i).zhifang/100;
            e=e+s[i]*friends.get(i).danbaizhi/100;
        }
        return new Food("",q,w,e);
   }

    public  FoodChooseAdapter(Context context, List<Food> d,PageFragment pageFragment) {
        super();
        this.context = context;
       friends=d;


        s=new int[friends.size()];
        for(int i=0;i<s.length;i++){
            s[i]=100;
        }
        this.pageFragment=pageFragment;
        pageFragment.s(s.length*100);

    }

    @Override
    public  FoodChooseAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_chose, parent, false);
        FoodChooseAdapter.ViewHolder holder = new  FoodChooseAdapter.ViewHolder(view);


        //  holder.setIsRecyclable(false);
        return holder;

    }




    @Override
    public int getItemCount() {
        return friends.size();
    }

    public  void noty(){
        int j=0;
        for(int i =0 ;i<s.length;i++){
            j=j+s[i];
        }
        pageFragment.s(j);
    }
    public void onBindViewHolder(final  FoodChooseAdapter.ViewHolder holder, final int position) {

        final Food food=friends.get(position);
        holder.a.setText(food.name);
        holder.b.setText(s[position]+"");
        holder.b.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    s[position]= Integer.parseInt(((EditText)v).getText().toString());
                    noty();
                }
            }
        });
        holder.r.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                friends.remove(position);
                int ss[] =new int[friends.size()];
                for(int i=0;i<ss.length;i++){
                  if(i<position){
                      ss[i]=s[i];
                  }else if(i>=position){
                      ss[i]=s[i+1];
                  }

                }
                s=ss;
                noty();
            }
        });
    }


}
