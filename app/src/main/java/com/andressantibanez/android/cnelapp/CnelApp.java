package com.andressantibanez.android.cnelapp;

import android.app.Application;

import com.andressantibanez.android.presentermanager.PresenterManager;

public class CnelApp extends Application {

    PresenterManager mPresenterManager;

    private static CnelApp instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        mPresenterManager = new PresenterManager();
    }

    public PresenterManager getPresenterManager() {
        return mPresenterManager;
    }

    public static CnelApp getInstance() {
        return instance;
    }

    public static PresenterManager presenterManager() {
        return getInstance().getPresenterManager();
    }
}
