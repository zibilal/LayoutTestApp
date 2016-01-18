package com.zibilal.layouttestapp.network.retrofit;

import com.zibilal.layouttestapp.model.Item;
import com.zibilal.layouttestapp.model.UsersResponse;

import java.util.List;

import retrofit.RestAdapter;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Bilal on 1/15/2016.
 */
public class SeApiManager {
    private final StackExchangeService mStackExchangeService;

    public SeApiManager() {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("https://api.stackexchange.com")
                .setLogLevel(RestAdapter.LogLevel.BASIC)
                .build();
        mStackExchangeService = restAdapter.create(StackExchangeService.class);
    }

    public Observable<List<Item>> getTenMostPopularSOusers() {
        return mStackExchangeService.getTenMostPopularSOusers()
                .map(UsersResponse::getItems)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<List<Item>> getMostPopularSOusers(int howmany) {
        return mStackExchangeService.getMostPopularSOusers(howmany)
                .map(UsersResponse::getItems)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
