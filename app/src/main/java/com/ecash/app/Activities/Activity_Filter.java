package com.ecash.app.Activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.ecash.R;
import com.ecash.app.Adapters.Adapter_RV_Category;
import com.ecash.app.Adapters.Adapter_RV_SubCategory;
import com.ecash.app.Models.Model_Filter;

import java.util.ArrayList;

//public class Activity_Filter extends AppCompatActivity implements OnItemClickListener, OnItemClickListenerSubCat {
public class Activity_Filter extends AppCompatActivity {

    // Others
    private LinearLayoutManager rvLayoutManagerCat, rvLayoutManagerSubCat;
    private Adapter_RV_Category adapterRvCategory;
    private Adapter_RV_SubCategory adapterRvSubCategory;

    // Variables
    private ArrayList<String> arrayListCategory;
    private ArrayList<Model_Filter> arrayListModelSubCategory;
    private ArrayList<String> arrayListSubCategoryName;
    private ArrayList<Boolean> arrayListSubCategoryCb;

    // Widgets
    private RecyclerView rv_category, rv_sub_category;
    private Button btn_apply, btn_clear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frag_filter);

        find_view_by_id();
        init();

        /*btn_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // adapterRvSubCategory.notifyDataSetChanged();
                arrayListModelSubCategory.clear();
                addSubCategory();
                adapterRvSubCategory = new Adapter_RV_SubCategory(arrayListModelSubCategory);
                rv_sub_category.setAdapter(adapterRvSubCategory);
            }
        });*/

        /*btn_apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String CheckedCheckBoxes = "";
                List<Model_Filter> stList = ((Adapter_RV_SubCategory) adapterRvSubCategory).getList();

                for (int i = 0; i < stList.size(); i++) {
                    Model_Filter modelFilter = stList.get(i);
                    if (modelFilter.isSelected() == true) {
                        CheckedCheckBoxes = CheckedCheckBoxes + "\n" + modelFilter.getNameSubCat().toString();
                    }
                }

                Toast.makeText(Activity_Filter.this, "Selections are: \n" + CheckedCheckBoxes, Toast.LENGTH_LONG).show();

                Intent intent = new Intent(Activity_Filter.this, MainActivity.class);
                startActivity(intent);

                *//*Bundle bundle = new Bundle();
                bundle.putString("edttext", "From Activity");
                Frag_NearByOffers fragobj = new Frag_NearByOffers();
                fragobj.setArguments(bundle);
                replaceFrags(fragobj);*//*
            }
        });*/
    }

    public void replaceFrags(Fragment frag, String tag) {
        /*FragmentManager fm = ((Activity) mContext).getFragmentManager();*/
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.frag_container, frag, tag);
        /*if (frag.getClass().getName().equals("Frag_Home")) {
            ft.addToBackStack(frag.getClass().getName());
        }*/
        ft.commit();
    }

    private void find_view_by_id() {
        rv_category = (RecyclerView) findViewById(R.id.rv_category);
        //rv_sub_category = (RecyclerView) findViewById(R.id.rv_sub_category);
        /*btn_clear = (Button) findViewById(R.id.btn_clear);
        btn_apply = (Button) findViewById(R.id.btn_apply);*/
    }

    private void init() {
        arrayListCategory = new ArrayList<>();
        arrayListCategory.add("Category");

        arrayListSubCategoryName = new ArrayList<>();
        arrayListSubCategoryName.add("Motels");
        arrayListSubCategoryName.add("Medicals");
        arrayListSubCategoryName.add("Super Markets");

        arrayListSubCategoryCb = new ArrayList<>();
        arrayListSubCategoryCb.add(false);
        arrayListSubCategoryCb.add(false);
        arrayListSubCategoryCb.add(false);

        arrayListModelSubCategory = new ArrayList<>();

        addSubCategory();

        rvLayoutManagerCat = new LinearLayoutManager(this);
        rvLayoutManagerSubCat = new LinearLayoutManager(this);

        rv_category.setLayoutManager(rvLayoutManagerCat);
        //adapterRvCategory = new Adapter_RV_Category(arrayListCategory);
        //adapterRvSubCategory = new Adapter_RV_SubCategory(arrayListModelSubCategory);
        rv_category.setAdapter(adapterRvCategory);

        //adapterRvCategory.setClickListener(this);
    }

    private void addSubCategory() {
        for (int i = 0; i < 3; i++) {
            Model_Filter modelFilter = new Model_Filter();
            modelFilter.setNameSubCat(arrayListSubCategoryName.get(i));
            modelFilter.setSelected(arrayListSubCategoryCb.get(i));
            arrayListModelSubCategory.add(modelFilter);
        }
    }

    /*@Override
    public void onItemClick(View view, int position) {
        *//*rv_sub_category.setVisibility(View.VISIBLE);
        rv_sub_category.setLayoutManager(rvLayoutManagerSubCat);
        adapterRvSubCategory = new Adapter_RV_SubCategory(arrayListModelSubCategory);
        rv_sub_category.setAdapter(adapterRvSubCategory);

        adapterRvSubCategory.setClickListener(this);*//*
    }*/

    /*@Override
    public void onItemClickSubCat(View view, int position) {
        Toast.makeText(this, "Position: " + position, Toast.LENGTH_SHORT).show();
    }*/
}