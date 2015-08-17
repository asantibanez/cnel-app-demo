package com.andressantibanez.android.cnelapp.endpoints;

import com.andressantibanez.android.cnelapp.domain.Meter;
import com.andressantibanez.android.cnelapp.endpoints.GetMeterInfo.GetMeterInfoRequest;
import com.andressantibanez.android.cnelapp.endpoints.GetMeterInfo.GetMeterInfoResponse;
import com.squareup.okhttp.OkHttpClient;

import java.util.concurrent.TimeUnit;

import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.http.Body;
import retrofit.http.POST;
import rx.Observable;
import rx.functions.Func1;

public class CnelService {

    /**
     * API services definition
     */
    public interface CnelEndpoints {
        @POST("/CellWS/Consultas/consultaSuministro")
        Observable<GetMeterInfoResponse> getMeterInfo(@Body GetMeterInfoRequest request);
    }


    CnelEndpoints mService;

    public CnelService() {
        final OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setReadTimeout(120, TimeUnit.SECONDS);
        okHttpClient.setConnectTimeout(120, TimeUnit.SECONDS);

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("http://190.95.234.107:8087")
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setClient(new OkClient(okHttpClient))
                .build();

        mService = restAdapter.create(CnelEndpoints.class);
    }

    public Observable<Meter> getMeterInfo(String meterCode) {
        GetMeterInfoRequest request = new GetMeterInfoRequest(meterCode);
        return mService.getMeterInfo(request).map(new Func1<GetMeterInfoResponse, Meter>() {
            @Override
            public Meter call(GetMeterInfoResponse getMeterInfoResponse) {
                Meter meter = new Meter();

                meter.code = getMeterInfoResponse.cliente.codigo_cuenta;
                meter.name = getMeterInfoResponse.cliente.nombre;
                meter.debt = getMeterInfoResponse.cliente.deuda;
                meter.date = getMeterInfoResponse.cliente.fecha;

                return meter;
            }
        });
    }

}