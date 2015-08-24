package com.example.john.oneway;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.PlaceTypes;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class SearchActivity extends FragmentActivity implements
        GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks {
private static final String LOG_TAG = "MainActivity";
private static final int GOOGLE_API_CLIENT_ID = 0;

private AutoCompleteTextView mAutocompleteTextView;
private AutoCompleteTextView mAutoCompleteTextView2;
private TextView mNameTextView;
private TextView mAddressTextView;
private TextView mIdTextView;
private TextView mPhoneTextView;
private TextView mWebTextView;
private TextView mAttTextView;
private GoogleApiClient mGoogleApiClient;
private PlaceArrayAdapter mPlaceArrayAdapter;
private PlaceArrayAdapter mChurchArrayAdapter;
private static final LatLngBounds BOUNDS_MOUNTAIN_VIEW = new LatLngBounds(new LatLng(43.445667, -79.971781), new LatLng(44.132634, -79.230450
));
//private List<Integer> filterTypes = new ArrayList<Integer>();

@Override
protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        mGoogleApiClient = new GoogleApiClient.Builder(SearchActivity.this)
            .addApi(Places.GEO_DATA_API)
           .enableAutoManage(this, GOOGLE_API_CLIENT_ID, this)
            .addConnectionCallbacks(this)
            .build();

      mAutocompleteTextView = (AutoCompleteTextView) findViewById(R.id .autoCompleteTextView);
        mAutocompleteTextView.setThreshold(3);


    mAutoCompleteTextView2= (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView2);
    mAutoCompleteTextView2.setThreshold(3);
        mNameTextView = (TextView) findViewById(R.id.name);
        mAddressTextView = (TextView) findViewById(R.id.address);
        mIdTextView = (TextView) findViewById(R.id.place_id);
        mPhoneTextView = (TextView) findViewById(R.id.phone);
        mWebTextView = (TextView) findViewById(R.id.web);
        mAttTextView = (TextView) findViewById(R.id.att);
    mAutoCompleteTextView2.setOnItemClickListener(mAutocompleteClickListener);
    mAutocompleteTextView.setOnItemClickListener(mAutocompleteClickListener);
  //  filterTypes.add(Place.TYPE_CHURCH);

    mChurchArrayAdapter= new PlaceArrayAdapter(this, android.R.layout.simple_list_item_1,BOUNDS_MOUNTAIN_VIEW, null);
    mAutoCompleteTextView2.setAdapter(mChurchArrayAdapter);




    mPlaceArrayAdapter = new PlaceArrayAdapter(this, android.R.layout.simple_list_item_1,

        BOUNDS_MOUNTAIN_VIEW, null);
        mAutocompleteTextView.setAdapter(mPlaceArrayAdapter);
        }





private AdapterView.OnItemClickListener mAutocompleteClickListener
        = new AdapterView.OnItemClickListener() {
@Override
public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    if (mAutoCompleteTextView2.isSelected()) {
        final PlaceArrayAdapter.PlaceAutocomplete item2 = mChurchArrayAdapter.getItem(position);
        final String placeId2 = String.valueOf(item2.placeId);
        Log.i(LOG_TAG, "Selected: " + item2.description);
        PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                .getPlaceById(mGoogleApiClient, placeId2);
        placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
        Log.i(LOG_TAG, "Fetching details for ID: " + item2.placeId);
    } else if (mAutocompleteTextView.isSelected()){

        final PlaceArrayAdapter.PlaceAutocomplete item = mPlaceArrayAdapter.getItem(position);
        final String placeId = String.valueOf(item.placeId);
        Log.i(LOG_TAG, "Selected: " + item.description);
        PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                .getPlaceById(mGoogleApiClient, placeId);
        placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
        Log.i(LOG_TAG, "Fetching details for ID: " + item.placeId);

    }
}

};




private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback
        = new ResultCallback<PlaceBuffer>() {
@Override
public void onResult(PlaceBuffer places) {
        if (!places.getStatus().isSuccess()) {
        Log.e(LOG_TAG, "Place query did not complete. Error: " +
        places.getStatus().toString());
        return;
        }
// Selecting the first object buffer.
final Place place = places.get(0);
        CharSequence attributions = places.getAttributions();

        mNameTextView.setText(Html.fromHtml(place.getName() + ""));
        mAddressTextView.setText(Html.fromHtml(place.getAddress() + ""));
        mIdTextView.setText(Html.fromHtml(place.getId() + ""));
        mPhoneTextView.setText(Html.fromHtml(place.getPhoneNumber() + ""));
        mWebTextView.setText(place.getWebsiteUri() + "");
        if (attributions != null) {
        mAttTextView.setText(Html.fromHtml(attributions.toString()));
        }
        }
        };

@Override
public void onConnected(Bundle bundle) {
       mPlaceArrayAdapter.setGoogleApiClient(mGoogleApiClient);
        mChurchArrayAdapter.setGoogleApiClient(mGoogleApiClient);
        Log.i(LOG_TAG, "Google Places API connected.");

        }

@Override
public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.e(LOG_TAG, "Google Places API connection failed with error code: "
        + connectionResult.getErrorCode());

        Toast.makeText(this,
        "Google Places API connection failed with error code:" +
        connectionResult.getErrorCode(),
        Toast.LENGTH_LONG).show();
        }

@Override
public void onConnectionSuspended(int i) {
        mPlaceArrayAdapter.setGoogleApiClient(null);
        Log.e(LOG_TAG, "Google Places API connection suspended.");
        }
        }