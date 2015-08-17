package com.andressantibanez.android.cnelapp.checkpayments;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.andressantibanez.cnelapp.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class CheckPaymentsActivity extends AppCompatActivity {

    public static final String METER_CODE = "meter_code";

    String mMeterCode;


    @Bind(R.id.text) TextView mText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_payments);
        ButterKnife.bind(this);

        mMeterCode = getIntent().getStringExtra(METER_CODE);
    }



}
