package com.andressantibanez.android.cnelapp;

import com.andressantibanez.android.cnelapp.domain.Meter;
import com.andressantibanez.android.presentermanager.Presenter;
import com.andressantibanez.android.realmeventstore.RealmEventStore;

public class MeterSearchPresenter implements Presenter, SearchMeterInteractor.InteractorResult {

    public static final String TAG = MeterSearchPresenter.class.getSimpleName();

    String mNotificationId;
    Meter mCurrentMeter;

    SearchMeterInteractor mSearchMeterInteractor;

    public MeterSearchPresenter() {
        mSearchMeterInteractor = new SearchMeterInteractor(this);
    }

    public void setNotificationId(String notificationId) {
        mNotificationId = notificationId;
    }

    public boolean isSearchMeterRunning() {
        return mSearchMeterInteractor.isRunning();
    }

    /*
    public void setView(MeterSearchView view) {
        mView = view;

        if(mSearchMeterInteractor.isRunning()) {
            mView.showResults(false);
            mView.showLoadingIndicator(true);
            mView.enableSearchButton(false);
        }
    }

    public void onPause() {
        mSearchMeterInteractor.setListener(null);
    }

    public void onResume() {
        mSearchMeterInteractor.setListener(this);
    }
    */

    private void setCurrentMeter(Meter meter) {
        mCurrentMeter = meter;
    }

    public void searchMeter(String meterCode) {
        setCurrentMeter(null);
        RealmEventStore.push(mNotificationId, MeterSearchActivity.SEARCHING_METER);
        mSearchMeterInteractor.searchMeter(meterCode);
    }

    @Override
    public void onMeterFound(Meter meter) {
        setCurrentMeter(meter);
        RealmEventStore.push(mNotificationId, MeterSearchActivity.SEARCH_SUCCESS, meter.toJson());
    }

    @Override
    public void onMeterSearchError(String error) {
        RealmEventStore.push(mNotificationId, MeterSearchActivity.SEARCH_ERROR, error);
    }

}
