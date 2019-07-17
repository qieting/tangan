package com.example.hsy.tangan;


import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;


/**
 * Created by hsy on 2019/3/26.
 */

public interface PersonalProtocol {
    /**
     * 用户信息
     * @param name
     * @param pass
     * @return
     */
    @POST("login")
    @FormUrlEncoded
    Observable<ResponseBody> getPersonalListInfo(@Field("number") String name, @Field("password") String pass);
}