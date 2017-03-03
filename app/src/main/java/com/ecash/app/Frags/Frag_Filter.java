package com.ecash.app.Frags;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ecash.R;
import com.ecash.app.Adapters.Adapter_RV_Category;
import com.ecash.app.Interfaces.OnItemClickListenerFilter;
import com.ecash.app.Models.Model_Filter_Category;
import com.ecash.app.Utils.Common_Methods;
import com.ecash.app.Utils.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Inbridge04 on 23-Jan-17.
 */

public class Frag_Filter extends Fragment implements OnItemClickListenerFilter {

    // Others
    private MenuItem menuItemApply, menuItemReset;
    private LinearLayoutManager rvLayoutManagerCat;
    private Adapter_RV_Category adapterRvCategory;

    // Variables
    private String CheckedCheckBoxes = "";
    private String url = "listCategories.php";
    private ArrayList<Model_Filter_Category> arrayListModelCategory;

    // Widgets
    private ProgressDialog progressDialog;
    private RecyclerView rv_category;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frag_filter, container, false);

        find_view_by_id(rootView);
        init();
        fetchCategories();

        return rootView;
    }

    private void find_view_by_id(View view) {
        rv_category = (RecyclerView) view.findViewById(R.id.rv_category);
    }

    private void init() {
        setHasOptionsMenu(true);
        arrayListModelCategory = new ArrayList<>();
    }

    private void fetchCategories() {
        StringRequest stringReq = new StringRequest(Request.Method.POST, Constants.BASE_URL + url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Categories: ", response);
                progressDialog.dismiss();
                if (response != null) {
                    try {
                        JSONObject jo = new JSONObject(response);
                        if (jo.has("success")) {
                            JSONArray jaCategories = jo.getJSONArray("category_list");
                            for (int i = 0; i < jaCategories.length(); i++) {
                                Model_Filter_Category modelFilterCategory = new Model_Filter_Category();
                                modelFilterCategory.setCategory(jaCategories.getString(i));
                                modelFilterCategory.setBgColor(Common_Methods.chooseRandomColor());
                                arrayListModelCategory.add(modelFilterCategory);
                            }

                            // Setting Adapter for Categories
                            //rvLayoutManagerCat = new LinearLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
                            rvLayoutManagerCat = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                            rv_category.setLayoutManager(rvLayoutManagerCat);
                            adapterRvCategory = new Adapter_RV_Category(arrayListModelCategory);
                            rv_category.setAdapter(adapterRvCategory);
                            adapterRvCategory.setClickListener(Frag_Filter.this);

                        } else {
                            Toast.makeText(getActivity(), jo.getString("error_msg"), Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getActivity(), "Server error! Don't get Data from server. Try again later.", Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(getActivity(), "Error:" + error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", "1");
                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(stringReq);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading...");
        progressDialog.show();
    }

    @Override
    public void onItemClickFilter(View view, int pos) {
        menuItemApply.setVisible(true);
        menuItemReset.setVisible(true);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menuItemApply = menu.findItem(R.id.action_apply);
        menuItemReset = menu.findItem(R.id.action_reset);
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_apply) {
            List<Model_Filter_Category> stList = adapterRvCategory.getList();
            for (int i = 0; i < stList.size(); i++) {
                Model_Filter_Category modelFilterCate = stList.get(i);
                if (modelFilterCate.isSelected()) {
                    CheckedCheckBoxes += modelFilterCate.getCategory().toString() + "-";
                }
            }
            replaceFrags(new Frag_NearByOffers(), "Frag_NearByOffers");
            // Toast.makeText(getActivity(), "Selections are: \n" + CheckedCheckBoxes, Toast.LENGTH_LONG).show();
            return true;
        }

        if (item.getItemId() == R.id.action_reset) {
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.detach(this).attach(this).commit();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void replaceFrags(Fragment frag, String tag) {
        FragmentManager fm = getFragmentManager();

        Bundle args = new Bundle();
        args.putString(Constants.KEY_BUNDLE_DATA_PASSED, CheckedCheckBoxes);
        frag.setArguments(args);

        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.frag_container, frag);
        ft.addToBackStack(tag);
        ft.commit();
    }
}