package com.brightcns.testmoudle;

import android.app.Application;

/**
 * @author zhangfeng
 * @data： 27/3/18
 * @description：
 */

public class TestApp extends Application {

    private static TestApp myApplication = null;

    public static TestApp getInstance() {
        return myApplication;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        myApplication = this;
    }
}
