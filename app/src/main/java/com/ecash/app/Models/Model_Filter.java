package com.ecash.app.Models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Inbridge04 on 03-Jan-17.
 */

public class Model_Filter implements Parcelable {

    private boolean isSelected;
    private String nameSubCat;

    public Model_Filter() {
    }

    public Model_Filter(boolean isSelected, String nameSubCat) {
        this.isSelected = isSelected;
        this.nameSubCat = nameSubCat;
    }

    protected Model_Filter(Parcel in) {
        isSelected = in.readByte() != 0;
        nameSubCat = in.readString();
    }

    public static final Creator<Model_Filter> CREATOR = new Creator<Model_Filter>() {
        @Override
        public Model_Filter createFromParcel(Parcel in) {
            return new Model_Filter(in);
        }

        @Override
        public Model_Filter[] newArray(int size) {
            return new Model_Filter[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeByte((byte) (isSelected ? 1 : 0));
        dest.writeString(nameSubCat);
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getNameSubCat() {
        return nameSubCat;
    }

    public void setNameSubCat(String nameSubCat) {
        this.nameSubCat = nameSubCat;
    }
}