package com.example.assignment4.View;

import android.app.Dialog;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.assignment4.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SearchMenuDialog.BottomSheetListener {
    private static final String TAG = "MainActivity";
    private static final int ERROR_DIALOG_REQUEST = 900;
    BottomNavigationView bottomNavigationView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        bottomNavigationView = findViewById(R.id.parking_bottom_nav);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);

        if (isServicesOk()) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new SearchFragment()).commit();
        }

    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    Fragment selectedFragment = null;
                    switch (menuItem.getItemId()) {
                        case R.id.nav_search:
                            selectedFragment = new SearchFragment();
                            break;
                        case R.id.nav_find:
                            selectedFragment = new FindFragment();
                            break;
                        case R.id.nav_reservation:
                            selectedFragment = new ReservationFragment();
                            break;
                        case R.id.nav_my_car:
                            selectedFragment = new MyCarFragment();
                            break;
                    }

                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            selectedFragment).commit();


                    return true;
                }
            };

    private boolean isServicesOk() {
        Log.d(TAG, "isServicesOk: checking google services version");

        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(MainActivity.this);

        if (available == ConnectionResult.SUCCESS) {
            //everything is fine and user can make request
            return true;
        } else if (GoogleApiAvailability.getInstance().isUserResolvableError(available)) {
            Log.d(TAG, "isServicesOk: an error has occured but it can be fixed!");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(MainActivity.this, available, ERROR_DIALOG_REQUEST);
            dialog.show();
        } else {
            Toast.makeText(this, "Cannot make map requests", Toast.LENGTH_SHORT).show();
        }
        return false;
    }


    @Override
    public void onSearchClicked(String location, String date, String time, Integer reservation) {
        Toast.makeText(getBaseContext(),
                "Location: " + location + "\n" + "Date: " + date + "\n"
                        + "Time: " + time + "\n" + "Reservation: " + reservation + " minutes",
                Toast.LENGTH_LONG).show();

        //geolocate(location);
    }
    private void geolocate(String location){
        Log.d(TAG, "geolocate: Geolocating...");

        String searchString = location;

        Geocoder geocoder = new Geocoder(MainActivity.this);
        List<Address> list = new ArrayList<>();
        try{
            list = geocoder.getFromLocationName(searchString, 1);
        }catch (IOException e){
            Log.e(TAG, "geolocate: IOException: "+ e.getMessage());
        }
        if(list.size() > 0){
            Address address = list.get(0);

            Log.d(TAG, "geolocate: found a location: "+address.toString());

            SearchFragment searchFragment = new SearchFragment();
            searchFragment.getLatLongResult(address.getLatitude(),address.getLongitude(), address.getAddressLine(0));
        }
    }
}
