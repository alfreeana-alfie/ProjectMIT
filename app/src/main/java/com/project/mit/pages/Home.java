package com.project.mit.pages;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.project.mit.R;
import com.project.mit.adapter.RecordAdapter;
import com.project.mit.details.RecordDetails;
import com.project.mit.models.Location;
import com.project.mit.models.Record;
import com.project.mit.models.User;
import com.project.mit.session.SessionManager;
import com.project.mit.user.MyProfile;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@SuppressLint({"SetTextI18n","NonConstantResourceId"})
public class Home extends AppCompatActivity {
    private IntentIntegrator QRScanner;

    ImageView UserImage;
    TextView FullNameText, EmailAddressText;
    BottomNavigationView bottomNavigationView;
    FloatingActionButton CameraOpen;

    RecyclerView RecordView;
    List<RecordDetails> recordDetailsList;
    RecordAdapter recordAdapter;

    String getUID, getImage, getFirstName, getLastName, getEmail;
    SessionManager sessionManager;

    User user;
    Record record;
    Location location;

    private void ToolbarSettings(){
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar_home);
    }
    private void Declare(){
        UserImage = findViewById(R.id.UserImage);
        FullNameText = findViewById(R.id.FullNameText);
        EmailAddressText = findViewById(R.id.EmailAddressText);
        bottomNavigationView = findViewById(R.id.bottom_nav);
        CameraOpen = findViewById(R.id.CameraOpen);
        RecordView = findViewById(R.id.RecordView);
        RecordView.setHasFixedSize(true);
        RecordView.setLayoutManager(new LinearLayoutManager(this));
        RecordView.setNestedScrollingEnabled(false);

        recordDetailsList = new ArrayList<>();
        QRScanner = new IntentIntegrator(this);

        user = new User();
        record = new Record();
        location = new Location();
    }
    private void getSession(){
        sessionManager = new SessionManager(getApplicationContext());
        sessionManager.checkLogin();

        HashMap<String, String> UserDetails = sessionManager.getUserDetail();
        getUID = UserDetails.get(SessionManager.UID);
        getImage = UserDetails.get(SessionManager.PROFILE_PICTURE);
        getFirstName = UserDetails.get(SessionManager.FIRSTNAME);
        getLastName = UserDetails.get(SessionManager.LASTNAME);
        getEmail = UserDetails.get(SessionManager.EMAIL);
    }
    private void MethodSettings(){
        Log.i("IMAGE", getImage);
        Picasso.get().load(getImage).into(UserImage);
        FullNameText.setText(getFirstName + " " + getLastName);
        EmailAddressText.setText(getEmail);

        CameraOpen.setOnClickListener(v -> QRScanner.initiateScan());
    }
    private void BottomNav(){
        bottomNavigationView.setBackground(null);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.home:
                    Log.i("USER", "HOME");
                    break;

                case R.id.user:
                    Intent IntentUser = new Intent(getApplicationContext(), MyProfile.class);
                    startActivity(IntentUser);
                    break;
            }
            return true;
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        ToolbarSettings();
        Declare();
        getSession();
        MethodSettings();
        BottomNav();

        GetHistoryList();
    }

    private void GetHistoryList(){
        String SIGN_IN_API = record.GetRecordUser + "UserID=" + getUID;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, SIGN_IN_API, response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONArray jsonArray = jsonObject.getJSONArray("body");

                for(int i = 0; i<jsonArray.length();i++){
                    JSONObject object = jsonArray.getJSONObject(i);

                    String RecordID = object.getString(record.RecordID);
                    String LocationID = object.getString(record.LocationID);
                    String LocationName = object.getString(record.LocationName);
                    String LocationFullAddress = object.getString(record.LocationFullAddress);
                    String RiskStatus = object.getString(record.RiskStatus);
                    String ZoneStatus = object.getString(record.ZoneStatus);
                    String CreatedDateTime = object.getString(record.CreatedDateTime);

                    Log.i("USER", RecordID);
                    RecordDetails recordDetails = new RecordDetails(RecordID, getUID,
                            LocationID, LocationName,
                            LocationFullAddress,RiskStatus,
                            ZoneStatus, CreatedDateTime);
                    recordDetailsList.add(recordDetails);
                }
                recordAdapter = new RecordAdapter(getApplicationContext(), recordDetailsList);
                RecordView.setAdapter(recordAdapter);

            } catch (JSONException e) {
                e.printStackTrace();
                Log.i("USER", e.toString());
            }
        }, error -> Log.i("USER", error.toString()));

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                try {
                    JSONObject obj = new JSONObject(result.getContents());

                    String LocationID = obj.getString(location.LocationID);
                    String LocationName = obj.getString(location.LocationName);
                    String Address01 = obj.getString(location.Address01);
                    String Address02 = obj.getString(location.Address02);
                    String City = obj.getString(location.City);
                    String State = obj.getString(location.State);
                    String Postcode = obj.getString(location.Postcode);
                    String RiskStatus = obj.getString(location.RiskStatus);
                    String ZoneStatus = obj.getString(location.ZoneStatus);

                    String LocationFullAddress = Address01 + ", " + Address02 + ", " + Postcode + ", " + City + ", " + State;

                    HashMap<String, String> parameters = new HashMap<>();
                    parameters.put(record.UserID, getUID);
                    parameters.put(record.LocationID, LocationID);
                    parameters.put(record.LocationName, LocationName);
                    parameters.put(record.LocationFullAddress, LocationFullAddress);
                    parameters.put(record.RiskStatus, RiskStatus);
                    parameters.put(record.ZoneStatus, ZoneStatus);

                    JsonObjectRequest request_json = new JsonObjectRequest(record.CreateRecord, new JSONObject(parameters),
                            response -> Log.i("RESPONSE", "SUCCESS!"),
                            error -> Log.i("ERORR", error.toString()));
                    RequestQueue requestQueue = Volley.newRequestQueue(this);
                    requestQueue.add(request_json);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
