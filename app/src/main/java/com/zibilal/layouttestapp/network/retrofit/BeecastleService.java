package com.zibilal.layouttestapp.network.retrofit;

import com.zibilal.layouttestapp.network.retrofit.model.GroupModel;
import com.zibilal.layouttestapp.network.retrofit.model.TokenModel;

import java.util.List;

import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;
import rx.Observable;

/**
 * Created by Bilal on 1/15/2016.
 */
public interface BeecastleService {
    @FormUrlEncoded
    @POST("/token")
    Observable<TokenModel> login(@Field("username") String username, @Field("password") String password, @Field("grant_type") String grantType);

    @GET("/api/Group/Get")
    Observable<List<GroupModel>> getGroup();
}
