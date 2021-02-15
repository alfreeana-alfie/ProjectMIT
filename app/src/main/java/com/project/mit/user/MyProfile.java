package com.project.mit.user;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.project.mit.R;
import com.project.mit.models.User;
import com.project.mit.pages.Home;
import com.project.mit.pages.MainActivity;
import com.project.mit.session.SessionManager;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import static android.view.View.GONE;

public class MyProfile extends AppCompatActivity {
    User user;

    ImageView UserImage;
    EditText FirstNameField, LastNameField, BirthdayField,
            EmailField, PhoneNoField, Address01Field,
            Address02Field, CityField, StateField, PostCodeField;
    Button ButtonSaved, ButtonEdit, ButtonSignOut, ButtonCancel;
    ProgressBar Loading;
    RelativeLayout LoadingLayout;
    TextView FullNameDisplay, StateDisplay;

    String getUID, getFirstName, getLastName, getImage, getBirthday, getEmail, getPhoneNo, getAddress01, getAddress02, getCity, getState, getPostCode;
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
        ButtonEdit = findViewById(R.id.ButtonEditProfile);
        ButtonSignOut = findViewById(R.id.ButtonSignOut);
        ButtonCancel = findViewById(R.id.ButtonCancel);
        LoadingLayout = findViewById(R.id.LoadingLayout);

        FullNameDisplay = findViewById(R.id.FullNameDisplay);
        StateDisplay = findViewById(R.id.StateDisplay);

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
        LoadingLayout.setVisibility(GONE);
        ButtonSaved.setVisibility(GONE);
        ButtonCancel.setVisibility(GONE);

        FullNameDisplay.setText(getFirstName + " " + getLastName);
        StateDisplay.setText(getState + ", Malaysia");

        ButtonEdit.setOnClickListener(v -> EditProfileSettings());
        ButtonSaved.setOnClickListener(v -> SaveProfileSettings());
        ButtonCancel.setOnClickListener(v -> CancelSettings());
        ButtonSignOut.setOnClickListener(v -> SignOutSettings());
        BirthdayField.setOnClickListener(v -> BirthdaySettings());
        UserImage.setOnClickListener(v -> selectImage(MyProfile.this));

        Picasso.get().load(getImage).into(UserImage);
    }
    private void ToolbarSettings(){
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar_sign_in);

        View view = getSupportActionBar().getCustomView();
        TextView TitleText = view.findViewById(R.id.action_bar_title);
        ImageButton BackButton = view.findViewById(R.id.backButton);

        TitleText.setText(R.string.myprofile);

        BackButton.setOnClickListener(v -> {
            Intent IntentBack = new Intent(getApplicationContext(), Home.class);
            IntentBack.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(IntentBack);
        });

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
    private void EditProfileSettings(){
        FirstNameField.setFocusable(true);
        LastNameField.setFocusable(true);
        BirthdayField.setFocusable(true);
        EmailField.setFocusable(true);
        PhoneNoField.setFocusable(true);
        Address01Field.setFocusable(true);
        Address02Field.setFocusable(true);
        CityField.setFocusable(true);
        StateField.setFocusable(true);
        PostCodeField.setFocusable(true);
        UserImage.setFocusable(true);

        FirstNameField.setFocusableInTouchMode(true);
        LastNameField.setFocusableInTouchMode(true);
        BirthdayField.setFocusableInTouchMode(true);
        EmailField.setFocusableInTouchMode(true);
        PhoneNoField.setFocusableInTouchMode(true);
        Address01Field.setFocusableInTouchMode(true);
        Address02Field.setFocusableInTouchMode(true);
        CityField.setFocusableInTouchMode(true);
        StateField.setFocusableInTouchMode(true);
        PostCodeField.setFocusableInTouchMode(true);
        UserImage.setFocusableInTouchMode(true);

        ButtonEdit.setVisibility(GONE);
        ButtonSaved.setVisibility(View.VISIBLE);
        ButtonCancel.setVisibility(View.VISIBLE);
    }
    private void SaveProfileSettings(){
        FirstNameField.setFocusable(false);
        LastNameField.setFocusable(false);
        BirthdayField.setFocusable(false);
        EmailField.setFocusable(false);
        PhoneNoField.setFocusable(false);
        Address01Field.setFocusable(false);
        Address02Field.setFocusable(false);
        CityField.setFocusable(false);
        StateField.setFocusable(false);
        PostCodeField.setFocusable(false);
        UserImage.setFocusable(false);

        FirstNameField.setFocusableInTouchMode(false);
        LastNameField.setFocusableInTouchMode(false);
        BirthdayField.setFocusableInTouchMode(false);
        EmailField.setFocusableInTouchMode(false);
        PhoneNoField.setFocusableInTouchMode(false);
        Address01Field.setFocusableInTouchMode(false);
        Address02Field.setFocusableInTouchMode(false);
        CityField.setFocusableInTouchMode(false);
        StateField.setFocusableInTouchMode(false);
        PostCodeField.setFocusableInTouchMode(false);
        UserImage.setFocusableInTouchMode(false);

        ButtonEdit.setVisibility(View.VISIBLE);
        ButtonSaved.setVisibility(GONE);
        ButtonCancel.setVisibility(GONE);

        SaveData();
    }
    private void CancelSettings(){
        FirstNameField.setFocusable(false);
        LastNameField.setFocusable(false);
        BirthdayField.setFocusable(false);
        EmailField.setFocusable(false);
        PhoneNoField.setFocusable(false);
        Address01Field.setFocusable(false);
        Address02Field.setFocusable(false);
        CityField.setFocusable(false);
        StateField.setFocusable(false);
        PostCodeField.setFocusable(false);
        UserImage.setFocusable(false);

        FirstNameField.setFocusableInTouchMode(false);
        LastNameField.setFocusableInTouchMode(false);
        BirthdayField.setFocusableInTouchMode(false);
        EmailField.setFocusableInTouchMode(false);
        PhoneNoField.setFocusableInTouchMode(false);
        Address01Field.setFocusableInTouchMode(false);
        Address02Field.setFocusableInTouchMode(false);
        CityField.setFocusableInTouchMode(false);
        StateField.setFocusableInTouchMode(false);
        PostCodeField.setFocusableInTouchMode(false);
        UserImage.setFocusableInTouchMode(false);

        ButtonEdit.setVisibility(View.VISIBLE);
        ButtonSaved.setVisibility(GONE);
        ButtonCancel.setVisibility(GONE);
    }
    private void SignOutSettings(){
        sessionManager.logout();
    }

    //START of method to allow image selection using camera/gallery
    private void selectImage(Context context) {
        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Choose your profile picture");

        builder.setItems(options, (dialog, item) -> {

            if (options[item].equals("Take Photo")) {
                Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takePicture, 0);

            } else if (options[item].equals("Choose from Gallery")) {
                Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto , 1);

            } else if (options[item].equals("Cancel")) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    public String getStringImage(Bitmap bitmap11) {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap11.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);

        byte[] imageByteArray = byteArrayOutputStream.toByteArray();

        return Base64.encodeToString(imageByteArray, Base64.DEFAULT);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_image_capture, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    //END of method

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile);
        Declare();
        ToolbarSettings();
        getSession();
        MethodSettings();
    }

    private void SaveData(){
        LoadingLayout.setVisibility(View.VISIBLE);
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
        if(bitmap == null){
            parameters.put(user.ProfilePicture, getImage);
            //Toast.makeText(this, "Please Try Again!", Toast.LENGTH_LONG).show();
        }else{
            parameters.put(user.ProfilePicture, getStringImage(bitmap));
            //Toast.makeText(this, "Please !!!Try Again!", Toast.LENGTH_LONG).show();
//            parameters.put(user.ProfilePicture, getStringImage(bitmap));
        }

        parameters.put(user.Birthday, Birthday);
        parameters.put(user.EmailAddress, EmailAddress);
        parameters.put(user.PhoneNo, PhoneNo);
        parameters.put(user.Address01, Address01);
        parameters.put(user.Address02, Address02);
        parameters.put(user.City, City);
        parameters.put(user.State,State);
        parameters.put(user.Postcode, Postcode);

        JsonObjectRequest request_json = new JsonObjectRequest(user.updateUser, new JSONObject(parameters),
                response -> {
                    LoadingLayout.setVisibility(GONE);
                    sessionManager.logout02();
                    sessionManager.createSession(getUID, FirstName,
                            LastName, "http://hawkingnight.com/projectmit/API/upload/" + FirstName +".jpg",
                            Birthday, EmailAddress, PhoneNo, Address01, Address02, City, State, Postcode);
                },
                error -> {
                    LoadingLayout.setVisibility(GONE);
                    sessionManager.logout02();
                    sessionManager.createSession(getUID, FirstName,
                            LastName, "http://hawkingnight.com/projectmit/API/upload/" + FirstName +".jpg",
                            Birthday, EmailAddress, PhoneNo, Address01, Address02, City, State, Postcode);
                });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request_json);
    }
    private void closeKeyboard() {
        InputMethodManager inputManager = (InputMethodManager) getSystemService(
                Context.INPUT_METHOD_SERVICE);
        View focusedView = getCurrentFocus();
        if (focusedView != null) {
            inputManager.hideSoftInputFromWindow(focusedView.getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case 0:
                    if (resultCode == RESULT_OK && data != null) {
                        bitmap = (Bitmap) data.getExtras().get("data");
                        UserImage.setImageBitmap(bitmap);
                    }
                    break;
                case 1:
                    if (resultCode == RESULT_OK && data != null) {
                        filePath = data.getData();
                        try {
                            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                            bitmap = Bitmap.createScaledBitmap(bitmap, 300, 300, false);
                            UserImage.setImageBitmap(bitmap);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
            }
        }

    }
}
