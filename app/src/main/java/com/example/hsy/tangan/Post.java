package com.example.hsy.tangan;


import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobRelation;

/**
 * Created by hsy on 2018/11/7.
 */

public class Post extends BmobObject {

    private String title;//帖子标题

    private String content,location;// 帖子内容

    private String number;//帖子的发布者，这里体现的是一对一的关系，该帖子属于某个用户

    private List<BmobFile> images;//帖子图片



    public String getTitle() {

        return title;

    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }





    public List<BmobFile> getImages() {
        return images;
    }

    public void setImages(List<BmobFile> images) {
        this.images = images;
    }


    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }


    //自行实现getter和setter方法

}