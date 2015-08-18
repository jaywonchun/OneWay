package com.example.john.oneway;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;

/**
 * Created by John on 09/08/2015.
 */
public class jApplication extends Application {

    @Override
    public void onCreate(){
        super.onCreate();

        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "l79G9nvl4JCnQnwS8ITN4RDzM9SzOZEnHJna6D5m", "XutThlCJfAZOC4NDgyj676x89MXllQvQQetsw1bo");

        ParseObject testObject = new ParseObject("TestObject");
        testObject.put("foo", "bar");
        testObject.saveInBackground();
    }
}
