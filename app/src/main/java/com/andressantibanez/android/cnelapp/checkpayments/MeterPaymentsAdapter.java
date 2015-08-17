package com.andressantibanez.android.cnelapp.checkpayments;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.andressantibanez.android.cnelapp.domain.MeterPayment;
import com.andressantibanez.cnelapp.R;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MeterPaymentsAdapter extends RecyclerView.Adapter<MeterPaymentsAdapter.ViewHolder> {

    private ArrayList<MeterPayment> mPayments;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.date)
        TextView date;
        @Bind(R.id.amount)
        TextView amount;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public MeterPaymentsAdapter(ArrayList<MeterPayment> payments) {
        mPayments = payments;
    }

    @Override
    public MeterPaymentsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_meter, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        MeterPayment payment = mPayments.get(position);
        viewHolder.date.setText(payment.date);
        viewHolder.amount.setText("$ " + payment.amount);
    }

    @Override
    public int getItemCount() {
        return mPayments.size();
    }
}
