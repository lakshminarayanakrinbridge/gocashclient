package com.ecash.app.Utils;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Color;

import com.ecash.R;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Inbridge04 on 29-Dec-16.
 */

public class Common_Methods {

    private static Common_Methods instance = null;
    private Context mContext;

    public Common_Methods(Context context) {
        this.mContext = context;
    }

    public static Common_Methods getInstance(Context context) {
        if (instance == null) {
            instance = new Common_Methods(context);
        }
        return instance;
    }

    public void replaceFrags(Fragment frag) {
        FragmentManager fm = ((Activity) mContext).getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.frag_container, frag);
        if (frag.getClass().getName().equals("Frag_Home")) {
            ft.addToBackStack(frag.getClass().getName());
        }
        ft.commit();
    }

    public static int chooseRandomColor() {
        ArrayList<String> arrayListColors = new ArrayList<>();
        arrayListColors.add("#F44336");
        arrayListColors.add("#E91E63");
        arrayListColors.add("#9C27B0");
        arrayListColors.add("#673AB7");
        arrayListColors.add("#3F51B5");
        arrayListColors.add("#2196F3");
        arrayListColors.add("#03A9F4");
        arrayListColors.add("#00BCD4");
        arrayListColors.add("#009688");
        arrayListColors.add("#4CAF50");
        arrayListColors.add("#8BC34A");
        arrayListColors.add("#FF9800");
        arrayListColors.add("#FFC107");

        Random random = new Random();
        int colorIndex = random.nextInt(arrayListColors.size());

        return Color.parseColor(arrayListColors.get(colorIndex));
    }
}