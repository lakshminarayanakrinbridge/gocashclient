package com.ecash.app.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ecash.R;
import com.ecash.app.Interfaces.OnItemClickListener;

import java.util.ArrayList;

/**
 * Created by Inbridge04 on 29-Dec-16.
 */

public class Adapter_RV_NearByOffers extends RecyclerView.Adapter<Adapter_RV_NearByOffers.ViewHolder> {

    // Others
    private View view;
    private Context mContext;
    private ViewHolder mViewHolder;
    private static OnItemClickListener mOnItemClickListener;

    // Variables
    private ArrayList<String> mArrayListOffers;

    /*public Adapter_RV_NearByOffers(Context context, ArrayList<String> arrayListOffers, OnItemClickListener onItemClickListener) {
        mContext = context;
        mArrayListOffers = arrayListOffers;
        mOnItemClickListener = onItemClickListener;
    }*/

    public Adapter_RV_NearByOffers(Context context, ArrayList<String> arrayListOffers) {
        mContext = context;
        mArrayListOffers = arrayListOffers;
    }

    @Override
    public Adapter_RV_NearByOffers.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(mContext).inflate(R.layout.rv_item_nearby_offers, parent, false);
        mViewHolder = new ViewHolder(view);
        return mViewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.txt_offer.setText(mArrayListOffers.get(position));
        switch (mArrayListOffers.get(position)) {
            case "Nammoora Thindie":
                holder.img_place_icon.setBackgroundResource(R.drawable.ic_marker_one);
                break;
            case "Reliance Trends":
                holder.img_place_icon.setBackgroundResource(R.drawable.ic_marker_two);
                break;
            case "Star Market":
                holder.img_place_icon.setBackgroundResource(R.drawable.ic_marker_three);
                break;
            case "Nammane Upachar":
                holder.img_place_icon.setBackgroundResource(R.drawable.ic_marker_four);
                break;
            case "New Ram Bhavan Sweets":
                holder.img_place_icon.setBackgroundResource(R.drawable.ic_marker_five);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mArrayListOffers.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView img_place_icon;
        public TextView txt_offer;

        public ViewHolder(View v) {
            super(v);
            v.setOnClickListener(this);
            img_place_icon = (ImageView) v.findViewById(R.id.img_place_icon);
            txt_offer = (TextView) v.findViewById(R.id.txt_offer);
        }

        @Override
        public void onClick(View v) {
            mOnItemClickListener.onItemClick(v, getAdapterPosition());
        }
    }

    public void setClickListener(OnItemClickListener clicklistener) {
        this.mOnItemClickListener = clicklistener;
    }
}