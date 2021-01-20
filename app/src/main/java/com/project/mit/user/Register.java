package com.project.mit.user;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.project.mit.models.User;
import com.project.mit.pages.MainActivity;
import com.project.mit.R;

import org.json.JSONObject;

import java.util.HashMap;

public class Register extends AppCompatActivity {

    EditText FirstNameField, LastNameField, EmailField, PasswordField, ConfirmPasswordField;
    Button ButtonSignUp;
    User user;

    private void Declare(){
        FirstNameField = findViewById(R.id.FirstNameField);
        LastNameField = findViewById(R.id.LastNameField);
        EmailField = findViewById(R.id.EmailField);
        PasswordField = findViewById(R.id.PasswordField);
        ConfirmPasswordField = findViewById(R.id.ConfirmPasswordField);

        ButtonSignUp = findViewById(R.id.ButtonSignUp);

        user = new User();
    }
    private void ToolbarSettings(){
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar_sign_in);

        View view = getSupportActionBar().getCustomView();
        TextView TitleText = view.findViewById(R.id.action_bar_title);
        ImageButton BackButton = view.findViewById(R.id.backButton);

        TitleText.setText(R.string.sign_up);

        BackButton.setOnClickListener(v -> {
            Intent IntentBack = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(IntentBack);
        });

    }
    private void MethodSettings(){
        ButtonSignUp.setOnClickListener(v -> SignUp());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_page);

        ToolbarSettings();
        Declare();
        MethodSettings();
    }

    private void SignUp(){
        String FirstName = FirstNameField.getText().toString();
        String LastName = LastNameField.getText().toString();
        String Email = EmailField.getText().toString();
        String Password = PasswordField.getText().toString();
        String ConfirmPassword = ConfirmPasswordField.getText().toString();

        HashMap<String, String> parameters = new HashMap<>();
        parameters.put(user.FirstName, FirstName);
        parameters.put(user.LastName, LastName);
        parameters.put(user.EmailAddress, Email);
        parameters.put(user.Password, Password);

        if(ConfirmPassword.equals(Password)){
            JsonObjectRequest request_json = new JsonObjectRequest(user.createUser, new JSONObject(parameters),
                    response -> Log.i("RESPONSE", "SUCCESS!"),
                    error -> Log.i("ERORR", error.toString()));
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(request_json);
        }else{
            Toast.makeText(getApplicationContext(), "Incorrect Password/Confirm Password", Toast.LENGTH_LONG).show();
        }

    }
}
