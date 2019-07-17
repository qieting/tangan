package com.example.hsy.tangan;

import cn.bmob.v3.BmobObject;

/**
 * Created by hsy on 2018/11/7.
 */

public class Comment extends BmobObject {

    private String content;//评论内容

    private String number;//评论的用户，Pointer类型，一对一关系

    private Post post; //所评论的帖子，这里体现的是一对多的关系，一个评论只能属于一个微博

    public Comment() {

    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }


    //自行实现getter和setter方法
}
