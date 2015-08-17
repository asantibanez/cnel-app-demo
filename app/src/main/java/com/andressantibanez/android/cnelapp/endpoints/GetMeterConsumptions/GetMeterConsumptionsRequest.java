package com.andressantibanez.android.cnelapp.endpoints.GetMeterConsumptions;

import com.google.gson.annotations.SerializedName;

public class GetMeterConsumptionsRequest {

    @SerializedName("codigo_cuenta")
    String mMeterCode;

    public GetMeterConsumptionsRequest(String meterCode) {
        mMeterCode = meterCode;
    }
}
