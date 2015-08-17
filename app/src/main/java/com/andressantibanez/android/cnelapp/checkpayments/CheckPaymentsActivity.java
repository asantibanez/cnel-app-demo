package com.andressantibanez.android.cnelapp.checkpayments;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.andressantibanez.android.cnelapp.domain.MeterPayment;
import com.andressantibanez.android.cnelapp.endpoints.CnelService;
import com.andressantibanez.cnelapp.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class CheckPaymentsActivity extends AppCompatActivity {

    public static final String METER_CODE = "meter_code";
    public static final String SEARCH_COMPLETE = "search_complete";
    public static final String METER_PAYMENTS = "meter_payments";

    String mMeterCode;
    boolean mSearchComplete;
    ArrayList<MeterPayment> mMeterPayments;
    Subscription mSubscription;

    @Bind(R.id.progress_bar) ProgressBar mProgressBar;
    @Bind(R.id.payments_list) RecyclerView mRecyclerView;

    public static Intent getLaunchIntent(Context context, String meterCode) {
        Intent activityIntent = new Intent(context, CheckPaymentsActivity.class);
        activityIntent.putExtra(METER_CODE, meterCode);

        return activityIntent;
    }


    /**
     * Lifecycle methods
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_payments);
        ButterKnife.bind(this);

        mMeterCode = getIntent().getStringExtra(METER_CODE);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if(savedInstanceState != null)
            resetState(savedInstanceState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home)
            finish();

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mSubscription != null && !mSubscription.isUnsubscribed())
            mSubscription.unsubscribe();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(!mSearchComplete) {
            CnelService service = new CnelService();
            mSubscription = service.getMeterPayments(mMeterCode)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(getSubscriber());
        }
    }


    /**
     * State handling
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(SEARCH_COMPLETE, mSearchComplete);
        if(mMeterPayments != null) outState.putString(METER_PAYMENTS, new Gson().toJson(mMeterPayments));
    }

    public void resetState(Bundle savedState) {
        mSearchComplete = savedState.getBoolean(SEARCH_COMPLETE, false);
        if(mSearchComplete) {
            String meterPaymentsJson = savedState.getString(METER_PAYMENTS);
            Type meterPaymentsType = new TypeToken<ArrayList<MeterPayment>>(){}.getType();
            ArrayList<MeterPayment> meterPayments = new Gson().fromJson(meterPaymentsJson, meterPaymentsType);
            displayPayments(meterPayments);
        }
    }


    /**
     * Ui methods
     */
    public void displayPayments(ArrayList<MeterPayment> meterPayments) {
        mProgressBar.setVisibility(View.GONE);

        mSearchComplete = true;
        mMeterPayments = meterPayments;

        MeterPaymentsAdapter adapter = new MeterPaymentsAdapter(mMeterPayments);
        mRecyclerView.setAdapter(adapter);
    }

    public void displayError(String errorMessage) {
        Snackbar.make(findViewById(android.R.id.content), errorMessage, Snackbar.LENGTH_LONG).show();
        mProgressBar.setVisibility(View.GONE);
    }


    /**
     * Subscription for results
     */
    private Subscriber<ArrayList<MeterPayment>> getSubscriber() {
        return new Subscriber<ArrayList<MeterPayment>>() {

            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                displayError(e.toString());
            }

            @Override
            public void onNext(ArrayList<MeterPayment> meterPayments) {
                displayPayments(meterPayments);
            }
        };
    }


}
