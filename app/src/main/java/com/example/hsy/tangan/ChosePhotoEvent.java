package com.example.hsy.tangan;

/**
 * Created by hsy on 2018/12/10.
 */

public class ChosePhotoEvent {
    private  int num;

    public ChosePhotoEvent(int i ){
        num =i;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }
}
