package com.ecash.app.Frags;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.ecash.R;
import com.ecash.app.Adapters.CustomAdapter;
import com.ecash.app.Utils.Constants;

import java.util.ArrayList;

/**
 * Created by Inbridge04 on 30-Dec-16.
 */

public class Frag_Individual_Offer extends Fragment {

    // Others
    private CustomAdapter customAdapter;
    private RecyclerView.LayoutManager recylerViewLayoutManager;

    // Variables
    private String selection;
    private ArrayList<String> offers = new ArrayList<>();

    // Widgets
    private ImageView img_place;
    private RecyclerView rv_offers;
    private Button btn_msg, btn_call;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frag_individual_offer, container, false);
        find_view_by_id(rootView);

        Bundle bundle = this.getArguments();

        if (bundle != null) {
            selection = bundle.getString(Constants.KEY_BUNDLE_DATA_PASSED);
        }

        recylerViewLayoutManager = new LinearLayoutManager(getActivity());
        rv_offers.setLayoutManager(recylerViewLayoutManager);

        customAdapter = new CustomAdapter(offers);
        offers.add("flat 50% off on all products");
        offers.add("flat 30% off on Men's products");
        offers.add("flat 90% off on Sarees");
        offers.add("Carry bags are free..rolf");

        setData(selection);
        return rootView;
    }

    private void find_view_by_id(View view) {
        img_place = (ImageView) view.findViewById(R.id.img_place);
        rv_offers = (RecyclerView) view.findViewById(R.id.rv_offers);
        btn_msg = (Button) view.findViewById(R.id.btn_msg);
        btn_call = (Button) view.findViewById(R.id.btn_call);
    }

    private void setData(String position) {
        rv_offers.setAdapter(customAdapter);
        switch (position) {
            case "0":
                img_place.setBackgroundResource(R.drawable.nt);
                break;
            case "1":
                img_place.setBackgroundResource(R.drawable.rt);
                break;
            case "2":
                img_place.setBackgroundResource(R.drawable.sm);
                break;
            case "3":
                img_place.setBackgroundResource(R.drawable.nu);
                break;
            case "4":
                img_place.setBackgroundResource(R.drawable.nrbs);
                break;
        }
    }
}