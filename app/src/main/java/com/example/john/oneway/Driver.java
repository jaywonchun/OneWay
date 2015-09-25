package com.example.john.oneway;

import android.widget.TimePicker;
import android.widget.Toast;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * Created by John on 01/09/2015.
 */
public class Driver implements Serializable {

    private String mDenomination;
    private boolean mChecked;

    private String mTimePicker;


    public String getmTimePicker() {
        return mTimePicker;
    }

    public void setmTimePicker(String timePicker) {mTimePicker = timePicker;}

    public String getDenomination() {
        return mDenomination;
    }

    public void setDenomination(String denomination) {
        mDenomination = denomination;
    }

    public boolean isChecked() {
        return mChecked;
    }

    public void setChecked(boolean checked) {
        mChecked = checked;
    }
}
