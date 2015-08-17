package com.andressantibanez.android.cnelapp.endpoints.GetMeterPayments;

import com.google.gson.annotations.SerializedName;

public class GetMeterPaymentsRequest {

    @SerializedName("codigo_cuenta")
    String mMeterCode;

    public GetMeterPaymentsRequest(String meterCode) {
        mMeterCode = meterCode;
    }
}
