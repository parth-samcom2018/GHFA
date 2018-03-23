package com.horsnby.gladesvillehorsnby;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.horsnby.gladesvillehorsnby.models.Register;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;
import java.util.Vector;

import de.hdodenhof.circleimageview.CircleImageView;
import info.hoang8f.android.segmented.SegmentedGroup;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;
import retrofit.mime.TypedFile;

public class Registration extends BaseVC implements AdapterView.OnItemClickListener{


    static final int REQUEST_TAKE_PHOTO = 1;
    static final int REQUEST_PICK_IMAGE = 2;

    public static final String MYPref = "Pref";
    public static final String ALLOW_KEY = "ALLOWED";
    public static final String CAMERA_PREF = "camera_pref";
    String email1;

    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 100;
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 100;
    private static final int MY_PERMISSIONS_REQUEST_READ_PHONE = 100;
    private static final int MY_PERMISSIONS_REQUEST_EXTERNAL_STORAGE = 100;

    boolean isSelected;

    //VIEWS
    private CircleImageView profileIV;
    private Button chooseImageButton;

    private EditText emailET;
    private EditText passwordET;
    private EditText passwordConfirmET;
    private EditText firstNameET;
    private EditText surnameET;
    private SegmentedGroup genderSG;
    private RadioButton buttonSG1;
    private RadioButton buttonSG2;
    private EditText birthYearET;
    private EditText countryET;

    private EditText postCodeET;
    private Button chooseCountryButton;
    SharedPreferences sPref;
    private Switch termsSwitch;

    String[] country = { "Select Country","India", "USA", "China", "Japan", "Other",  };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        setContentView(R.layout.activity_register);

        sPref = getSharedPreferences(MYPref, MODE_PRIVATE);

        emailET = findViewById(R.id.email);
        passwordET = findViewById(R.id.password);
        passwordConfirmET = findViewById(R.id.password_confirm);
        firstNameET = findViewById(R.id.firstNameET);
        surnameET = findViewById(R.id.surnameET);

        genderSG = findViewById(R.id.genderSG);
        buttonSG1 = findViewById(R.id.buttonsg1);
        buttonSG2 = findViewById(R.id.buttonsg2);

        buttonSG1.setChecked(true);
        genderSG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (buttonSG1.isChecked()){
                    buttonSG1.setChecked(true);
                    return;
                }
                if (buttonSG2.isChecked()){
                    buttonSG2.setChecked(true);
                    return;
                }
            }
        });


        birthYearET = findViewById(R.id.birthYearET);
        countryET = findViewById(R.id.countryET);
        Spinner spin = findViewById(R.id.spinner1);

        Locale[] locales = Locale.getAvailableLocales();
        ArrayList<String> countries = new ArrayList<String>();
        for (Locale locale : locales) {
            String country = locale.getDisplayCountry();
            if (country.trim().length() > 0 && !countries.contains(country)) {
                countries.add(country);
            }
        }
        Collections.sort(countries);
        for (String country : countries) {
            System.out.println(country);
        }

        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,countries);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spin.setAdapter(aa);

        //countryET.setEnabled(false);


        chooseCountryButton = findViewById(R.id.chooseCountryButton);

        postCodeET = findViewById(R.id.postCodeET);

        postCodeET.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b){
                    hideKeyBoard(view);
                }
            }
        });

        termsSwitch = findViewById(R.id.termsSwitch);

        passwordConfirmET.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.register || id == EditorInfo.IME_NULL) {
                    registerAction1();
                    return true;
                }
                return false;
            }
        });


        Button registerButton = findViewById(R.id.register_button);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showAlert();
            }
        });

        Button termsButton = findViewById(R.id.termsButton);
        termsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                termsAction();
            }
        });

        Button backToLoginButton = findViewById(R.id.login_button);
        backToLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginAction();
            }
        });


        termsSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                switchOn = isChecked;
            }
        });

    }


    private void hideKeyBoard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }



    private boolean switchOn = false;

    private void registerAction1(){

        isOnline();

        // Reset errors.
        emailET.setError(null);
        passwordET.setError(null);
        passwordConfirmET.setError(null);
        firstNameET.setError(null);
        surnameET.setError(null);
        birthYearET.setError(null);
        countryET.setError(null);



        // Store values at the time of the login attempt.
        final String email = emailET.getText().toString();
        String password = passwordET.getText().toString();
        String passwordConfirm = passwordConfirmET.getText().toString();
        String firstName = firstNameET.getText().toString();
        String lastName = surnameET.getText().toString();
        String birthday = birthYearET.getText().toString();
        String postCode = postCodeET.getText().toString();
        String countryCode = countryET.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(email) || !DM.isEmailValid(email)){
            Toast.makeText(this,"Enter an Email ID", Toast.LENGTH_LONG).show();
            emailET.requestFocus();
            emailET.setFocusable(true);
            focusView = emailET;
            cancel = true;
            return;
        }

        if (!email.matches("[a-zA-Z0-9._-]+@[a-z]+.[a-z]+")) {

            Toast.makeText(this, "Invalid Email ID", Toast.LENGTH_SHORT).show();
            emailET.requestFocus();
            emailET.setFocusable(true);
            focusView = emailET;
            cancel = true;
            return;
        }

        if (TextUtils.isEmpty(password)){
            Toast.makeText(this,"Enter Password", Toast.LENGTH_LONG).show();
            passwordET.requestFocus();
            passwordET.setFocusable(true);
            focusView = passwordET;
            cancel = true;
            return;
        }


        if (password.length()<6){
            passwordET.requestFocus();
            passwordET.setFocusable(true);
            Toast.makeText(this, "Password is too Short! Atleast 6 character required", Toast.LENGTH_SHORT).show();
            return;
        }



        if (TextUtils.isEmpty(passwordConfirm)){
            Toast.makeText(this,"Enter Confirm Password", Toast.LENGTH_LONG).show();
            passwordConfirmET.requestFocus();
            passwordConfirmET.setFocusable(true);
            focusView = passwordConfirmET;
            cancel = true;
            return;
        }

        if (!passwordET.getText().toString().matches(passwordConfirmET.getText().toString())){
            Toast.makeText(this,"Password doesn't match", Toast.LENGTH_LONG).show();
            passwordConfirmET.requestFocus();
            passwordConfirmET.setFocusable(true);
            return;
        }

        if (TextUtils.isEmpty(firstName)) {

            Toast.makeText(this,"Enter a first name", Toast.LENGTH_LONG).show();
            firstNameET.requestFocus();
            firstNameET.setFocusable(true);
            focusView = firstNameET;
            cancel = true;
            return;
        }

        if (TextUtils.isEmpty(lastName)){
            Toast.makeText(this, "Enter a last name", Toast.LENGTH_SHORT).show();
            surnameET.requestFocus();
            surnameET.setFocusable(true);
            focusView = surnameET;
            cancel = true;
            return;
        }

        if (TextUtils.isEmpty(birthday)){
            Toast.makeText(this,"Enter Birthday Year", Toast.LENGTH_LONG).show();
            birthYearET.requestFocus();
            birthYearET.setFocusable(true);
            focusView = birthYearET;
            cancel = true;
            return;
        }

        if (TextUtils.isEmpty(countryCode)){
            Toast.makeText(this, "Select Country", Toast.LENGTH_SHORT).show();
            countryET.requestFocus();
            countryET.setFocusable(true);
            return;
        }


        if (TextUtils.isEmpty(postCode)){
            Toast.makeText(this,"Enter PostCode without using + character", Toast.LENGTH_LONG).show();
            postCodeET.requestFocus();
            postCodeET.setFocusable(true);
            focusView = postCodeET;
            cancel = true;
            return;
        }

        if(!switchOn)
        {
            Toast.makeText(this,"You must accept the terms first", Toast.LENGTH_LONG).show();
            return;
        }

        if (genderSG.getCheckedRadioButtonId() == -1)
        {
            Toast.makeText(this, "Select Gender", Toast.LENGTH_SHORT).show();
            return;
        }
        else
        {
            int selectedId = genderSG.getCheckedRadioButtonId();
            buttonSG1 = findViewById(selectedId);
            //Toast.makeText(getApplicationContext(), buttonSG1.getText().toString()+" is selected", Toast.LENGTH_SHORT).show();

        }


        if (cancel) {

            focusView.requestFocus();
        } else {


            final Register registerModel = new Register();
            registerModel.email = email;
            registerModel.password = password;
            registerModel.confirmPassword = passwordConfirm;
            registerModel.firstname = firstNameET.getText().toString();
            registerModel.surname = surnameET.getText().toString();

            registerModel.gender = "U"; //unknown
            int checkedID = genderSG.getCheckedRadioButtonId();


            if (buttonSG1.getId() == checkedID) registerModel.gender = "M";
            if (buttonSG2.getId() == checkedID) registerModel.gender = "F";

            registerModel.birthYear = birthYearET.getText().toString();
            registerModel.country = countryET.getText().toString();
            registerModel.postCode = postCodeET.getText().toString();


            DM.getApi().getInvitedGrouping(registerModel.email, new Callback<Response>() {
                @Override
                public void success(Response response, Response response2) {
                    String r = response.getBody().toString();
                    Log.d("HQ", "group invite response: " + r);
                    if (r.equals("true")) {
                        //already invited to groups..
                        makeRegistrationRequest(registerModel);
                    } else {

                        String name = "Gladesville Horsnby";
                        registerModel.GroupName = name;
                        makeRegistrationRequest(registerModel);

                        SharedPreferences preferences = getSharedPreferences(MYPref, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("autoSave", passwordConfirmET.getText().toString());
                        editor.apply();
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    Toast.makeText(Registration.this, "could not check groups", Toast.LENGTH_LONG).show();
                }
            });

        }

    }


    private void makeRegistrationRequest(final Register registerModel)
    {
        final ProgressDialog pd = DM.getPD(this, "Registering...");
        pd.show();

        DM.getApi().register(DM.getAuthString(), registerModel, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {

                Toast.makeText(Registration.this, "Registration success!", Toast.LENGTH_LONG).show();
                DM.hideKeyboard(Registration.this);
                pd.dismiss();


                //triggers auto login
                Login.justRegisteredUsername = registerModel.email;
                Login.justRegisteredPassword = registerModel.password;

                finish();

            }

            @Override
            public void failure(RetrofitError error) {

                pd.dismiss();

                String s =  new String(((TypedByteArray)error.getResponse().getBody()).getBytes());
                Log.d("HQ",s);


                if(s.contains("already in use"))
                {

                    Toast.makeText(Registration.this, "User already registered, please try a different email", Toast.LENGTH_LONG).show();
                }
                else
                {

                    Toast.makeText(Registration.this, "Registration failed: "+error.getMessage(), Toast.LENGTH_LONG).show();
                }



            }
        });

    }


    private void showAlert() {
        if (isSelected ==true){
            AlertDialog alertDialog = new AlertDialog.Builder(Registration.this).create();
            alertDialog.setTitle("Alert");
            alertDialog.setMessage("App needs to access the Camera.");
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "DONT ALLOW",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            finish();
                        }
                    });
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "ALLOW",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            ActivityCompat.requestPermissions(Registration.this,
                                    new String[]{Manifest.permission.CAMERA},
                                    MY_PERMISSIONS_REQUEST_CAMERA);
                            ActivityCompat.requestPermissions(Registration.this,
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                    MY_PERMISSIONS_REQUEST_LOCATION);
                            ActivityCompat.requestPermissions(Registration.this,
                                    new String[]{Manifest.permission.READ_PHONE_STATE},
                                    MY_PERMISSIONS_REQUEST_READ_PHONE);
                            ActivityCompat.requestPermissions(Registration.this,
                                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                    MY_PERMISSIONS_REQUEST_EXTERNAL_STORAGE);

                            saveToPreferences(Registration.this, ALLOW_KEY, true);
                        }
                    });
            alertDialog.show();
        }

        else {

            registerAction1();
        }

    }


    public static void saveToPreferences(Context context, String key,
                                         Boolean allowed) {
        SharedPreferences myPrefs = context.getSharedPreferences
                (CAMERA_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = myPrefs.edit();
        prefsEditor.putBoolean(key, allowed);
        prefsEditor.commit();
    }

    private void termsAction()
    {

        WebVC.url = "http://www.sportsclubhq.com/standard-terms-of-use.html";
        WebVC.title = "Terms";
        Intent i = new Intent(this, WebVC.class);
        startActivity(i);
    }

    private void loginAction() //goes back to login screen
    {
        this.finish();
    }



    private Uri capturedImageUri;
    String mCurrentPhotoPath;
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();

        capturedImageUri = Uri.fromFile(image);
        return image;
    }


    private String imgDecodableString;
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        Bitmap b = null;

        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == Activity.RESULT_OK) {
            // Bundle extras = data.getExtras();
            //Bitmap imageBitmap = (Bitmap) extras.get("data");

            Log.d("hq", "request take photo");
            try {
                b = MediaStore.Images.Media.getBitmap(this.getApplicationContext().getContentResolver(), capturedImageUri);
                Log.d("hipcook", "I now have a photo bitmap:" + b.getWidth());
                float scaleFactor = 640f / b.getWidth();
                b = DM.createScaledBitmap(b, scaleFactor);
                Log.d("hipcook", "I now have a scaled photo bitmap:" + b.getWidth());


            } catch (IOException e) {
                e.printStackTrace();
                Log.d("hipcook", "bitmap exception");
            }
        } else if (requestCode == REQUEST_PICK_IMAGE &&
                resultCode == Activity.RESULT_OK &&
                null != data) {
            // Get the Image from data

            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            // Get the cursor
            Cursor cursor = this.getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            imgDecodableString = cursor.getString(columnIndex);
            cursor.close();

            b = DM.decodeSampledBitmapFromFile(imgDecodableString, 640, 640);
            Log.d("hipcook", "I now have a bitmap:" + b.getWidth());


        } else {
            Toast.makeText(this, "You haven't picked Image", Toast.LENGTH_LONG).show();
        }

        //if found a bitmap, upload or save in registration model
        if (b != null) {

            profileIV.setImageBitmap(b);
            newProfileImage = b;
        }
    }

    private Bitmap newProfileImage = null;
    private void postImagefromImageView()
    {
        if(newProfileImage == null) return; //no need to upload
        final Bitmap b = newProfileImage;

        final ProgressDialog pd = DM.getPD(this, "Updating Profile Image...");
        pd.show();

        String fileName = "photo.png";
        File f = new File(this.getCacheDir(), fileName);
        try {
            f.createNewFile();
            //Convert bitmap to byte array

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            b.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
            byte[] bitmapdata = bos.toByteArray();

            //write the bytes in file
            FileOutputStream fos = new FileOutputStream(f);
            fos.write(bitmapdata);
            fos.flush();
            fos.close();


            TypedFile typedImage = new TypedFile("image/png", f);
            DM.getApi().postProfileImage(DM.getAuthString(), typedImage, new Callback<Response>() {
                @Override
                public void success(Response response, Response response2) {
                    Toast.makeText(Registration.this, "Profile Image Updated", Toast.LENGTH_LONG).show();
                    pd.hide();
                }

                @Override
                public void failure(RetrofitError error) {

                    Toast.makeText(Registration.this, "Could not update profile image: " + error.getMessage(), Toast.LENGTH_LONG).show();
                    pd.hide();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("hq", "file exception");
        }

    }


    public boolean isOnline() {

        ConnectivityManager connec =
                (ConnectivityManager)getSystemService(getBaseContext().CONNECTIVITY_SERVICE);

        if ( connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED ) {

            return true;

        } else if (
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
                        connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED  ) {

            Toast.makeText(Registration.this, "Internet is not Connected! ", Toast.LENGTH_LONG).show();
            return false;
        }
        return false;
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Toast.makeText(this, country[i], Toast.LENGTH_SHORT).show();
    }
}
