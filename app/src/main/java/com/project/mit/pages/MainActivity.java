package com.project.mit.pages;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.project.mit.R;
import com.project.mit.session.SessionManager;
import com.project.mit.user.Login;
import com.project.mit.user.Register;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private static final String SIGN_IN_URL = "http://hawkingnight.com/projectmit/API/GetUserSingle.php?";

    private SessionManager sessionManager;
    private String getUID;

    Button ButtonSignIn, ButtonSignUp;

    private void Declare(){
        ButtonSignIn = findViewById(R.id.ButtonSignIn);
        ButtonSignUp = findViewById(R.id.ButtonSignUp);
    }
    private void getSession(){
        sessionManager = new SessionManager(getApplicationContext());
        sessionManager.checkLogin();

        HashMap<String, String> UserDetails = sessionManager.getUserDetail();
        getUID = UserDetails.get(SessionManager.UID);
    }
    private void MethodSettings(){
        ButtonSignIn.setOnClickListener(v -> {
            Intent IntentSignIn = new Intent(getApplicationContext(), Login.class);
            startActivity(IntentSignIn);
        });

        ButtonSignUp.setOnClickListener(v -> {
            Intent IntentSignUp = new Intent(getApplicationContext(), Register.class);
            startActivity(IntentSignUp);
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        Declare();
        getSession();
        MethodSettings();

        if(sessionManager.isLoggin()){
            SignIn();
        }
    }

    private void SignIn(){
        String SIGN_IN_API = SIGN_IN_URL + "UID=" + getUID;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, SIGN_IN_API, response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                String UID = jsonObject.getString("UID");
                String FirstName = jsonObject.getString("FirstName");
                String LastName = jsonObject.getString("LastName");
                String Birthday = jsonObject.getString("Birthday");
                String EmailAddress = jsonObject.getString("EmailAddress");
                String PhoneNo = jsonObject.getString("PhoneNo");
                String Address01 = jsonObject.getString("Address01");
                String Address02 = jsonObject.getString("Address02");
                String City = jsonObject.getString("City");
                String State = jsonObject.getString("State");
                String PostCode = jsonObject.getString("Postcode");

                sessionManager.createSession(UID, FirstName, LastName, Birthday, EmailAddress, PhoneNo, Address01, Address02, City, State, PostCode);

                Intent IntentHome = new Intent(getApplicationContext(), Home.class);
                startActivity(IntentHome);
            } catch (JSONException e) {
                e.printStackTrace();
                Log.i("USER", e.toString());
            }
        }, error -> Log.i("USER", error.toString()));

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
