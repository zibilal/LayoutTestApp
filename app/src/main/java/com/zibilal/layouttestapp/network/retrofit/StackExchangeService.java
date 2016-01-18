package com.zibilal.layouttestapp.network.retrofit;

import com.zibilal.layouttestapp.model.UsersResponse;

import retrofit.http.GET;
import retrofit.http.Query;
import rx.Observable;

/**
 * Created by Bilal on 1/15/2016.
 */
public interface StackExchangeService {
    @GET("/2.2/users?order=desc&sort=reputation&site=stackoverflow")
    Observable<UsersResponse> getMostPopularSOusers(@Query("pagesize") int howmany);

    @GET("/2.2/users?order=desc&sort=reputation&site=stackoverflow&pagesize=10")
    Observable<UsersResponse> getTenMostPopularSOusers();
}
