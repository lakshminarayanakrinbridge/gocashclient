package com.ecash.app.Adapters;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.ecash.R;
import com.ecash.app.Interfaces.OnItemClickListenerSubCat;
import com.ecash.app.Models.Model_Filter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Inbridge04 on 03-Jan-17.
 */

public class Adapter_RV_SubCategory extends RecyclerView.Adapter<Adapter_RV_SubCategory.MyViewHolder> {

    private ArrayList<Model_Filter> mArrayListSubCategory;
    private ArrayList<String> arrayListCheckedBox = new ArrayList<>();
    private static OnItemClickListenerSubCat mOnItemClickListenerSubCat;

    public Adapter_RV_SubCategory(ArrayList<Model_Filter> arrayListSubCategory) {
        this.mArrayListSubCategory = arrayListSubCategory;
    }

    @Override
    public Adapter_RV_SubCategory.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_item_sub_category, parent, false);
        Adapter_RV_SubCategory.MyViewHolder myViewHolder = new Adapter_RV_SubCategory.MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final Adapter_RV_SubCategory.MyViewHolder holder, final int position) {
        holder.textViewName.setText(mArrayListSubCategory.get(position).getNameSubCat());

       /* holder.textViewName.setTextColor(Color.parseColor("#00aaff"));

        holder.chbx.setChecked(mArrayListSubCategory.get(position).isSelected());
        holder.chbx.setTag(mArrayListSubCategory.get(position));

        holder.chbx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox cb = (CheckBox) v;
                Model_Filter contact = (Model_Filter) cb.getTag();
                contact.setSelected(cb.isChecked());
                mArrayListSubCategory.get(position).setSelected(cb.isChecked());
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return mArrayListSubCategory.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    /*public static class MyViewHolder extends RecyclerView.ViewHolder {*/

        private TextView textViewName;
        private CheckBox chbx;

        public MyViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            this.textViewName = (TextView) itemView.findViewById(R.id.txtView);
            //this.chbx = (CheckBox) itemView.findViewById(R.id.chbx);
        }

        @Override
        public void onClick(View v) {
            mOnItemClickListenerSubCat.onItemClickSubCat(v, getAdapterPosition());
        }
    }

    public void setClickListener(OnItemClickListenerSubCat clicklistener) {
        this.mOnItemClickListenerSubCat = clicklistener;
    }

    // method to access in activity after updating selection
    public List<Model_Filter> getList() {
        return mArrayListSubCategory;
    }
}