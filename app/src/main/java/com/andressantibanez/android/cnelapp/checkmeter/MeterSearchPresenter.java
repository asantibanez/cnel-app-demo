package com.andressantibanez.android.cnelapp.checkmeter;

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

    private void setCurrentMeter(Meter meter) {
        mCurrentMeter = meter;
    }

    public void searchMeter(String meterCode) {
        setCurrentMeter(null);
        RealmEventStore.push(mNotificationId, MeterSearchActivity.SEARCHING_METER);
        mSearchMeterInteractor.searchMeter(meterCode);
    }

    public void cancelSearch() {
        if(mSearchMeterInteractor.isRunning())
            mSearchMeterInteractor.cancel();
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
