package com.example.john.oneway.Filter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.view.View; import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow; import android.widget.TextView;


import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.john.oneway.Controller.MainActivity;
import com.example.john.oneway.Driver;
import com.example.john.oneway.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class SearchActivity extends FragmentActivity implements Serializable,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks {

 private static final int EDIT_TASK_REQUEST = 10;
    private static final int EDIT_TIME_REQUEST = 20;

    private static final String LOG_TAG = "MainActivity";
private static final int GOOGLE_API_CLIENT_ID = 0;
    public static final String TAG = SearchActivity.class.getSimpleName();

   public static CheckBox checkBoxAddress;
    public static AutoCompleteTextView mAutocompleteTextView;
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
    public static final String EXTRA = "userPreferenceExtra";

Button mSave2;

    ParseObject filter;
     String filterID ;



    private PlaceArrayAdapter mChurchArrayAdapter;
private static final LatLngBounds BOUNDS_MOUNTAIN_VIEW = new LatLngBounds(new LatLng(43.445667, -79.971781), new LatLng(44.132634, -79.230450
));

    LinearLayout layoutOfPopup;
    PopupWindow popupMessage;
    Button mSave;
    SharedPreferences sharedpreferences;
    String denoName;
    String homeAddress;
    String churchAddress;
    String serviceTime;
    ArrayList<String> mDenoName;


//private List<Integer> filterTypes = new ArrayList<Integer>();

@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_search);
//Log.e(TAG, "current user is" + currentUser.getObjectId());
    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);




    SharedPreferences settings = getSharedPreferences("mysettings", 0);
    final SharedPreferences.Editor editor = settings.edit();



    String user = ParseUser.getCurrentUser().getObjectId();
    ParseUser parseUser = ParseUser.getCurrentUser();



    mAutocompleteTextView = (AutoCompleteTextView) findViewById(R.id .autoCompleteTextView);
    mAutocompleteTextView.setThreshold(3);
   checkBoxAddress = (CheckBox)findViewById(R.id.addressCheckBox);
    checkBoxAddress.setChecked(settings.getBoolean("checkBoxState", false));
    if(checkBoxAddress.isChecked()) {
        mAutocompleteTextView.setEnabled(false);
    };

//http://stackoverflow.com/questions/9308267/passing-a-simple-variable-value-for-use-in-another-class-in-android


    mAutoCompleteTextView2= (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView2);
    mAutoCompleteTextView2.setThreshold(3);
    //mAutocompleteTextView.setFocusable(false);
checkBoxAddress.setOnClickListener(new OnClickListener() {
    @Override
    public void onClick(View v) {
        //    mAutocompleteTextView.setFocusable(true);
        if(checkBoxAddress.isChecked()) {
            mAutocompleteTextView.setEnabled(false);
            Log.e(TAG, "I'm getting clicked and its false!");

            boolean checkBoxValue = true;
            editor.putBoolean("checkBoxState", checkBoxValue);
            editor.commit();;



        }else {
            mAutocompleteTextView.setEnabled(true);

            Log.e(TAG, "I'm getting clicked and its true!");

            boolean checkBoxValue = false;
            editor.putBoolean("checkBoxState", checkBoxValue);
            editor.commit();;



        }
      //  Log.e(TAG, "I'm getting clicked!");


    }
});


    ParseQuery<ParseUser> query = ParseQuery.getQuery("User");
    //query.whereExists("namezz");



        query.getInBackground(user, new GetCallback<ParseUser>() {
                    @Override
                    public void done(ParseUser parseUser, ParseException e) {

                        parseUser = ParseUser.getCurrentUser();
                        //   mDenoName= new ArrayList<String>();
                        Log.e(TAG, "current user is is" + parseUser);

                        //  mDenoName.add(denoName);
                        //  mDenoName.add(serviceTIme);

                        denoName = parseUser.getString("namezz");
                        serviceTime = parseUser.getString("serviceTime");
                        homeAddress = parseUser.getString("HomeAddress");
                        churchAddress = parseUser.getString("churchAddress");


                        Log.e(TAG, "time is" + serviceTime);
                        Log.e(TAG, "namezzz is" + denoName);


                    }

                }

        );
        mFilterName = new ArrayList<Driver>();

        denoName = parseUser.getString("namezz");
        mFilterName.add(new Driver());
        if(denoName == null) {
            mFilterName.get(0).setDenomination("Select denomination ");
        }else {
        mFilterName.get(0).setDenomination(denoName)
        ;}

       serviceTime = parseUser.getString("serviceTime");
    mFilterName.add(new Driver());
        if(serviceTime == null) {
            mFilterName.get(1).setmTimePicker("select the service time");
        }else {
        mFilterName.get(1).setmTimePicker(serviceTime);}

    homeAddress = parseUser.getString("HomeAddress");
    if(homeAddress == null) {
            mAutocompleteTextView.setText("Please select your home address");
    }else {
        mAutocompleteTextView.setText(homeAddress);

        churchAddress = parseUser.getString("churchAddress");
        if(churchAddress == null) {
            mAutoCompleteTextView2.setText("Please select your church address");
        }else {
            mAutoCompleteTextView2.setText(churchAddress);
        }

    }




    filterList = (ListView)findViewById(R.id.filters);
    filterList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (position == 0) {
                Intent i = new Intent(SearchActivity.this, Pop.class);
                startActivityForResult(i, EDIT_TASK_REQUEST);

            }
            if (position == 1) {
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






        mNameTextView = (TextView) findViewById(R.id.name);
    // mAddressTextView = (TextView) findViewById(R.id.address);

    mAutocompleteTextView.setOnItemClickListener(mAutocompleteClickListener);
    mAutoCompleteTextView2.setOnItemClickListener(mAutocompleteClickListener2);


    //  filterTypes.add(Place.TYPE_CHURCH);

    mChurchArrayAdapter= new PlaceArrayAdapter(this, android.R.layout.simple_list_item_1,BOUNDS_MOUNTAIN_VIEW, null);
    mAutoCompleteTextView2.setAdapter(mPlaceArrayAdapter);




    mPlaceArrayAdapter = new PlaceArrayAdapter(this, android.R.layout.simple_list_item_1,

        BOUNDS_MOUNTAIN_VIEW, null);
        mAutocompleteTextView.setAdapter(mPlaceArrayAdapter);
        mAutoCompleteTextView2.setAdapter(mPlaceArrayAdapter);

    mSave =(Button) findViewById(R.id.pref_save_button);

    mSave.setOnClickListener(new OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent intent = new Intent(SearchActivity.this, MainActivity.class);
        if(mAutocompleteTextView.isEnabled()) {
            intent.putExtra(SearchActivity.EXTRA, "true");

        }
        if(!mAutocompleteTextView.isEnabled()) {
            intent.putExtra(SearchActivity.EXTRA, "false");

        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

    }
});
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

            if (position ==1)
            {
                  TextView filterName = (TextView) convertView.findViewById(R.id.filter_item_name);
                filterName.setText(driver.getmTimePicker());



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

            Log.e(TAG, "i'm selected inside");
            Log.e(TAG,"My position is" + position);
            final PlaceArrayAdapter.PlaceAutocomplete item= mPlaceArrayAdapter.getItem(position);
            final String placeId = String.valueOf(item.placeId);
            Log.i(LOG_TAG, "Selected: " + item.description);
            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById(mGoogleApiClient, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
            Log.i(LOG_TAG, "Fetching details for ID: " + item.placeId);

        }

    };



    private AdapterView.OnItemClickListener mAutocompleteClickListener2
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            Log.e(TAG, "i'm selected inside");
            Log.e(TAG,"My position is" + position);
            final PlaceArrayAdapter.PlaceAutocomplete item= mPlaceArrayAdapter.getItem(position);
            final String placeId2 = String.valueOf(item.placeId);
            Log.i(LOG_TAG, "Selected: " + item.description);
            PendingResult<PlaceBuffer> placeResult2 = Places.GeoDataApi
                    .getPlaceById(mGoogleApiClient, placeId2);
            placeResult2.setResultCallback(mUpdatePlaceDetailsCallback2);
            Log.i(LOG_TAG, "Fetching details for ID: " + item.placeId);

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





    ParseUser currentUser = ParseUser.getCurrentUser();
    ParseUser user;
    user = currentUser;
    Log.e(TAG, "current home address is" + place.getAddress());
    user.put("HomeAddress", place.getAddress());
    user.put("latitude", place.getLatLng().latitude);
    user.put("longitude", place.getLatLng().longitude);
    user.saveInBackground();


  //  Intent intent = new Intent(SearchActivity.this, MainActivity.class);
   // intent.putExtra(SearchActivity.EXTRA, String.valueOf(place.getLatLng()));
   // startActivity(intent);
//Log.e(TAG, "take a sip"  + String.valueOf(place.getLatLng()));
        }
        };


    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback2
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





            ParseUser currentUser = ParseUser.getCurrentUser();
            ParseUser user;
            user = currentUser;
            Log.e(TAG, "current church address is" + place.getAddress());
            user.put("churchAddress", place.getAddress());
            user.saveInBackground();

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

                      //  filter = new ParseObject("denominationName");
                       // filter.put("namez", driver.getDenomination());

                        ParseUser currentUser = ParseUser.getCurrentUser();
                        ParseUser user;
                        user = currentUser;
                        Log.e(TAG, "current user is" + user.getObjectId());
                        user.put("namezz", driver.getDenomination());
                        user.saveInBackground();
                        mAdapter.setNotifyOnChange(true);
                        filterList.setAdapter(new filterAdapter(mFilterName));

                        Intent intent = new Intent(SearchActivity.this, MainActivity.class);
                        if(mAutocompleteTextView.isEnabled()) {
                            intent.putExtra(SearchActivity.EXTRA, "true");

                        }

                        startActivity(intent);

                    }
                });
            } }

             if (requestCode == 20) {
                 if(resultCode== RESULT_OK){
                final Driver driver =(Driver) data.getSerializableExtra(TimeActivity.EXTRA);
            //     TimePicker timePicker = driver.getmTimePicker();
              //  mFilterName.get(1).setDenomination("hi");

                     mFilterName.get(1).setmTimePicker(String.valueOf(driver.getmTimePicker()));
                 Log.i(TAG, "timesss is" + driver.getmTimePicker());

                 mAdapter.setNotifyOnChange(true);
                 filterList.setAdapter(new filterAdapter(mFilterName));
                     mSave =(Button) findViewById(R.id.pref_save_button);

                mSave.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {



                    //    ParseObject filter2 = new ParseObject("selectedTime");
                      //  filter2.put("time",   mFilterName.get(1).setmTimePicker(String.valueOf(driver.getmTimePicker())));
                      //  filter2.saveInBackground();

                        ParseUser currentUser = ParseUser.getCurrentUser();
                        ParseUser user;
                        user = currentUser;
                        Log.e(TAG, "current user is" + user.getObjectId());
                        user.put("serviceTime", driver.getmTimePicker());
                        user.saveInBackground();
                        mAdapter.setNotifyOnChange(true);
                        filterList.setAdapter(new filterAdapter(mFilterName));


                        Intent intent = new Intent(SearchActivity.this, MainActivity.class);
                        if(mAutocompleteTextView.isEnabled()) {
                            intent.putExtra(SearchActivity.EXTRA, "true");

                        }

                        startActivity(intent);




                    }
                });


             }


                //  filterList = (ListView)findViewById(R.id.filters);
           //     mAdapter = new filterAdapter(mFilterName);
            //   filterList.setAdapter(new filterAdapter(mFilterName));
            }
        if(resultCode == RESULT_CANCELED){
                Log.e(TAG, "DATA WASNT RECEIVED");

            }
        }




}