package com.example.john.oneway;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle; import android.view.View; import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow; import android.widget.TextView;








import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
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

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import android.view.ViewGroup.LayoutParams;


public class SearchActivity extends FragmentActivity implements Serializable,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks {

 private static final int EDIT_TASK_REQUEST = 10;
    private static final int EDIT_TIME_REQUEST = 20;

    private static final String LOG_TAG = "MainActivity";
private static final int GOOGLE_API_CLIENT_ID = 0;
    public static final String TAG = SearchActivity.class.getSimpleName();
    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String denominationName = "denoKey";
    public static final String selectedTime = "timeKey";


    private AutoCompleteTextView mAutocompleteTextView;
private AutoCompleteTextView mAutoCompleteTextView2;
private TextView mNameTextView;
private TextView mAddressTextView;
private TextView mIdTextView;
private TextView mPhoneTextView;
private TextView mWebTextView;
private TextView mAttTextView;
private GoogleApiClient mGoogleApiClient;
    private Spinner areaspinner;
private PlaceArrayAdapter mPlaceArrayAdapter;
    private filterAdapter mAdapter;
    private ArrayList<Driver> mFilterName;
    private ListView filterList;



    private PlaceArrayAdapter mChurchArrayAdapter;
private static final LatLngBounds BOUNDS_MOUNTAIN_VIEW = new LatLngBounds(new LatLng(43.445667, -79.971781), new LatLng(44.132634, -79.230450
));

    LinearLayout layoutOfPopup;
    PopupWindow popupMessage;
    Button mSave;
    SharedPreferences sharedpreferences;


//private List<Integer> filterTypes = new ArrayList<Integer>();

@Override
protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

    sharedpreferences = getSharedPreferences(MyPREFERENCES,
            Context.MODE_PRIVATE);


     if (sharedpreferences.contains(denominationName ) && (!sharedpreferences.contains(selectedTime))) {

         sharedpreferences.getString(denominationName, "");


         mFilterName = new ArrayList<Driver>();
         mFilterName.add(new Driver());

         mFilterName.get(0).setDenomination(sharedpreferences.getString(denominationName, ""));
         Log.i(TAG, "test2" + denominationName);

         mFilterName.add(new Driver());
        mFilterName.get(1).setmTimePicker("please select your time");


     }else if (sharedpreferences.contains(selectedTime) && (!sharedpreferences.contains(denominationName))  ) {
         mFilterName = new ArrayList<Driver>();
         mFilterName.add(new Driver());
         mFilterName.get(0).setDenomination("hi");

         sharedpreferences.getString(selectedTime, "");

         mFilterName.add(new Driver());

         mFilterName.get(1).setmTimePicker(sharedpreferences.getString(selectedTime, ""));
         Log.i(TAG, "test2" + selectedTime);
     }else if (sharedpreferences.contains(denominationName) && (sharedpreferences.contains(selectedTime))) {

         sharedpreferences.getString(denominationName, "");


         mFilterName = new ArrayList<Driver>();
         mFilterName.add(new Driver());

         mFilterName.get(0).setDenomination(sharedpreferences.getString(denominationName, ""));


         sharedpreferences.getString(selectedTime, "");

         mFilterName.add(new Driver());
         mFilterName.get(1).setmTimePicker(sharedpreferences.getString(selectedTime, ""));

     }else {

         //insert rest here !!! position 2 and 3


         mFilterName = new ArrayList<Driver>();

         mFilterName.add(new Driver());
         mFilterName.get(0).setDenomination("hi");

         mFilterName.add(new Driver());
         mFilterName.get(1).setmTimePicker("set your time");

         mFilterName.add(new Driver());
         mFilterName.get(2).setDenomination("Click here to set your .............");

     }


    filterList = (ListView)findViewById(R.id.filters);
    filterList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (position == 0) {
                Intent i = new Intent(SearchActivity.this, Pop.class);
                startActivityForResult(i, EDIT_TASK_REQUEST);

            }
                if(position == 1){
                    Intent i = new Intent(SearchActivity.this, TimeActivity.class);
                startActivityForResult(i, EDIT_TIME_REQUEST);
            }

        }
    });

    mAdapter = new filterAdapter(mFilterName);
    filterList.setAdapter(new filterAdapter(mFilterName));


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

    mAutoCompleteTextView2.setOnItemClickListener(mAutocompleteClickListener);
    mAutocompleteTextView.setOnItemClickListener(mAutocompleteClickListener);
  //  filterTypes.add(Place.TYPE_CHURCH);

    mChurchArrayAdapter= new PlaceArrayAdapter(this, android.R.layout.simple_list_item_1,BOUNDS_MOUNTAIN_VIEW, null);
    mAutoCompleteTextView2.setAdapter(mChurchArrayAdapter);




    mPlaceArrayAdapter = new PlaceArrayAdapter(this, android.R.layout.simple_list_item_1,

        BOUNDS_MOUNTAIN_VIEW, null);
        mAutocompleteTextView.setAdapter(mPlaceArrayAdapter);

        }



    private class filterAdapter extends  ArrayAdapter<Driver> {
        filterAdapter(ArrayList filterName) {

            super(SearchActivity.this, R.layout.filter_row, R.id.filter_item_name, filterName);
        }



        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //anon class can use constructor of its parents
            convertView = super.getView(position, convertView, parent);
            final Driver driver = getItem(position);
            if(position ==0) {
                 TextView filterName = (TextView) convertView.findViewById(R.id.filter_item_name);

                filterName.setText(driver.getDenomination());
            }

            if (position ==1) {
                  TextView filterName = (TextView) convertView.findViewById(R.id.filter_item_name);
         //       filterName.setText(driver.getDenomination()); works
                filterName.setText(String.valueOf(driver.getmTimePicker()));
              //  mFilterName.get(1).setmTimePicker(String.valueOf(hi));


                //make sure timepicker is formatted

                //   filterName.setText((CharSequence) driver.getmTimePicker());
            }

          //  filterName.setText((CharSequence) driver.getmTimePicker());

            return convertView;
        }

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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 10){
            if(resultCode== RESULT_OK) {
                //String result = data.getStringExtra(Pop.EXTRA);
                final Driver driver = (Driver) data.getSerializableExtra(Pop.EXTRA);
               mFilterName.get(0).setDenomination(driver.getDenomination());
                Log.i(TAG, "denomination is" + driver.getDenomination());

                mAdapter.setNotifyOnChange(true);
                filterList.setAdapter(new filterAdapter(mFilterName));
                Log.e(TAG, "DATA WAS RECEIVED" + driver.getDenomination());
                mSave =(Button) findViewById(R.id.pref_save_button);

                mSave.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {

                      //  Log.e(TAG, "Button!!!" + d);

                        String x =      mFilterName.get(0).setDenomination(driver.getDenomination());

                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString(denominationName, x);
                        Log.e(TAG, "test3" + x);
                        mAdapter.setNotifyOnChange(true);
                        filterList.setAdapter(new filterAdapter(mFilterName));
                        editor.commit();
                    }
                });
            } }

             if (requestCode == 20) {
                 if(resultCode== RESULT_OK){
                final Driver driver =(Driver) data.getSerializableExtra(TimeActivity.EXTRA);
            //     TimePicker timePicker = driver.getmTimePicker();
              //  mFilterName.get(1).setDenomination("hi");

                     mFilterName.get(1).setmTimePicker(String.valueOf(driver.getmTimePicker()));

                 Log.i(TAG, "time is" + driver.getmTimePicker());

                 mAdapter.setNotifyOnChange(true);
                 filterList.setAdapter(new filterAdapter(mFilterName));
                     mSave =(Button) findViewById(R.id.pref_save_button);

                mSave.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {


                       String y=   mFilterName.get(1).setmTimePicker(String.valueOf(driver.getmTimePicker()));




                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString(selectedTime, y);

                        Log.e(TAG, "test5" + y);

                        mAdapter.setNotifyOnChange(true);
                        filterList.setAdapter(new filterAdapter(mFilterName));
                        editor.commit();


































                    }
                });


             }


                //  filterList = (ListView)findViewById(R.id.filters);
           //     mAdapter = new filterAdapter(mFilterName);
            //   filterList.setAdapter(new filterAdapter(mFilterName));
            }if(resultCode == RESULT_CANCELED){
                Log.e(TAG, "DATA WASNT RECEIVED");

            }
        }

}