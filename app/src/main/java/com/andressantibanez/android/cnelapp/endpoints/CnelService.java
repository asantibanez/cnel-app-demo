package com.andressantibanez.android.cnelapp.endpoints;

import com.andressantibanez.android.cnelapp.domain.Meter;
import com.andressantibanez.android.cnelapp.domain.MeterConsumption;
import com.andressantibanez.android.cnelapp.domain.MeterPayment;
import com.andressantibanez.android.cnelapp.endpoints.GetMeterConsumptions.GetMeterConsumptionsRequest;
import com.andressantibanez.android.cnelapp.endpoints.GetMeterConsumptions.GetMeterConsumptionsResponse;
import com.andressantibanez.android.cnelapp.endpoints.GetMeterInfo.GetMeterInfoRequest;
import com.andressantibanez.android.cnelapp.endpoints.GetMeterInfo.GetMeterInfoResponse;
import com.andressantibanez.android.cnelapp.endpoints.GetMeterPayments.GetMeterPaymentsRequest;
import com.andressantibanez.android.cnelapp.endpoints.GetMeterPayments.GetMeterPaymentsResponse;
import com.andressantibanez.android.cnelapp.exceptions.InvalidMeterCodeException;
import com.squareup.okhttp.OkHttpClient;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.http.Body;
import retrofit.http.POST;
import rx.Observable;
import rx.exceptions.OnErrorThrowable;
import rx.functions.Action1;
import rx.functions.Func1;

public class CnelService {

    /**
     * API services definition
     */
    public interface CnelEndpoints {
        @POST("/CellWS/Consultas/consultaSuministro")
        Observable<GetMeterInfoResponse> getMeterInfo(@Body GetMeterInfoRequest request);

        @POST("/CellWS/Consultas/consultaPago")
        Observable<GetMeterPaymentsResponse> getMeterPayments(@Body GetMeterPaymentsRequest request);

        @POST("/CellWS/Consultas/consultaConsumo")
        Observable<GetMeterConsumptionsResponse> getMeterConsumptions(@Body GetMeterConsumptionsRequest request);
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
        return mService.getMeterInfo(request)
                .doOnNext(new Action1<GetMeterInfoResponse>() {

                    @Override
                    public void call(GetMeterInfoResponse getMeterInfoResponse) {
                        if (getMeterInfoResponse.codigoResultado.equals("99"))
                            throw new RuntimeException(getMeterInfoResponse.mensajeResultado);
                    }

                }).map(new Func1<GetMeterInfoResponse, Meter>() {

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

    public Observable<ArrayList<MeterPayment>> getMeterPayments(String meterCode) {
        GetMeterPaymentsRequest request = new GetMeterPaymentsRequest(meterCode);
        return mService.getMeterPayments(request).map(new Func1<GetMeterPaymentsResponse, ArrayList<MeterPayment>>() {
            @Override
            public ArrayList<MeterPayment> call(GetMeterPaymentsResponse getMeterPaymentsResponse) {

                ArrayList<MeterPayment> payments = new ArrayList<>();

                for (GetMeterPaymentsResponse.Pago pago : getMeterPaymentsResponse.listapago) {
                    MeterPayment payment = new MeterPayment();
                    payment.meterCode = pago.codigo_cuenta;
                    payment.amount = pago.pago;
                    payment.date = pago.fecha;
                    payments.add(payment);
                }

                return payments;
            }
        });
    }

    public Observable<ArrayList<MeterConsumption>> getMeterConsumptions(String meterCode) {
        GetMeterConsumptionsRequest request = new GetMeterConsumptionsRequest(meterCode);
        return mService.getMeterConsumptions(request).map(new Func1<GetMeterConsumptionsResponse, ArrayList<MeterConsumption>>() {
            @Override
            public ArrayList<MeterConsumption> call(GetMeterConsumptionsResponse getMeterConsumptionsResponse) {

                ArrayList<MeterConsumption> consumptions = new ArrayList<>();

                for (GetMeterConsumptionsResponse.Consumo consumo : getMeterConsumptionsResponse.listaconsumo) {
                    MeterConsumption consumption = new MeterConsumption();
                    consumption.meterCode = consumo.codigo_cuenta;
                    consumption.amount = consumo.consumo;
                    consumption.date = consumo.fecha;
                    consumptions.add(consumption);
                }

                return consumptions;
            }
        });
    }

}