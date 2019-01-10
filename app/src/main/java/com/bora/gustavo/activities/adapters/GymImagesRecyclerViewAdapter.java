package com.bora.gustavo.activities.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bora.gustavo.R;

public class GymImagesRecyclerViewAdapter extends RecyclerView.Adapter<GymImagesRecyclerViewAdapter.ViewHolder> {
    private String[] mData;
    private LayoutInflater mInflater;

    public GymImagesRecyclerViewAdapter(Context context, String[] data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.gym_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.equipmentTextView.setText(mData[position]);
    }

    @Override
    public int getItemCount() {
        return mData.length;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView equipmentTextView;

        ViewHolder(View itemView) {
            super(itemView);
            equipmentTextView = itemView.findViewById(R.id.gym_item_equipment);
        }
    }

    public String getItem(int id) {
        return mData[id];
    }
}

