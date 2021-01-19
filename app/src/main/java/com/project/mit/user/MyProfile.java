package com.project.mit.user;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.project.mit.R;
import com.project.mit.session.SessionManager;

import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;

public class MyProfile extends AppCompatActivity {

    private static String API_UPDATE_USER = "http://hawkingnight.com/projectmit/API/UpdateUser.php";

    ImageView UserImage;
    EditText FirstNameField, LastNameField, BirthdayField,
            EmailField, PhoneNoField, Address01Field,
            Address02Field, CityField, StateField, PostCodeField;
    Button ButtonSaved;

    String getUID, getFirstName, getLastName, getBirthday, getEmail, getPhoneNo, getAddress01, getAddress02, getCity, getState, getPostCode;
    SessionManager sessionManager;
    DatePickerDialog datePickerDialog;

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
    }
    private void getSession(){
        sessionManager = new SessionManager(getApplicationContext());
        sessionManager.checkLogin();

        HashMap<String, String> UserDetails = sessionManager.getUserDetail();
        getUID = UserDetails.get(SessionManager.UID);
        getFirstName = UserDetails.get(SessionManager.FIRSTNAME);
        getLastName = UserDetails.get(SessionManager.LASTNAME);
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
    }

    private void BirthdaySettings(){
        Calendar calendar = Calendar.getInstance();
        int Day = calendar.get(Calendar.DAY_OF_MONTH);
        int Month = calendar.get(Calendar.MONTH);
        int Year = calendar.get(Calendar.YEAR);

        datePickerDialog = new DatePickerDialog(getApplicationContext(), R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String Date = dayOfMonth + "/" + (month + 1) + "/" + year;
                BirthdayField.setText(Date);
            }
        }, Year, Month, Day);
        datePickerDialog.show();
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
        parameters.put("UID", getUID);
        parameters.put("FirstName", FirstName);
        parameters.put("LastName", LastName);
        parameters.put("Birthday", Birthday);
        parameters.put("EmailAddress", EmailAddress);
        parameters.put("PhoneNo", PhoneNo);
        parameters.put("Address01", Address01);
        parameters.put("Address02", Address02);
        parameters.put("City", City);
        parameters.put("State",State);
        parameters.put("Postcode", Postcode);

        JsonObjectRequest request_json = new JsonObjectRequest(API_UPDATE_USER, new JSONObject(parameters),
                response -> Log.i("RESPONSE", "SUCCESS!"),
                error -> Log.i("ERORR", error.toString()));

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request_json);
    }
}
