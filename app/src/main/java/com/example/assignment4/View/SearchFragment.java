package com.example.assignment4.View;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
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
    GoogleMap mGoogleMap;
    MapView mMapView;
    View mView;
    FloatingActionButton btn_open_search;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mView =inflater.inflate(R.layout.fragment_search,container,false);

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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(getContext());

        mGoogleMap = googleMap;
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(33.992020,-84.346451)).title("My Home").snippet("this is where I sleep"));

        CameraPosition sookHome = CameraPosition.builder().target(new LatLng(33.992020,-84.346451)).zoom(16).bearing(0).tilt(45).build();
        mGoogleMap.moveCamera(CameraUpdateFactory.newCameraPosition(sookHome));

        //LatLng sookHome = new LatLng(33.992020,-84.346451);
        //mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(sookHome));
    }
}
