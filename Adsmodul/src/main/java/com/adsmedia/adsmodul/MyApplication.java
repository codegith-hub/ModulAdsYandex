package com.adsmedia.adsmodul;


import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.app.Application;
import com.yandex.mobile.ads.common.MobileAds;

public class MyApplication extends Application {
    @SuppressLint("StaticFieldLeak")
    private static OpenAds openAds;

    @Override
    public void onCreate() {
        super.onCreate();
        openAds = new OpenAds(this);
        try {
            MobileAds.initialize(this, () -> {
                // Now you can use ads
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void initSdk(String appid){

    }
}