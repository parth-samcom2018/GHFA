package com.horsnby.gladesvillehorsnby;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.horsnby.gladesvillehorsnby.models.Profile;

import info.hoang8f.android.segmented.SegmentedGroup;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ProfileFragment extends Fragment {

    private EditText emailET;
    private EditText firstNameET;
    private EditText surnameET;
    private SegmentedGroup genderSG;
    private RadioButton buttonSG1;
    private RadioButton buttonSG2;
    private EditText birthYearET;
    private EditText countryET;
    private EditText postCodeET;

    private TextView usernameTV;
    private Button logoutButton;


    public void ProfileFragment() {

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {


        inflater.inflate(R.menu.update_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        //theres only create
        if (item.getItemId() == R.id.update) this.updateAction();
        return super.onOptionsItemSelected(item);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setHasOptionsMenu(true);
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_profile, container, false);


        emailET = v.findViewById(R.id.email);
        firstNameET = v.findViewById(R.id.firstNameET);
        surnameET = v.findViewById(R.id.surnameET);

        genderSG = v.findViewById(R.id.genderSG);
        buttonSG1 = v.findViewById(R.id.buttonsg1);
        buttonSG2 = v.findViewById(R.id.buttonsg2);

        birthYearET = v.findViewById(R.id.birthYearET);

        birthYearET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                birthYearET.setFocusableInTouchMode(true);
                birthYearET.setFocusable(true);
                birthYearET.requestFocus();
                InputMethodManager bd = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                bd.showSoftInput(birthYearET, InputMethodManager.SHOW_IMPLICIT);
            }
        });
        countryET = v.findViewById(R.id.countryET);


        postCodeET = v.findViewById(R.id.postCodeET);

        //postCodeET.setImeOptions(EditorInfo.IME_ACTION_DONE);


        postCodeET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postCodeET.setFocusable(true);
                postCodeET.requestFocus();

            }
        });


        usernameTV = v.findViewById(R.id.usernameTV);

        logoutButton = v.findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutAction();
            }
        });


        DM.getApi().getMemberDetailing(DM.getAuthString(), new Callback<Profile>() {
            @Override
            public void success(Profile profilev2, Response response) {
                if (response.getStatus() == 200) {

                    //copy attributes over
                    DM.member.copyAttributesFromDetails(profilev2.getData());
                    modelToView();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(ProfileFragment.this.getContext(),
                        "Could not load member details:" + error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });


        return v;
    }

    private void modelToView() {


        emailET.setText(DM.member.username);
        //emailET.setEnabled(false);
        firstNameET.setText(DM.member.firstName);
        surnameET.setText(DM.member.lastName);
        countryET.setEnabled(false);

        if (DM.member.gender != null) {
            Log.d("hq", "gender: " + DM.member.gender);
            if (DM.member.gender.equalsIgnoreCase("M")) {
                //  buttonSG1.setSelected(true);
                genderSG.check(buttonSG1.getId());
            }
            if (DM.member.gender.equalsIgnoreCase("F")) {
                // buttonSG2.setSelected(true);
                genderSG.check(buttonSG2.getId());
            }
        }

        birthYearET.setText(DM.member.birthYear);
        //birthYearET.setEnabled(false);
        countryET.setText(DM.member.country);
        postCodeET.setText(DM.member.postCode);

        usernameTV.setText(DM.member.username);

    }

    private void logoutAction() {
        DM.member = null;

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ProfileFragment.this.getActivity());
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove("HQUsername");
        editor.remove("HQToken");
        editor.apply();

        unregisterForPush();

        getActivity().finish();

        Intent i = new Intent(ProfileFragment.this.getActivity(), Login.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }


    private void unregisterForPush() {/*try {
            FirebaseInstanceId.getInstance().deleteInstanceId();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        //TODO call api and unregister with device token


    }

    private void updateAction() {
        //view to model

        DM.member.firstName = firstNameET.getText().toString();
        DM.member.lastName = surnameET.getText().toString();

        DM.member.gender = "U"; //unknown


        int checkedID = genderSG.getCheckedRadioButtonId();
        if (buttonSG1.getId() == checkedID) DM.member.gender = "M";
        if (buttonSG2.getId() == checkedID) DM.member.gender = "F";
        // DM.member.birthYear = birthYearET.getText().toString();


        final ProgressDialog pd = DM.getPD(this.getActivity(), "Updating Profile...");
        pd.show();


        DM.member.birthYear = birthYearET.getText().toString();
        DM.member.country = countryET.getText().toString();
        DM.member.postCode = postCodeET.getText().toString();

        DM.getApi().putMember(DM.getAuthString(), DM.member, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                Toast.makeText(ProfileFragment.this.getActivity(), "User Update success!", Toast.LENGTH_LONG).show();
                DM.hideKeyboard(ProfileFragment.this.getActivity());
                pd.dismiss();

            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(ProfileFragment.this.getActivity(), "could not update", Toast.LENGTH_LONG).show();
                DM.hideKeyboard(ProfileFragment.this.getActivity());
                pd.dismiss();

            }
        });


    }
}
