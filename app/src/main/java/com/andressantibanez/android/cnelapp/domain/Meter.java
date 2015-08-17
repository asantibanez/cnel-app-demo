package com.andressantibanez.android.cnelapp.domain;

import com.google.gson.Gson;

public class Meter {

    public String code;
    public String name;
    public String debt;
    public String date;

    public String toJson() {
        return new Gson().toJson(this);
    }

    public static Meter fromJson(String jsonString) {
        Meter meter;

        try {
            meter = new Gson().fromJson(jsonString, Meter.class);
        } catch (Exception e) {
            meter = null;
        }

        return meter;
    }

}
