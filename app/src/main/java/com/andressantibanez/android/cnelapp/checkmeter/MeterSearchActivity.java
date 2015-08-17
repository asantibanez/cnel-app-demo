package com.andressantibanez.android.cnelapp.checkmeter;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.andressantibanez.android.cnelapp.CnelApp;
import com.andressantibanez.android.cnelapp.checkconsumption.CheckConsumptionsActivity;
import com.andressantibanez.android.cnelapp.checkpayments.CheckPaymentsActivity;
import com.andressantibanez.android.cnelapp.domain.Meter;
import com.andressantibanez.android.realmeventstore.RealmEventStoreAppCompatActivity;
import com.andressantibanez.cnelapp.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MeterSearchActivity extends RealmEventStoreAppCompatActivity {

    //Constants
    public static final String TAG = MeterSearchActivity.class.getSimpleName();
    public static final String JSON_METER = "json_meter";

    //Events
    public static final String SEARCHING_METER = "searching_meter";
    public static final String SEARCH_SUCCESS = "search_success";
    public static final String SEARCH_ERROR = "search_error";

    //Variables
    private MeterSearchPresenter mPresenter;
    private Meter mMeter;

    //Controls
    @Bind(R.id.progress_bar) ProgressBar mProgressBar;
    @Bind(R.id.meter_code) EditText mMeterCode;
    @Bind(R.id.search_meter) Button mSearchMeter;

    @Bind(R.id.results_container) LinearLayout mResultsContainer;
    @Bind(R.id.name) TextView mName;
    @Bind(R.id.debt) TextView mDebt;
    @Bind(R.id.date) TextView mDate;

    @Bind(R.id.view_payments) Button mViewPayments;
    @Bind(R.id.view_consumptions) Button mViewConsumption;

    /**
     * Lifecycle events
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meter_search);
        ButterKnife.bind(this);

        if(savedInstanceState == null) {
            mPresenter = new MeterSearchPresenter();
            CnelApp.presenterManager().hold(getRealmActivityId(), mPresenter);
        } else {
            mPresenter = (MeterSearchPresenter) CnelApp.presenterManager().get(getRealmActivityId());
            resetState(savedInstanceState);
        }

        mPresenter.setNotificationId(getRealmActivityId());
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(isFinishing())
            mPresenter.cancelSearch();
    }

    /**
     * State handling
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(mMeter != null) outState.putString(JSON_METER, mMeter.toJson());
    }

    public void resetState(Bundle savedState) {
        if(mPresenter.isSearchMeterRunning()) {
            showLoadingIndicator(true);
            enableSearchButton(false);
            return;
        }

        String meterJson = savedState.getString(JSON_METER);
        mMeter = Meter.fromJson(meterJson);
        if(mMeter != null)
            displayMeterInfo();
    }


    /**
     * Event dispatching
     */
    @OnClick(R.id.search_meter)
    public void onSearchMeterClick(View view) {
        String meterCode = mMeterCode.getText().toString();
        mPresenter.searchMeter(meterCode);
    }

    @OnClick(R.id.view_payments)
    public void onViewPaymentsClick(View view) {
        String meterCode = mMeter.code;
        startActivity(CheckPaymentsActivity.getLaunchIntent(this, meterCode));
    }

    @OnClick(R.id.view_consumptions)
    public void onViewConsumptionsClick(View view) {
        String meterCode = mMeter.code;
        startActivity(CheckConsumptionsActivity.getLaunchIntent(this, meterCode));
    }


    /**
     * Ui updating methods
     */
    public void enableSearchButton(boolean enable) {
        mSearchMeter.setEnabled(enable);
    }

    public void showLoadingIndicator(boolean show) {
        int visibility = show ? View.VISIBLE : View.GONE;
        mProgressBar.setVisibility(visibility);
    }

    public void displayErrorMessage(String errorMessage) {
        Snackbar.make(findViewById(android.R.id.content), errorMessage, Snackbar.LENGTH_LONG).show();
    }

    public void showResults(boolean show) {
        int visibility = show ? View.VISIBLE : View.GONE;
        mResultsContainer.setVisibility(visibility);
    }

    public void displayMeterInfo() {
        showResults(true);
        mName.setText(mMeter.name);
        mDebt.setText(mMeter.debt);
        mDate.setText(mMeter.date);
    }


    /**
     * Event handling
     */
    @Override
    public void processEvent(String id, String type, String payload) {
        super.processEvent(id, type, payload);

        if(type.equals(SEARCHING_METER)) {
            showResults(false);
            showLoadingIndicator(true);
            enableSearchButton(false);

            return;
        }

        if(type.equals(SEARCH_SUCCESS)) {
            mMeter = Meter.fromJson(payload);
            displayMeterInfo();
            showLoadingIndicator(false);
            enableSearchButton(true);

            return;
        }

        if(type.equals(SEARCH_ERROR)) {
            displayErrorMessage(payload);
            showLoadingIndicator(false);
            enableSearchButton(true);
        }
    }
}
