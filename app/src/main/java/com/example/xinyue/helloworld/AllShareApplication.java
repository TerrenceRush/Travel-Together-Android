package com.example.xinyue.helloworld;

/**
 * Created by xinyue on 5/18/15.
 */

import java.util.HashMap;
import java.util.Map;


import android.app.Application;
import android.content.Context;

import com.example.xinyue.helloworld.util.PreferenceUtils;

import imageCache.AsyncImageLoader;

public class AllShareApplication extends Application {
    public static Context applicationContext;
    private static AllShareApplication mAllShareApplication;
    public static AsyncImageLoader asyncImageLoader;

    @Override
    public void onCreate() {
        super.onCreate();
        applicationContext = this;
        mAllShareApplication = this;
        PreferenceUtils.init(applicationContext);
        asyncImageLoader = new AsyncImageLoader(applicationContext);
    }

    public static AllShareApplication getInstance() {
        return mAllShareApplication;
    }

}
