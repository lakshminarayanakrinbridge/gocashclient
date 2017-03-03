package com.ecash.app.Frags;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ecash.R;

/**
 * Created by Inbridge04 on 29-Dec-16.
 */

public class Frag_Home extends Fragment implements View.OnClickListener {

    // Widgets
    private LinearLayout btn_nearby;
    private TextView txt_pay_now;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frag_home, container, false);

        find_view_by_id(rootView);
        btn_nearby.setOnClickListener(this);
        txt_pay_now.setOnClickListener(this);

        return rootView;
    }

    private void find_view_by_id(View view) {
        btn_nearby = (LinearLayout) view.findViewById(R.id.btn_nearby);
        txt_pay_now = (TextView) view.findViewById(R.id.txt_pay_now);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_nearby) {
            replaceFrags(new Frag_NearByOffers(), "Frag_NearByOffers");
        }
        if (v.getId() == R.id.txt_pay_now) {
            replaceFrags(new Frag_PayNow(), "Frag_PayNow");
        }
    }

    public void replaceFrags(Fragment frag, String tag) {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.frag_container, frag);
        ft.addToBackStack(tag);
        ft.commit();
    }
}