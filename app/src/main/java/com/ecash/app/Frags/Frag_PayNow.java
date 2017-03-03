package com.ecash.app.Frags;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ecash.R;

/**
 * Created by Inbridge04 on 29-Dec-16.
 */

public class Frag_PayNow extends Fragment {

    // Widgets
    private ImageView img_back_arrow;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frag_pay_now, container, false);

        find_view_by_id(rootView);
        img_back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });
        return rootView;
    }

    private void find_view_by_id(View view) {
        img_back_arrow = (ImageView) view.findViewById(R.id.img_back_arrow);
    }
}