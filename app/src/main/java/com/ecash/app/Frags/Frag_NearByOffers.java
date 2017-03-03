package com.ecash.app.Frags;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.Menu;
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
import com.ecash.app.Adapters.Adapter_RV_NearByOffers;
import com.ecash.app.Interfaces.OnItemClickListener;
import com.ecash.app.Models.Model_Markers;
import com.ecash.app.Utils.Constants;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Inbridge04 on 29-Dec-16.
 */

public class Frag_NearByOffers extends Fragment implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener, OnItemClickListener {

    // Variables
    private FloatingActionButton fab;
    private Adapter_RV_NearByOffers recyclerViewAdapter;
    private RecyclerView.LayoutManager recylerViewLayoutManager;
    private LocationRequest mLocationRequest;
    private GoogleApiClient mGoogleApiClient;
    private BottomSheetBehavior bottomSheetBehavior;
    private Location mLastLocation;

    private String url = "http://3dedn.com/E-Cash/app/listOffers.php";
    private String currLat, currLng, SelectedCategory = "";
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    private ArrayList<Model_Markers> arrayListMarkers;
    private ArrayList<LatLng> arrayListLatLngs;
    private ArrayList<String> arrayListCategory;
    private ArrayList<String> arrayListOffers;
    private ArrayList<Integer> arrayListIconResId;
    private ArrayList<String> arrayListSelectedCategory;

    private final static LatLng LL_NT = new LatLng(12.960620, 77.512415); // Nammoora Thindi
    private final static LatLng LL_RT = new LatLng(12.961189, 77.509888); // Reliance Trends
    private final static LatLng LL_SM = new LatLng(12.961219, 77.510639); // Star Market - A TATA and TESCO Enterprise
    private final static LatLng LL_NU = new LatLng(12.960916, 77.511395); // Nammane Upachar
    private final static LatLng LL_RBS = new LatLng(12.960843, 77.511867); // New Ram Bhavan Sweets

    // Widgets
    private ProgressDialog progressDialog;
    private View bottomSheet;
    private static View view;
    private RecyclerView recyclerView;
    private GoogleMap mGoogleMap;
    private MapFragment mapFrag;
    private Marker mCurrLocationMarker;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // View rootView = inflater.inflate(R.layout.frag_nearby_offers, container, false);

        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        }

        try {
            view = inflater.inflate(R.layout.frag_nearby_offers, container, false);
        } catch (InflateException e) {
        /* map is already there, just return view as it is */
        }

        find_view_by_id(view);
        arrayListSelectedCategory = new ArrayList<>();
        init();
        setupBottomSheet();

        recylerViewLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(recylerViewLayoutManager);
        /*recyclerViewAdapter = new Adapter_RV_NearByOffers(getContext(), arrayListOffers, this);*/
        recyclerViewAdapter = new Adapter_RV_NearByOffers(getActivity(), arrayListOffers);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerViewAdapter.setClickListener(this);

        setHasOptionsMenu(true);

        return view;
    }

    private void find_view_by_id(View view) {
        mapFrag = (MapFragment) getFragmentManager().findFragmentById(R.id.map_google);
        recyclerView = (RecyclerView) view.findViewById(R.id.rv_nearby_offers);
        bottomSheet = view.findViewById(R.id.bottom_sheet);
        fab = (FloatingActionButton) view.findViewById(R.id.btn_fab);
    }

    private void init() {
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            SelectedCategory = null;
            SelectedCategory = getArguments().getString(Constants.KEY_BUNDLE_DATA_PASSED);

            SelectedCategory.substring(0, SelectedCategory.length() - 1);

            String[] hmm = SelectedCategory.split("-");
            for (int i = 0; i < hmm.length; i++) {
                arrayListSelectedCategory.add(hmm[i]);
            }
        }

        /*Bundle data = getArguments();
        if (data != null) {
            arrayListSelectedCategory = getArguments().getStringArrayList("list");
        }*/

        mapFrag.getMapAsync(this);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);

        arrayListMarkers = new ArrayList<Model_Markers>();
        arrayListLatLngs = new ArrayList<LatLng>();
        arrayListCategory = new ArrayList<String>();
        arrayListOffers = new ArrayList<String>();
        arrayListIconResId = new ArrayList<Integer>();

        arrayListLatLngs.add(LL_NT);
        arrayListLatLngs.add(LL_RT);
        arrayListLatLngs.add(LL_SM);
        arrayListLatLngs.add(LL_NU);
        arrayListLatLngs.add(LL_RBS);

        /*arrayListOffers.add("Nammoora Thindi");
        arrayListOffers.add("Reliance Trends");
        arrayListOffers.add("Star Market");
        arrayListOffers.add("Nammane Upachar");
        arrayListOffers.add("New Ram Bhavan Sweets");*/

        arrayListIconResId.add(R.drawable.ic_marker_one);
        arrayListIconResId.add(R.drawable.ic_marker_two);
        arrayListIconResId.add(R.drawable.ic_marker_three);
        arrayListIconResId.add(R.drawable.ic_marker_four);
        arrayListIconResId.add(R.drawable.ic_marker_five);

        arrayListCategory.add("Motels");
        arrayListCategory.add("Medicals");
        arrayListCategory.add("Super Markets");
        arrayListCategory.add("Clothings");
        arrayListCategory.add("Bars");

        // Setting Model Marker Data
        /*for (int i = 0; i < 5; i++) {
            Model_Markers modelMarkersObj = new Model_Markers();
            modelMarkersObj.setLatLng(arrayListLatLngs.get(i));
            modelMarkersObj.setTitle(arrayListOffers.get(i));
            modelMarkersObj.setIconResId(arrayListIconResId.get(i));
            modelMarkersObj.setCategory(arrayListCategory.get(i));
            arrayListMarkers.add(modelMarkersObj);
        }*/
    }

    private void setupBottomSheet() {
        // Handling movement of bottom sheets from buttons
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    fab.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.quantum_ic_keyboard_arrow_down_white_36));
                } else if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    fab.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_arrow_up));
                }
            }
        });

        // Handling movement of bottom sheets from sliding
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_COLLAPSED) {

                } else if (newState == BottomSheetBehavior.STATE_EXPANDED) {

                }
            }

            @Override
            public void onSlide(View bottomSheet, float slideOffset) {
                // bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });
    }

    private void fetchData() {
        StringRequest stringReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("res:", response.toString());
                progressDialog.dismiss();
                if (response != null) {
                    try {
                        JSONObject jo = new JSONObject(response);
                        if (jo.has("success")) {

                            JSONArray jsonArrayOffers_list = jo.getJSONArray("offers_list");

                            for (int i = 0; i < jsonArrayOffers_list.length(); i++) {
                                JSONObject jsonObject = jsonArrayOffers_list.getJSONObject(i);
                                String lat = jsonObject.getString("lat");
                                String lng = jsonObject.getString("lng");
                                String name = jsonObject.getString("name");
                                String category=jsonObject.getString("category");
                                // String offer = jsonObject.getString("offer");

                                Model_Markers model_markers = new Model_Markers();
                                model_markers.setCategory(category);
                                model_markers.setLatLng(new LatLng(Double.parseDouble(lat), Double.parseDouble(lng)));
                                model_markers.setTitle(name);

                                switch (i) {
                                    case 0:
                                        model_markers.setIconResId(R.drawable.ic_marker_one);
                                        break;
                                    case 1:
                                        model_markers.setIconResId(R.drawable.ic_marker_two);
                                        break;
                                    case 2:
                                        model_markers.setIconResId(R.drawable.ic_marker_three);
                                        break;
                                    case 3:
                                        model_markers.setIconResId(R.drawable.ic_marker_four);
                                        break;
                                }
                                arrayListMarkers.add(model_markers);
                            }
                            try {
                                addMarkersToMap();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(getActivity(), jo.getString("error_msg"), Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getActivity(), "Server error! Server sent empty data!! Try again later.", Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //progressDialog.dismiss();
                progressDialog.dismiss();
                //Log.e("Error: ", error.getMessage());
                Toast.makeText(getActivity(), "Error:" + error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", "1");
                params.put("lat", currLat.trim());
                params.put("lng", currLng.trim());
                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(stringReq);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Fetching your details!");
        progressDialog.show();
    }

    /*@Override
    public void onStart() {
        super.onStart();
        settingsrequest();
    }

    public void settingsrequest() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(30 * 1000);
        locationRequest.setFastestInterval(5 * 1000);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true); //this is the key ingredient

        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                final LocationSettingsStates state = result.getLocationSettingsStates();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        // All location settings are satisfied. The client can initialize location
                        // requests here.
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied. But could be fixed by showing the user
                        // a dialog.
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult(getActivity(), REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                            // Ignore the error.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have no way to fix the
                        // settings so we won't show the dialog.
                        break;
                }
            }
        });
    }*/

    /*@Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
// Check for the integer request code originally supplied to startResolutionForResult().
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        //startLocationUpdates();
                        break;
                    case Activity.RESULT_CANCELED:
                        settingsrequest();//keep asking if imp or do whatever
                        break;
                }
                break;
        }
    }*/

    @Override
    public void onItemClick(View view, int position) {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.KEY_BUNDLE_DATA_PASSED, position + "");
        Frag_Individual_Offer offerFrag = new Frag_Individual_Offer();
        offerFrag.setArguments(bundle);

        replaceFrags(offerFrag, "Frag_Individual_Offer");
    }

    @Override
    public void onPause() {
        super.onPause();
        //stop location updates when Activity is no longer active
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;

        /*try {
            addMarkersToMap();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/

        mGoogleMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {
                LatLngBounds bounds = mGoogleMap.getProjection().getVisibleRegion().latLngBounds;
                arrayListOffers.clear();

                if (arrayListSelectedCategory.size() != 0) {
                    Log.e("marker size: ", arrayListMarkers.size() + "");
                    for (int i = 0; i < arrayListMarkers.size(); i++) {
                        Log.e("lol", "inside FOR: ");
                        for (int j = 0; j < arrayListSelectedCategory.size(); j++) {
                            if (arrayListMarkers.get(i).getCategory().equals(arrayListSelectedCategory.get(j))) {
                                arrayListOffers.add(arrayListMarkers.get(i).getTitle());
                            }
                        }
                    }

                    /*Log.e("Size: ", arrayListSelectedCategory.size() + "");*//*
                    for (int i = 0; i < arrayListSelectedCategory.size(); i++) {

                        Log.e("cate: ", arrayListSelectedCategory.get(i));
                        Log.e("marker size: ", arrayListMarkers.size() + "");

                        if (arrayListMarkers.contains(arrayListSelectedCategory.get(i))) {
                            Log.e("maker data: ", arrayListMarkers.get(i).getTitle());
                        }

                        //int indexMarker = arrayListMarkers.indexOf(arrayListSelectedCategory.get(i));

                        //Log.e("index: ", String.valueOf(indexMarker));

                        *//*if (bounds.contains(arrayListMarkers.get(indexMarker).getLatLng())) {
                            arrayListOffers.add(arrayListSelectedCategory.get(i));
                        }*//*
                    }*/

                } else {
                    for (int i = 0; i < arrayListMarkers.size(); i++) {
                        if (bounds.contains(arrayListMarkers.get(i).getLatLng())) {
                            arrayListOffers.add(arrayListMarkers.get(i).getTitle());
                        }
                    }
                }

                /*for (int i = 0; i < arrayListMarkers.size(); i++) {
                    if (bounds.contains(arrayListMarkers.get(i).getLatLng())) {
                        if (arrayListSelectedCategory.size() != 0) {

                            arrayListOffers.add(arrayListSelectedCategory.get(i));
                        }
                    }
                }*/

                recyclerViewAdapter.notifyDataSetChanged();

                // bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });

        // Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getContext(),
                    android.Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                //Location Permission already granted
                buildGoogleApiClient();
                mGoogleMap.setMyLocationEnabled(true);
            } else {
                //Request Location Permission
                checkLocationPermission();
            }
        } else {
            buildGoogleApiClient();
            mGoogleMap.setMyLocationEnabled(true);
        }
    }

    private void addMarkersToMap() throws InterruptedException {

        // Log.e("hey", "in addMarkersToMap --> " + arrayListMarkers.size());

        for (int i = 0; i < arrayListMarkers.size(); i++) {
            //Log.e("hey yo", "in addMarkersToMap --> " + arrayListMarkers.size());
            Log.e("hey yo", "arrayListSelectedCategory --> " + arrayListSelectedCategory.size());
            if (arrayListSelectedCategory.size() != 0) {
                Log.e("hey yo", "im inn!");
                for (int j = 0; j < arrayListSelectedCategory.size(); j++) {
                    Log.e("hey yo", "count time: --> " + j);
                    if (arrayListMarkers.get(i).getCategory().equals(arrayListSelectedCategory.get(j))) {
                        mGoogleMap.addMarker(
                                new MarkerOptions()
                                        .position(arrayListMarkers.get(i).getLatLng())
                                        .title(arrayListMarkers.get(i).getTitle())
                                        .icon(BitmapDescriptorFactory.fromResource(arrayListMarkers.get(i).getIconResId())));
                    }
                }
            } else {
                mGoogleMap.addMarker(
                        new MarkerOptions()
                                .position(arrayListMarkers.get(i).getLatLng())
                                .title(arrayListMarkers.get(i).getTitle())
                                .icon(BitmapDescriptorFactory.fromResource(arrayListMarkers.get(i).getIconResId())));
            }
        }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        mGoogleApiClient.connect();

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(30 * 1000);
        locationRequest.setFastestInterval(5 * 1000);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);

        //**************************
        builder.setAlwaysShow(true); //this is the key ingredient
        //**************************

        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                final LocationSettingsStates state = result.getLocationSettingsStates();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        // All location settings are satisfied. The client can initialize location
                        // requests here.
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied. But could be fixed by showing the user
                        // a dialog.
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult(
                                    getActivity(), 1000);
                        } catch (IntentSender.SendIntentException e) {
                            // Ignore the error.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have no way to fix the
                        // settings so we won't show the dialog.
                        break;
                }
            }
        });
    }

    @Override
    public void onConnected(Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(getActivity(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
    }

    @Override
    public void onLocationChanged(Location location) {

        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }

        // Place current location marker
        currLat = String.valueOf(location.getLatitude());
        currLng = String.valueOf(location.getLongitude());

        fetchData();

        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        markerOptions.icon(getMarkerIcon("#e2d3f0"));
        mCurrLocationMarker = mGoogleMap.addMarker(markerOptions);

        // move map camera
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(15));

        // stop location updates
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }

    // method definition
    public BitmapDescriptor getMarkerIcon(String color) {
        float[] hsv = new float[3];
        Color.colorToHSV(Color.parseColor(color), hsv);
        return BitmapDescriptorFactory.defaultMarker(hsv[0]);
    }

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(getActivity())
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs the Location permission, please accept to use location functionality")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(getActivity(),
                                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        }).create().show();
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(getActivity(),
                            android.Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mGoogleMap.setMyLocationEnabled(true);
                    }
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(getActivity(), "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        MapFragment f = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map_google);
        if (f != null)
            getFragmentManager().beginTransaction().remove(f).commit();
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.action_filter).setVisible(true);
        super.onPrepareOptionsMenu(menu);
    }

    public void replaceFrags(Fragment frag, String tag) {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.frag_container, frag);
        ft.addToBackStack(tag);
        ft.commit();
    }
}