package com.project.mit.user;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.project.mit.pages.Home;
import com.project.mit.pages.MainActivity;
import com.project.mit.R;
import com.project.mit.session.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

public class Login extends AppCompatActivity {

    private static final String SIGN_IN_URL = "http://hawkingnight.com/projectmit/API/GetUserEmail.php?";

    private SessionManager sessionManager;

    EditText EmailField, PasswordField;
    Button ButtonSignIn;

    private void ToolbarSettings(){
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar_sign_in);

        View view = getSupportActionBar().getCustomView();
        ImageButton BackButton = view.findViewById(R.id.backButton);

        BackButton.setOnClickListener(v -> {
            Intent IntentBack = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(IntentBack);
        });

    }
    private void Declare(){
        EmailField = findViewById(R.id.EmailField);
        PasswordField = findViewById(R.id.PasswordField);
        ButtonSignIn = findViewById(R.id.ButtonSignIn);

        sessionManager = new SessionManager(getApplicationContext());
    }
    private void MethodSettings(){
        ButtonSignIn.setOnClickListener(v -> SignIn());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);

        ToolbarSettings();
        Declare();
        MethodSettings();
    }

    private void SignIn(){
        String Email = EmailField.getText().toString();
        String Password = PasswordField.getText().toString();

        String SIGN_IN_API = SIGN_IN_URL + "EmailAddress=" + Email + "&Password=" + Password;

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