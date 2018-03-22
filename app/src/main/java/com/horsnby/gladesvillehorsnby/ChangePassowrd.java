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
    private EditText etnewpw,etconfirmpw;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change);


        etnewpw = findViewById(R.id.et_new_pw);
        etconfirmpw = findViewById(R.id.et_confirm_pw);
        savebutton = findViewById(R.id.save_button);
        cancel_button = findViewById(R.id.cancel_button);

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

    private void changePasswordAction() {

        etnewpw.setError(null);
        etconfirmpw.setError(null);

        String newPassword = etnewpw.getText().toString();
        String confirmPassword = etconfirmpw.getText().toString();

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

        attemptoChange();
    }

    private void attemptoChange() {

        final ChangePW changePWModel = new ChangePW();

        final ProgressDialog pd = DM.getPD(ChangePassowrd.this, "Loading for Changing Password...");
        pd.show();

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

