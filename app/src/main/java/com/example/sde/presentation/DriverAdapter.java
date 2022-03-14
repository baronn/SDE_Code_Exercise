package com.example.sde.presentation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sde.R;
import com.example.sde.data.DriversShipment;

import java.util.List;

public class DriverAdapter extends RecyclerView.Adapter<DriverAdapter.ViewHolder> {

    private List<DriversShipment> values;
    private View.OnClickListener onItemClickListener;


    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvDriver;
        public ImageView ivRoute;
        public View layout;

        public ViewHolder(@NonNull View v) {
            super(v);
            layout   = v;
            tvDriver = v.findViewById(R.id.tv_driver_name);
            ivRoute  = v.findViewById(R.id.iv_status);

            v.setTag(this);
            v.setOnClickListener(onItemClickListener);
        }
    }

    public DriverAdapter(List<DriversShipment> values) {
        this.values = values;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.item_driver_list, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvDriver.setText(values.get(position).getDriver());
        if( values.get(position).isRoute() ){
            holder.ivRoute.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return values.size();
    }

    public void setValues(List<DriversShipment> values) {
        this.values = values;
    }

    public void setOnItemClickListener(View.OnClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void updateStatus(Integer position) {
        values.get(position).setRoute(true);
        notifyItemChanged(position);
    }

    public void clear() {
        int size = values.size();
        values.clear();
        notifyItemRangeRemoved(0, size);
    }

}
