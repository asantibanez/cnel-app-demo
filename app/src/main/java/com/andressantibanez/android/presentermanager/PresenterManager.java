package com.andressantibanez.android.presentermanager;

import java.util.HashMap;

public class PresenterManager {

    private HashMap<String, Presenter> mPresenters;

    public PresenterManager() {
        mPresenters = new HashMap<>();
    }

    public Presenter get(String key) {
        return mPresenters.get(key);
    }

    public void hold(String key, Presenter presenter) {
        mPresenters.put(key, presenter);
    }

}
