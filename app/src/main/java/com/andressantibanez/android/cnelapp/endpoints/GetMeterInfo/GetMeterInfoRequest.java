package com.andressantibanez.android.cnelapp.endpoints.GetMeterInfo;

import com.google.gson.annotations.SerializedName;

public class GetMeterInfoRequest {

    @SerializedName("codigo_cuenta")
    String mMeterCode;

    public GetMeterInfoRequest(String meterCode) {
        mMeterCode = meterCode;
    }

}
