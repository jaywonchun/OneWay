package com.example.john.oneway;

import java.io.Serializable;

/**
 * Created by John on 01/09/2015.
 */
public class Driver implements Serializable {

    private String mDenomination;
    private boolean mChecked;


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
