package com.zibilal.layouttestapp.network.retrofit.model;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Bilal on 1/18/2016.
 */
public class Preferences {

    private static Preferences _instance;

    private SharedPreferences mPreferences;

    private static final String APP_PREFERENCES="beecastel_preferences";
    private static final String ACCESS_TOKEN="access token";
    private static final String USER_NAME="user name";
    private static final String TOKEN_TYPE="token type";
    private static final String EXPIRES_IN="expires in";
    private static final String ISSUED="issued";
    private static final String EXPIRES="expires";

    private Preferences(Context context) {
        mPreferences = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
    }

    public static void init(Context context) {
        if (_instance == null) {
            _instance = new Preferences(context);
        }
    }

    public static Preferences getInstance() throws Exception {
        if (_instance == null)
            throw new Exception("Preferences has not been initialized.");
        return _instance;
    }

    public void setTokenObject(TokenModel token) {
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putString(TOKEN_TYPE, token.getTokenType());
        editor.putString(ACCESS_TOKEN, token.getAccessToken());
        editor.putLong(EXPIRES_IN, token.getExpiresIn());
        editor.putString(EXPIRES, token.getExpires());
        editor.putString(ISSUED, token.getIssued());
        editor.apply();
    }

    public TokenModel getTokenObject() throws Exception{
        if (mPreferences.getString(ACCESS_TOKEN, null) == null) {
            throw new Exception("Exception is occured: " + "Acces token has not been saved before");
        }
        TokenModel model = new TokenModel();
        model.setAccessToken(mPreferences.getString(ACCESS_TOKEN, null));
        model.setTokenType(mPreferences.getString(TOKEN_TYPE, null));
        model.setExpires(mPreferences.getString(EXPIRES, null));
        model.setExpiresIn(mPreferences.getLong(EXPIRES_IN, 0l));
        model.setIssued(mPreferences.getString(USER_NAME, null));
        return model;
    }
}
