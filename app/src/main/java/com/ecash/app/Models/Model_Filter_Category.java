package com.ecash.app.Models;

/**
 * Created by Inbridge04 on 19-Jan-17.
 */

public class Model_Filter_Category {

    public String category;
    public int bgColor;
    public boolean isSelected;

    public Model_Filter_Category() {
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getBgColor() {
        return bgColor;
    }

    public void setBgColor(int bgColor) {
        this.bgColor = bgColor;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}