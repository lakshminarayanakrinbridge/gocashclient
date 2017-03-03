package com.ecash.app.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.ecash.R;
import com.ecash.app.Interfaces.OnItemClickListenerFilter;
import com.ecash.app.Models.Model_Filter_Category;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Inbridge04 on 03-Jan-17.
 */

public class Adapter_RV_Category extends RecyclerView.Adapter<Adapter_RV_Category.MyViewHolder> {

    // private boolean flag;

    private static ArrayList<Model_Filter_Category> mArrayListModelCategory;
    private static OnItemClickListenerFilter mClickListnerFilter;

    /*private Model_Filter_Category model;
    private int ColorFromModel;
    private int Position;*/

    // private static OnItemClickListener mClickListner;
    // private ArrayList<String> mArrayListCategory;
    // private static OnItemClickListener mOnItemClickListener;

    /*public Adapter_RV_Category(ArrayList<String> arrayListCategory) {
        this.mArrayListCategory = arrayListCategory;
    }*/

    public Adapter_RV_Category(ArrayList<Model_Filter_Category> arrayListModelCategory) {
        this.mArrayListModelCategory = arrayListModelCategory;
    }

    @Override
    public Adapter_RV_Category.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_category_item, parent, false);
        Adapter_RV_Category.MyViewHolder myViewHolder = new Adapter_RV_Category.MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final Adapter_RV_Category.MyViewHolder holder, final int listPosition) {

        final int pos = listPosition;

        /*model = mArrayListModelCategory.get(listPosition);
        Position = listPosition;*/

        holder.tvName.setText(mArrayListModelCategory.get(listPosition).getCategory());
        holder.chkSelected.setChecked(mArrayListModelCategory.get(listPosition).isSelected());
        holder.chkSelected.setTag(mArrayListModelCategory.get(listPosition));

        holder.chkSelected.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                CheckBox cb = (CheckBox) v;
                Model_Filter_Category modelFilterCategory = (Model_Filter_Category) cb.getTag();
                modelFilterCategory.setSelected(cb.isChecked());
                mArrayListModelCategory.get(pos).setSelected(cb.isChecked());
                // Toast.makeText(v.getContext(), "Clicked on Checkbox: " + cb.getText() + " is " + cb.isChecked(), Toast.LENGTH_LONG).show();

                mClickListnerFilter.onItemClickFilter(v, listPosition);
            }
        });

        /*holder.textViewName.setText(mArrayListModelCategory.get(listPosition).getCategory());
        holder.txt_name_letter.setText(mArrayListModelCategory.get(listPosition).getCategory().charAt(0) + "");*/
        // holder.cv.setBackgroundColor(mArrayListModelCategory.get(listPosition).getBgColor());

        /*ColorFromModel = mArrayListModelCategory.get(listPosition).getBgColor();
        holder.cv.setBackgroundColor(model.isSelected() ? Color.CYAN : ColorFromModel);*/

        /*holder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                model.setSelected(!model.isSelected());
                holder.cv.setBackgroundColor(model.isSelected() ? Color.CYAN : ColorFromModel);
                mClickListnerFilter.onItemClickFilter(v, Position);
            }
        });*/

        /*holder.cv.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                model.setSelected(!model.isSelected());
                holder.cv.setBackgroundColor(model.isSelected() ? Color.CYAN : ColorFromModel);
                flag = !flag;
                return false;
            }
        });*/

       /* holder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                *//*if (flag) {*//*
                model.setSelected(!model.isSelected());
                holder.cv.setBackgroundColor(model.isSelected() ? Color.CYAN : ColorFromModel);
                *//*}*//*
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return mArrayListModelCategory.size();
    }

    //public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

    /*public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {*/
    public class MyViewHolder extends RecyclerView.ViewHolder {

        /*private CardView cv;
        private TextView textViewName, txt_name_letter;*/

        public TextView tvName;
        public CheckBox chkSelected;

        public MyViewHolder(View itemView) {
            super(itemView);

            tvName = (TextView) itemView.findViewById(R.id.tvName);
            chkSelected = (CheckBox) itemView.findViewById(R.id.chkSelected);

            // itemView.setOnClickListener(this);
            // itemView.setOnLongClickListener(this);

            /*this.textViewName = (TextView) itemView.findViewById(R.id.txtView);
            this.txt_name_letter = (TextView) itemView.findViewById(R.id.txt_name_letter);
            this.cv = (CardView) itemView.findViewById(R.id.cv);*/

            //this.cv.setOnClickListener(this);
        }

        /*@Override
        public void onClick(View v) {
            *//*model.setSelected(!model.isSelected());
            this.cv.setBackgroundColor(model.isSelected() ? Color.CYAN : ColorFromModel);

            mClickListnerFilter.onItemClickFilter(v, getAdapterPosition());*//*
        }*/
    }

    public void setClickListener(OnItemClickListenerFilter clicklistener) {
        this.mClickListnerFilter = clicklistener;
    }

    /*public void setClickListener(OnItemClickListener clicklistener) {
        this.mOnItemClickListener = clicklistener;
    }*/

    public List<Model_Filter_Category> getList() {
        return mArrayListModelCategory;
    }
}