package com.zibilal.layouttestapp.network.retrofit;

import com.zibilal.layouttestapp.network.retrofit.model.TokenModel;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Bilal on 1/15/2016.
 */
public class BeecastleApiManager {
    private final BeecastleService mBeecastleService;

    public BeecastleApiManager() {
        mBeecastleService = ServiceGenerator.createService(BeecastleService.class);
    }

    public Observable<TokenModel> login(String username, String password) {
        return mBeecastleService.login(username, password, "password")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
