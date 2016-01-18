package com.zibilal.layouttestapp.network.retrofit;

import com.zibilal.layouttestapp.network.retrofit.model.GroupModel;
import com.zibilal.layouttestapp.network.retrofit.model.TokenModel;

import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Bilal on 1/18/2016.
 */
public class BeecastleRestrictedApiManager {
    private final BeecastleService mBeecastleService;

    public BeecastleRestrictedApiManager(TokenModel tokenModel) {
        String auth = "bearer " + tokenModel.getAccessToken();
        mBeecastleService = ServiceGenerator.createService(BeecastleService.class, auth);
    }

    public Observable<List<GroupModel>> getGroup() {
        return mBeecastleService.getGroup()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
