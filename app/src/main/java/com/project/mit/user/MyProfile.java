package com.project.mit.user;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.se.omapi.Session;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.project.mit.R;
import com.project.mit.models.User;
import com.project.mit.session.SessionManager;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;

public class MyProfile extends AppCompatActivity {
    User user;

    ImageView UserImage;
    EditText FirstNameField, LastNameField, BirthdayField,
            EmailField, PhoneNoField, Address01Field,
            Address02Field, CityField, StateField, PostCodeField;
    Button ButtonSaved;

    String getUID, getFirstName, getLastName,getImage, getBirthday, getEmail, getPhoneNo, getAddress01, getAddress02, getCity, getState, getPostCode;
    SessionManager sessionManager;
    DatePickerDialog datePickerDialog;
    private Bitmap bitmap;
    public Uri filePath;

    private void Declare(){
        UserImage = findViewById(R.id.UserImage);
        FirstNameField = findViewById(R.id.FirstNameField);
        LastNameField = findViewById(R.id.LastNameField);
        BirthdayField = findViewById(R.id.BirthdayField);
        EmailField = findViewById(R.id.EmailField);
        PhoneNoField = findViewById(R.id.PhoneField);
        Address01Field = findViewById(R.id.Address01Field);
        Address02Field = findViewById(R.id.Address02Field);
        CityField = findViewById(R.id.CityField);
        StateField = findViewById(R.id.StateField);
        PostCodeField = findViewById(R.id.PostCodeField);
        ButtonSaved = findViewById(R.id.ButtonSave);

        user = new User();
    }
    private void getSession(){
        sessionManager = new SessionManager(getApplicationContext());
        sessionManager.checkLogin();

        HashMap<String, String> UserDetails = sessionManager.getUserDetail();
        getUID = UserDetails.get(SessionManager.UID);
        getFirstName = UserDetails.get(SessionManager.FIRSTNAME);
        getLastName = UserDetails.get(SessionManager.LASTNAME);
        getImage = UserDetails.get(SessionManager.PROFILE_PICTURE);
        getBirthday = UserDetails.get(SessionManager.BIRTHDAY);
        getEmail = UserDetails.get(SessionManager.EMAIL);
        getPhoneNo = UserDetails.get(SessionManager.PHONE_NO);
        getAddress01 = UserDetails.get(SessionManager.ADDRESS01);
        getAddress02 = UserDetails.get(SessionManager.ADDRESS02);
        getCity = UserDetails.get(SessionManager.CITY);
        getState = UserDetails.get(SessionManager.STATE);
        getPostCode = UserDetails.get(SessionManager.POSTCODE);
    }
    private void MethodSettings(){
        FirstNameField.setText(getFirstName);
        LastNameField.setText(getLastName);
        BirthdayField.setText(getBirthday);
        EmailField.setText(getEmail);
        PhoneNoField.setText(getPhoneNo);
        Address01Field.setText(getAddress01);
        Address02Field.setText(getAddress02);
        CityField.setText(getCity);
        StateField.setText(getState);
        PostCodeField.setText(getPostCode);

        ButtonSaved.setOnClickListener(v -> SaveData());
        BirthdayField.setOnClickListener(v -> BirthdaySettings());
        UserImage.setOnClickListener(v -> chooseFile());

        Picasso.get().load(getImage).into(UserImage);
    }
    private void BirthdaySettings(){
        Calendar calendar = Calendar.getInstance();
        int Day = calendar.get(Calendar.DAY_OF_MONTH);
        int Month = calendar.get(Calendar.MONTH);
        int Year = calendar.get(Calendar.YEAR);
        closeKeyboard();
        datePickerDialog = new DatePickerDialog(MyProfile.this, R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String Date = dayOfMonth + "/" + (month + 1) + "/" + year;
                BirthdayField.setText(Date);

            }
        }, Year, Month, Day);
        datePickerDialog.show();
    }
    private void chooseFile(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
    }

    public String getStringImage(Bitmap bitmap11) {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap11.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);

        byte[] imageByteArray = byteArrayOutputStream.toByteArray();

        return Base64.encodeToString(imageByteArray, Base64.DEFAULT);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile);
        Declare();
        getSession();
        MethodSettings();
    }

    private void SaveData(){
        String FirstName = FirstNameField.getText().toString();
        String LastName = LastNameField.getText().toString();
        String Birthday = BirthdayField.getText().toString();
        String EmailAddress = EmailField.getText().toString();
        String PhoneNo = PhoneNoField.getText().toString();
        String Address01 = Address01Field.getText().toString();
        String Address02 = Address02Field.getText().toString();
        String City = CityField.getText().toString();
        String State = StateField.getText().toString();
        String Postcode = PostCodeField.getText().toString();

        HashMap<String, String> parameters = new HashMap<>();
        parameters.put(user.UID, getUID);
        parameters.put(user.FirstName, FirstName);
        parameters.put(user.LastName, LastName);
        parameters.put(user.ProfilePicture, getStringImage(bitmap));
        parameters.put(user.Birthday, Birthday);
        parameters.put(user.EmailAddress, EmailAddress);
        parameters.put(user.PhoneNo, PhoneNo);
        parameters.put(user.Address01, Address01);
        parameters.put(user.Address02, Address02);
        parameters.put(user.City, City);
        parameters.put(user.State,State);
        parameters.put(user.Postcode, Postcode);

        JsonObjectRequest request_json = new JsonObjectRequest(user.updateUser, new JSONObject(parameters),
                response -> Log.i("RESPONSE", "SUCCESS!"),
                error -> Log.i("ERORR", error.toString()));

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request_json);
    }

    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                bitmap = Bitmap.createScaledBitmap(bitmap, 300, 300, false);
                UserImage.setImageBitmap(bitmap);
                Log.i("IMAGE", getStringImage(bitmap));
                Log.i("IMAGE", String.valueOf(bitmap));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
