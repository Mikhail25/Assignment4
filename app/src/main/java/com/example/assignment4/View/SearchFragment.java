package com.example.assignment4.View;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.assignment4.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class SearchFragment extends Fragment implements OnMapReadyCallback {
    private static final float DEFAULT_ZOOM = 16 ;
    GoogleMap mGoogleMap;
    MapView mMapView;
    View mView;
    FloatingActionButton btn_open_search;

    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1423;
    private static final String TAG = SearchFragment.class.getSimpleName();

    private boolean mLocationPermissionGranted = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mView =inflater.inflate(R.layout.fragment_search,container,false);
        getLocationPermission();

        btn_open_search = mView.findViewById(R.id.open_search_menu);
        btn_open_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchMenuDialog searchSheet = new SearchMenuDialog();
                searchSheet.show(getFragmentManager(), "searchBottomSheet");
            }
        });


        return mView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mMapView = mView.findViewById(R.id.map_search);
        if(mMapView != null){
            mMapView.onCreate(null);
            mMapView.onResume();
            mMapView.getMapAsync(this);
        }
    }

    private void getLocationPermission(){
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};

        if(ContextCompat.checkSelfPermission(this.getContext(),FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(this.getContext(),COARSE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED){
                mLocationPermissionGranted = true;
                MapsInitializer.initialize(getContext());
            }else{
                ActivityCompat.requestPermissions(getActivity(),permissions, LOCATION_PERMISSION_REQUEST_CODE);
            }
        }else{
            ActivityCompat.requestPermissions(getActivity(),permissions, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;

        switch (requestCode){
            case LOCATION_PERMISSION_REQUEST_CODE:
                if(grantResults.length >0){
                    for(int i=0; i < grantResults.length; i++){
                        if(grantResults[i]!= PackageManager.PERMISSION_GRANTED){
                            mLocationPermissionGranted = false;
                            return;
                        }
                    }
                    mLocationPermissionGranted = true;
                    //map initialize
                }

        }
    }
    public void getLatLongResult(double latitude, double longitude, String title){
        CameraPosition searchedPos = CameraPosition.builder().target(new LatLng(latitude,longitude)).zoom(16).bearing(0).tilt(45).build();
        MarkerOptions markerOptions = new  MarkerOptions().position(new LatLng(latitude,longitude)).title(title);

        Log.d(TAG, "cameraMover: moving the camera to lat: "+latitude+"lng: "+longitude);
        cameraMover(searchedPos,markerOptions);

    }

    private void cameraMover(CameraPosition searchedPos,MarkerOptions markerOptions){

        mGoogleMap.moveCamera(CameraUpdateFactory.newCameraPosition(searchedPos));
        mGoogleMap.addMarker(markerOptions);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {


        mGoogleMap = googleMap;
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(33.992020,-84.346451)).title("My Home"));

        CameraPosition sookHome = CameraPosition.builder().target(new LatLng(33.992020,-84.346451)).zoom(16).bearing(0).tilt(45).build();
        mGoogleMap.moveCamera(CameraUpdateFactory.newCameraPosition(sookHome));

        //LatLng sookHome = new LatLng(33.992020,-84.346451);
        //mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(sookHome));
    }



}
