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
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.project.mit.models.User;
import com.project.mit.pages.Home;
import com.project.mit.pages.MainActivity;
import com.project.mit.R;
import com.project.mit.session.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

public class Login extends AppCompatActivity {
    private SessionManager sessionManager;

    EditText EmailField, PasswordField;
    Button ButtonSignIn;

    User user;

    private void ToolbarSettings(){
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar_sign_in);

        View view = getSupportActionBar().getCustomView();
        TextView TitleText = view.findViewById(R.id.action_bar_title);
        ImageButton BackButton = view.findViewById(R.id.backButton);

        TitleText.setText(R.string.sign_in);

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
        user = new User();
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

        String SIGN_IN_API = user.getUserEmailSingle + "EmailAddress=" + Email + "&Password=" + Password;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, SIGN_IN_API, response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                String UID = jsonObject.getString(user.UID);
                String FirstName = jsonObject.getString(user.FirstName);
                String LastName = jsonObject.getString(user.LastName);
                String ProfilePicture = jsonObject.getString(user.ProfilePicture);
                String Birthday = jsonObject.getString(user.Birthday);
                String EmailAddress = jsonObject.getString(user.EmailAddress);
                String PhoneNo = jsonObject.getString(user.PhoneNo);
                String Address01 = jsonObject.getString(user.Address01);
                String Address02 = jsonObject.getString(user.Address02);
                String City = jsonObject.getString(user.City);
                String State = jsonObject.getString(user.State);
                String PostCode = jsonObject.getString(user.Postcode);

                sessionManager.createSession(UID, FirstName, LastName, ProfilePicture, Birthday, EmailAddress, PhoneNo, Address01, Address02, City, State, PostCode);

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