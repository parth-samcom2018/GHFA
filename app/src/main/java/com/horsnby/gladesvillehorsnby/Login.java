package com.horsnby.gladesvillehorsnby;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.horsnby.gladesvillehorsnby.models.Member;
import com.horsnby.gladesvillehorsnby.models.Token;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class Login extends AppCompatActivity {

    private static final String TAG = "RegisterToken";
    private TextView DeviceID;
    String unique_id;

    private EditText mEmailView,mPasswordView;
    private Button btn_register;
    public static String justRegisteredUsername = null;
    public static String justRegisteredPassword = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        setContentView(R.layout.activity_login);

        mEmailView = findViewById(R.id.et_username);
        mPasswordView = findViewById(R.id.et_password);
        btn_register = findViewById(R.id.register_button);

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register();
            }
        });

        Button btn_login = findViewById(R.id.login_in_button);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mEmailView.getText().toString().isEmpty() && mPasswordView.getText().toString().isEmpty()){

                    /*Toast toast = */Toast.makeText(Login.this, "Username and Password Missing!", Toast.LENGTH_SHORT).show();
                    /*toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();*/
                    return ;
                }
                attemptLogin();
            }
        });

        //check for registered user
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String username = preferences.getString("HQUsername", null);
        String token = preferences.getString("HQToken", null);
        int memberID = preferences.getInt("HQMemberId", 0);
        if (username != null && token != null) {
            Token t = new Token();
            t.userName = username;
            t.access_token = token;
            t.memberId = memberID;
            setUserOnDevice(t);
            proceed();
        }

        DeviceID = findViewById(R.id.textView1);

        unique_id = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        DeviceID.setText("My ID is: " + unique_id);
        Log.i(TAG, "Registration Device: " + unique_id);
    }

    private void register() {

        Intent i = new Intent(this, Registration.class);
        startActivity(i);
    }

    private void proceed() {
        //  if(true)return;
        Intent i = new Intent(this, MainTabbing.class);
        startActivity(i);
    }

    private void setUserOnDevice(Token tokenModel)
    {
        DM.member = new Member();
        DM.member.username = tokenModel.userName;
        DM.member.access_token = tokenModel.access_token;
        DM.member.memberId = tokenModel.memberId;

        Log.d("HQ","here is a memberID"+DM.member.memberId);



        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("HQUsername",tokenModel.userName);
        editor.putString("HQToken",tokenModel.access_token);
        editor.putInt("HQMemberId",tokenModel.memberId);
        editor.apply();

    }

    @Override
    protected void onResume() {
        super.onResume();

        if(justRegisteredUsername != null && justRegisteredPassword != null)
        {

            mEmailView.setText(justRegisteredUsername);
            mPasswordView.setText(justRegisteredPassword);
            attemptLogin();
            justRegisteredPassword = null;
            justRegisteredUsername = null;
        }

    }

    private void attemptLogin() {



        mEmailView.setError(null);
        mPasswordView.setError(null);


        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !DM.isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!DM.isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (TextUtils.isEmpty(password)){
            mPasswordView.setError(getString(R.string.error_password));
            focusView = mPasswordView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            DM.hideKeyboard(this);
            final ProgressDialog pd = DM.getPD(this,"Logging In...");
            pd.show();



            DM.getApi().login("password", email, password, new Callback<Token>() {
                @Override
                public void success(Token login, Response response) {
                    Log.d("hq","success");
                    pd.dismiss();
                    setUserOnDevice(login);

                    //get the device TOKEN id in background...set for push

                    Intent service = new Intent(Login.this, RegistrationIntentService.class);
                    startService(service);

                    proceed();

                }

                @Override
                public void failure(RetrofitError error) {

                    pd.dismiss();
                    Log.d("hq","failed"+error.getMessage());
                    Toast.makeText(Login.this,"Could not login: "+error.getMessage(),Toast.LENGTH_LONG).show();

                }
            });


        }


    }

}
