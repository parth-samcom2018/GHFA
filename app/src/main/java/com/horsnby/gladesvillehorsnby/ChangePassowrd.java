package com.horsnby.gladesvillehorsnby;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.horsnby.gladesvillehorsnby.models.ChangePW;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ChangePassowrd extends BaseVC{

    private Button savebutton,cancel_button;
    private EditText et_old_pw,etnewpw,etconfirmpw;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change);

        et_old_pw = findViewById(R.id.et_old_pw);
        etnewpw = findViewById(R.id.et_new_pw);
        etconfirmpw = findViewById(R.id.et_confirm_pw);
        savebutton = findViewById(R.id.save_button);
        cancel_button = findViewById(R.id.cancel_button);

        getDetails();

        savebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changePasswordAction();
            }
        });

        cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void getDetails() {


    }


    private void changePasswordAction() {


        et_old_pw.setError(null);
        etnewpw.setError(null);
        etconfirmpw.setError(null);

        String oldPassword = et_old_pw.getText().toString();
        String newPassword = etnewpw.getText().toString();
        String confirmPassword = etconfirmpw.getText().toString();

        if (oldPassword.isEmpty()){
            Toast toast = Toast.makeText(ChangePassowrd.this, "Old Password Missing!", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER,0,0);
            toast.show();
            et_old_pw.requestFocus();
            et_old_pw.setFocusable(true);
            DM.hideKeyboard(ChangePassowrd.this);
            return;
        }

        if(oldPassword.isEmpty()){
            Toast toast = Toast.makeText(ChangePassowrd.this, "Old Password Missing!", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER,0,0);
            toast.show();
            et_old_pw.requestFocus();
            et_old_pw.setFocusable(true);
            DM.hideKeyboard(ChangePassowrd.this);
            return;
        }

        if (newPassword.isEmpty()){

            Toast toast = Toast.makeText(ChangePassowrd.this, "New Password Missing!", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER,0,0);
            toast.show();
            etnewpw.requestFocus();
            etnewpw.setFocusable(true);
            DM.hideKeyboard(ChangePassowrd.this);
            return;
        }

        if (!DM.isPasswordValid(newPassword)){
            Toast toast = Toast.makeText(ChangePassowrd.this, "Password is too Short! Atleast 6 character required", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER,0,0);
            toast.show();
            etnewpw.requestFocus();
            etnewpw.setFocusable(true);
            DM.hideKeyboard(ChangePassowrd.this);
            return;
        }

        if (confirmPassword.isEmpty()){

            Toast toast = Toast.makeText(ChangePassowrd.this, "Confirm Password Missing!", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER,0,0);
            toast.show();
            etconfirmpw.requestFocus();
            etconfirmpw.setFocusable(true);
            DM.hideKeyboard(ChangePassowrd.this);
            return;
        }

        if (!etnewpw.getText().toString().matches(etconfirmpw.getText().toString())){
            Toast toast = Toast.makeText(ChangePassowrd.this, "New password and confirm password doesn't match!", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER,0,0);
            toast.show();
            etconfirmpw.requestFocus();
            etconfirmpw.setFocusable(true);
            DM.hideKeyboard(ChangePassowrd.this);
            return;
        }



        final ProgressDialog pd = DM.getPD(ChangePassowrd.this, "Loading for Changing Password...");
        pd.show();

        final ChangePW changePWModel = new ChangePW();

        DM.getApi().postNewPassword(DM.getAuthString(), changePWModel, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                Toast toast = Toast.makeText(ChangePassowrd.this, "New password Changed!", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER,0,0);
                toast.show();
                pd.dismiss();
                DM.hideKeyboard(ChangePassowrd.this);
            }

            @Override
            public void failure(RetrofitError error) {
                Toast toast = Toast.makeText(ChangePassowrd.this, "Failed Changed!", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER,0,0);
                toast.show();
                pd.dismiss();
                DM.hideKeyboard(ChangePassowrd.this);
            }
        });
    }



}

