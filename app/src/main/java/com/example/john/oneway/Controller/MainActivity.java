package com.example.john.oneway.Controller;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.john.oneway.Filter.SearchActivity;
import com.example.john.oneway.LoginPage.LoginActivity;
import com.example.john.oneway.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.ParseUser;


public class MainActivity extends AppCompatActivity implements  GoogleApiClient.OnConnectionFailedListener {

    public static final String TAG = MainActivity.class.getSimpleName();
    public static final String EXTRA = "FilterExtra";

    MapFragment mMapFragment;
protected Location mLastLocation;
private GoogleMap map;
private Button mSearchButton;
    private LatLng latLng;
private GoogleApiClient mGoogleApiClient;
    protected TextView mLatitudeText;
    protected TextView mLongitudeText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSearchButton = (Button) findViewById(R.id.search_filter);





        ParseUser currentUser = ParseUser.getCurrentUser();
       //        currentUser.put();
        if(currentUser == null) {
            navigateToLogin();
        }
        else {
            Log.i(TAG,currentUser.getUsername());
        }

        mMapFragment = ((MapFragment) getFragmentManager()
               .findFragmentById(R.id.map));
        if(mMapFragment !=null) {
            mMapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {
                    loadMap(googleMap);
                   latLng = loadMap(googleMap);

                }

            });


        }

        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                //   intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                //  intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.putExtra(MainActivity.EXTRA,latLng);
                startActivity(intent);

            }
        });

    }



        protected LatLng loadMap( GoogleMap googleMap) {
        map = googleMap;
        if (map != null) {
            Log.v(TAG, "my map is ready to use");
            Toast.makeText(this, "Map was loaded properly!", Toast.LENGTH_SHORT).show();
            map.setMyLocationEnabled(true);

            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(new ConnectionCallbacks() {
                        @Override
                        public void onConnected(Bundle bundle) {


                            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                                    mGoogleApiClient);
                            if (mLastLocation != null) {
                                mLastLocation.getLatitude();
                                mLastLocation.getLongitude();
                                latLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());


                                Log.v(TAG, "we are connected!");

                                Log.v(TAG, "my latitude is " + mLastLocation.getLatitude());
                                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 14);
                                map.animateCamera(cameraUpdate);

                                map.addMarker(new MarkerOptions()
                                        .position(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude()))
                                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                                        .title("The 6ix"));



                            }}

                        @Override
                        public void onConnectionSuspended(int i) {

                        }
                    })
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();

            connectClient();
        } else {
            Toast.makeText(this, "Error- Map was null!", Toast.LENGTH_SHORT).show();
        }

            return latLng;

    }



private void connectClient() {
        if(mGoogleApiClient !=null) {
            mGoogleApiClient.connect();
        }
}






    private void navigateToLogin() {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if(id == R.id.action_logout){
            ParseUser.logOut();
            navigateToLogin();
        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }












    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}
