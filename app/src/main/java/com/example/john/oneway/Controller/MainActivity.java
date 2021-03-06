package com.example.john.oneway.Controller;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.john.oneway.Driver;
import com.example.john.oneway.Filter.Pop;
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
    private static AutoCompleteTextView mAutocompleteTextView;
    private LatLng globalLatLng;
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


        final ParseUser currentUser = ParseUser.getCurrentUser();
        final boolean userCheck = currentUser != null;


        if (currentUser == null) {
            navigateToLogin();
        } else {
            Log.i(TAG, "name is" + currentUser.getUsername());
            String userCategory = currentUser.getString("type");


            Log.e(TAG, "type of user is" + userCategory);
            if (userCategory.equals("Driver")) {
                setContentView(R.layout.activity_main);
                mSearchButton = (Button) findViewById(R.id.search_filter);

                // http://stackoverflow.com/questions/22759644/access-the-variable-in-activity-in-another-class


                //        currentUser.put();

                mMapFragment = ((MapFragment) getFragmentManager()
                        .findFragmentById(R.id.map));
                if (mMapFragment != null) {
                    mMapFragment.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(GoogleMap googleMap) {
                            if (userCheck) {
                                loadMap(googleMap);
                                latLng = loadMap(googleMap);
                            }
                        }

                    });


                }

                mSearchButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                        //   intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        //  intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.putExtra(MainActivity.EXTRA, latLng);
                        startActivity(intent);

                    }
                });

            } else {
                setContentView(R.layout.activity_passenger);
                if (currentUser == null) {
                    navigateToLogin();
                } else {
                    Log.i(TAG, "name is" + currentUser.getUsername());
                }
            }
        }
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
                            Log.e(TAG, "my last location is " + mLastLocation);

                            ParseUser currentUser = ParseUser.getCurrentUser();
                            ParseUser user;
                            user = currentUser;


                            Double lang = (Double) currentUser.get("updatedLastLocationLatitude");
                            Double longi = (Double) currentUser.get("updatedLastLocationLongitude");


                            Intent i =  getIntent();
                            String checkUser = i.getStringExtra(SearchActivity.EXTRA);
                            String x = "true";
                            String y = "false";
                            Log.e(TAG,"my value is" + checkUser);
                            if (x.equals(checkUser)) {
                                Log.e(TAG, "my check is " + checkUser);

                                ParseUser parseUser = ParseUser.getCurrentUser();
                                double userLatitude =  parseUser.getDouble("latitude");
                                double userLongitude =  parseUser.getDouble("longitude");
                                Log.e(TAG, "my userlocation is " + userLatitude);

                                latLng = new LatLng(userLatitude, userLongitude);

                                globalLatLng = latLng;

                                 currentUser = ParseUser.getCurrentUser();
                                user = currentUser;

                                user.put("updatedLastLocationLatitude", globalLatLng.latitude);
                                user.put("updatedLastLocationLongitude", globalLatLng.longitude);

                                user.saveInBackground();















                                Log.v(TAG, "we are not connected!" + globalLatLng);

                                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 14);
                                map.animateCamera(cameraUpdate);

                                map.addMarker(new MarkerOptions()
                                        .position(new LatLng(userLatitude, userLongitude))
                                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                                        .title("The 6ix"));





                            } else if (y.equals(checkUser)) {
                                Log.e(TAG, "my check is " + checkUser);

                                //if (SearchActivity.mAutocompleteTextView.isEnabled() == true) {

                                //  Intent intent = new Intent();
                                // LatLng userLocation = intent.getParcelableExtra(SearchActivity.EXTRA);

                                // Log.v(TAG, "my userlocation is " + userLocation.longitude);
                                // } else {


                                mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                                        mGoogleApiClient);
                                Log.e(TAG, "my last location is " + mLastLocation);


                                if (mLastLocation != null) {
                                    mLastLocation.getLatitude();
                                    mLastLocation.getLongitude();
                                    latLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());



                                    //   CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(userLocation, 14);
                                    //     map.animateCamera(cameraUpdate);
                                    globalLatLng = latLng;

                                     currentUser = ParseUser.getCurrentUser();

                                    user = currentUser;


                                    user.put("updatedLastLocationLatitude", globalLatLng.latitude);
                                    user.put("updatedLastLocationLongitude", globalLatLng.longitude);


                                    user.saveInBackground();





                                    Log.v(TAG, "we are connected!" + globalLatLng);



                                    Log.v(TAG, "my latitude is " + mLastLocation.getLatitude());
                                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 14);
                                    map.animateCamera(cameraUpdate);

                                    map.addMarker(new MarkerOptions()
                                            .position(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude()))
                                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                                            .title("The 6ix"));


                                }

                            }
                            else if (lang !=null && longi != null){
                                 currentUser = ParseUser.getCurrentUser();
                                if(currentUser.isAuthenticated()) {
                                     lang = (Double) currentUser.get("updatedLastLocationLatitude");
                                     longi = (Double) currentUser.get("updatedLastLocationLongitude");

                                    latLng = new LatLng(lang, longi);


                                    Log.e(TAG, "REAL SLIM SHADY" + currentUser.get("updatedLastLocationLatitude"));
                                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 14);
                                    map.animateCamera(cameraUpdate);
                                }
                                  /*  map.addMarker(new MarkerOptions()
                                            .position(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude()))
                                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                                            .title("The 6ix"));
                                    */
                            }else {
                                Log.e(TAG, "my check is " + checkUser);

                                //if (SearchActivity.mAutocompleteTextView.isEnabled() == true) {

                                //  Intent intent = new Intent();
                                // LatLng userLocation = intent.getParcelableExtra(SearchActivity.EXTRA);

                                // Log.v(TAG, "my userlocation is " + userLocation.longitude);
                                // } else {


                                mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                                        mGoogleApiClient);
                                Log.e(TAG, "my last location is " + mLastLocation);


                                if (mLastLocation != null) {
                                    mLastLocation.getLatitude();
                                    mLastLocation.getLongitude();
                                    latLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());



                                    //   CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(userLocation, 14);
                                    //     map.animateCamera(cameraUpdate);
                                    globalLatLng = latLng;
                                    Log.v(TAG, "we are connected!" + globalLatLng);



                                    Log.v(TAG, "my latitude iss " + mLastLocation.getLatitude());
                                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 14);
                                    map.animateCamera(cameraUpdate);

                                    map.addMarker(new MarkerOptions()
                                            .position(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude()))
                                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                                            .title("The 6ix"));


                                }
                            }



                            // Log.e(TAG, "Real value is"+ globalLatLng);

                        }

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
        Log.e(TAG, "I'm getting called" + id);
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

