package com.andressantibanez.android.cnelapp.checkmeter;

import com.andressantibanez.android.cnelapp.domain.Meter;
import com.andressantibanez.android.cnelapp.endpoints.CnelService;
import com.andressantibanez.android.cnelapp.exceptions.InvalidMeterCodeException;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SearchMeterInteractor {

    InteractorResult mListener;
    Observable<Meter> mSearchMeterRequest;
    Subscription mSearchMeterSubscription;

    public SearchMeterInteractor(InteractorResult listener) {
        mListener = listener;
    }

    public Subscriber<Meter> getSubscriber() {
        return new Subscriber<Meter>() {
            @Override
            public void onCompleted() {}

            @Override
            public void onError(Throwable e) {
                mListener.onMeterSearchError(e.getMessage());
            }

            @Override
            public void onNext(Meter meter) {
                mListener.onMeterFound(meter);
            }
        };
    }

    public void searchMeter(String meterCode) {
        CnelService cnelService = new CnelService();

        //Observable
        mSearchMeterRequest = cnelService.getMeterInfo(meterCode);

        //Subscriber
        mSearchMeterSubscription = mSearchMeterRequest
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(getSubscriber());
    }

    public boolean isRunning() {
        return mSearchMeterSubscription != null && !mSearchMeterSubscription.isUnsubscribed();
    }

    public void cancel() {
        if(mSearchMeterRequest == null || mSearchMeterSubscription == null)
            return;

        mSearchMeterSubscription.unsubscribe();
    }

    public interface InteractorResult {
        void onMeterFound(Meter meter);
        void onMeterSearchError(String error);
    }


}
