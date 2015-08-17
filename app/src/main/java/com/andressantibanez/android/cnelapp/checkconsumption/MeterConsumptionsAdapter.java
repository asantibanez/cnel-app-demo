package com.andressantibanez.android.cnelapp.checkconsumption;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.andressantibanez.android.cnelapp.domain.MeterConsumption;
import com.andressantibanez.cnelapp.R;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MeterConsumptionsAdapter extends RecyclerView.Adapter<MeterConsumptionsAdapter.ViewHolder> {

    private ArrayList<MeterConsumption> mConsumptions;

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

    public MeterConsumptionsAdapter(ArrayList<MeterConsumption> consumptions) {
        mConsumptions = consumptions;
    }

    @Override
    public MeterConsumptionsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_meter, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        MeterConsumption consumption = mConsumptions.get(position);
        viewHolder.date.setText(consumption.date);
        viewHolder.amount.setText(consumption.amount + " kw");
    }

    @Override
    public int getItemCount() {
        return mConsumptions.size();
    }
}
