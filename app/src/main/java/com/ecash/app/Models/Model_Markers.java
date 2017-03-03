package com.ecash.app.Models;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Inbridge04 on 22-Dec-16.
 */

public class Model_Markers {

    public LatLng latLng;
    public String title;
    public int iconResId;
    public String category;

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getIconResId() {
        return iconResId;
    }

    public void setIconResId(int iconResId) {
        this.iconResId = iconResId;
    }



    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}