package com.example.john.oneway.Filter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.john.oneway.Driver;
import com.example.john.oneway.R;

import java.util.ArrayList;

/**
 * Created by John on 28/08/2015.
 */
public class Pop extends Activity  {
    public static final String TAG = Pop.class.getSimpleName();

    public static final String EXTRA = "DriverExtra";

    private ArrayList<Driver> mDenoName;
    private TaskAdapter mAdapter;
   private Button mSaveButton;
    private RadioButton mDoneBox;
   private ListView denominationList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.popupwindow);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width *.8),(int)(height*0.8));
        getDenominationNames();


         denominationList= (ListView)findViewById(R.id.denomination);
        denominationList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        mAdapter = new TaskAdapter(mDenoName);

      //  mAdapter = new TaskAdapter(mDenoName);



        denominationList.setAdapter(new TaskAdapter(mDenoName));
        mSaveButton= (Button)findViewById(R.id.save_button);
        mSaveButton.setEnabled(false);



    }




    private class TaskAdapter extends  ArrayAdapter<Driver> {
           TaskAdapter(ArrayList denoName) {
                super(Pop.this,R.layout.denominationrow,R.id.deno_item_name,denoName);
           }
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            convertView = super.getView(position, convertView, parent);
            final Driver driver = getItem(position);
            final TextView denooName =(TextView) convertView.findViewById(R.id.deno_item_name);

            denooName.setText(driver.getDenomination());

            mDoneBox =(RadioButton) convertView.findViewById(R.id.done_item);
            mDoneBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                        mSaveButton.setEnabled(true);
                    boolean mCurrentlyCheckedRB = mDoneBox.isChecked();
                    // checks if we already have a checked radiobutton. If we don't, we can assume that
// the user clicked the current one to check it, so we can remember it.
                    if (!mCurrentlyCheckedRB)
                        mDoneBox = (RadioButton) v;
                    final String name= (String) denooName.getText();
                    mSaveButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent returnIntent = new Intent();
                            returnIntent.putExtra(EXTRA,driver);
                            setResult(RESULT_OK, returnIntent);
                            finish();


                        }
                    });

                    Log.i(TAG, "my namez is" + name);
                    mDoneBox.setChecked(true);


// If the user clicks on a RadioButton that we've already stored as being checked, we
// don't want to do anything.
                    if (mDoneBox == v)

                        return;

// Otherwise, uncheck the currently checked RadioButton, check the newly checked
// RadioButton, and store it for future reference.
                    mDoneBox.setChecked(false);
                    ((RadioButton) v).setChecked(true);
                    mDoneBox = (RadioButton) v;
                    Log.i(TAG, "my namez is" + name);

                    }



            });


            //doneBox.setChecked(driver.isChecked());
            return convertView;
        }

    }



    public void getDenominationNames(){

        mDenoName = new ArrayList<Driver>();

        mDenoName.add(new Driver());
        mDenoName.get(0).setDenomination("Catholic");
        mDenoName.add(new Driver());
        mDenoName.get(1).setDenomination("Protestant");
        mDenoName.add(new Driver());
        mDenoName.get(2).setDenomination("Prysbeterian");
        mDenoName.add(new Driver());
        mDenoName.get(3).setDenomination("Lutheran");
        mDenoName.add(new Driver());
        mDenoName.get(4).setDenomination("Baptist");






    }

}
