package com.example.john.oneway.LoginPage;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.john.oneway.Controller.MainActivity;
import com.example.john.oneway.R;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SignUpCallback;


public class SignupActivity extends AppCompatActivity {
    public static final String TAG = SignupActivity.class.getSimpleName();
    private static final String[] choices = {"Driver", "Passenger"};
    protected EditText mUsername;
    protected EditText mPassword;
    protected EditText mEmail;
    protected Button mSignupButton;
    private Spinner spinner1;
    private int sid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        spinner1 = (Spinner) findViewById(R.id.spinner1);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, choices);


        spinner1.setAdapter(adapter);
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                final int sid = spinner1.getSelectedItemPosition();
                final String type = spinner1.getItemAtPosition(sid).toString();

                mUsername = (EditText) findViewById(R.id.usernameField);
                mPassword = (EditText) findViewById(R.id.passwordField);
                mEmail = (EditText) findViewById(R.id.emailField);
                mSignupButton = (Button) findViewById(R.id.signupButton);

                mSignupButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d(TAG, "We are logging" + String.valueOf(sid));
                        String username = mUsername.getText().toString();
                        String password = mPassword.getText().toString();
                        String email = mEmail.getText().toString();

                        username = username.trim();
                        password = password.trim();
                        email = email.trim();

                        if (username.isEmpty() || password.isEmpty() || email.isEmpty()) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(SignupActivity.this);
                            builder.setMessage(R.string.signup_error_message);
                            builder.setTitle(R.string.signup_error_title);
                            builder.setPositiveButton(android.R.string.ok, null);
                            AlertDialog dialog = builder.create();
                            dialog.show();
                        } else {

                            final ParseUser user = new ParseUser();
                            user.setUsername(username);
                            user.setPassword(password);
                            user.setEmail(email);
                            user.put("type", type);

                            user.signUpInBackground(new SignUpCallback() {
                                @Override
                                public void done(final ParseException e) {
                                    if (e == null && sid == 0) {
                                        //ParseUser volunteerDetails = new ParseUser("Volunteer");
                                        // volunteerDetails.put("typez","driver");

                                        //  String objectId = volunteerDetails.getObjectId();
                                        //  volunteerDetails.saveInBackground();

                                        Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                    }else if(e == null && sid == 1){
                                        ParseObject volunteerDetails = new ParseObject("Volunteer");
                                        volunteerDetails.put("typez", "passenger");
                                        String objectId = volunteerDetails.getObjectId();
                                        volunteerDetails.saveInBackground();

                                        Intent intent1 = new Intent(SignupActivity.this, Passenger.class);
                                        intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent1);

                                    }else {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(SignupActivity.this);
                                        builder.setMessage(e.getMessage());
                                        builder.setTitle(R.string.signup_error_title);
                                        builder.setPositiveButton(android.R.string.ok, null);
                                        AlertDialog dialog = builder.create();
                                        dialog.show();
                                    }
                                }

                            });
                        }
                    }
                });

            }





            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_signup, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
