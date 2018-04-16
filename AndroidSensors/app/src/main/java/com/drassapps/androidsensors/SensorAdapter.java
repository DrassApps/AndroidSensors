package com.drassapps.androidsensors;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Andres on 7/4/18.
 */

public class SensorAdapter extends RecyclerView.Adapter<SensorAdapter.ViewHolder> {

    private ArrayList<String> sensorList;
    private Context mCtx;

    public SensorAdapter(Context context, ArrayList<String> sensorList) {
        this.mCtx = context;
        this.sensorList = sensorList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView sensorName;

        private ViewHolder(View view) {
            super(view);
            sensorName = (TextView) view.findViewById(R.id.list_sensorName);
        }
    }

    @Override
    public SensorAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {

        viewHolder.sensorName.setText(sensorList.get(position));
    }


    @Override
    public int getItemCount() {
        return sensorList.size();
    }
}

