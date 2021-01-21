package com.project.mit.pages;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.project.mit.R;
import com.project.mit.models.Record;

import java.util.Date;

@SuppressLint("ResourceAsColor")
public class ScanDetail extends AppCompatActivity {

    TextView LocationName, LocationFullAddress, DateTime, StatusText;
    CardView CardViewStatus;
    Button GoBack;

    String locationName, locationFullAddress, dateTime, RiskStatus, ZoneStatus;
    Record record;

    private void Declare(){
        LocationName = findViewById(R.id.LocationName);
        LocationFullAddress = findViewById(R.id.LocationFullAddress);
        DateTime = findViewById(R.id.DateTime);
        StatusText = findViewById(R.id.TitleStatus);
        CardViewStatus = findViewById(R.id.CardViewStatus);
        GoBack = findViewById(R.id.ButtonGoBack);

        record = new Record();
    }

    private void MethodSettings(){
        GoBack.setOnClickListener(v -> {
            Intent GoBackIntent = new Intent(ScanDetail.this, Home.class);
            startActivity(GoBackIntent);
        });

        Intent getData = getIntent();
        locationName = getData.getStringExtra(record.LocationName);
        locationFullAddress = getData.getStringExtra(record.LocationFullAddress);
        dateTime = getData.getStringExtra(record.CreatedDateTime);
        RiskStatus = getData.getStringExtra(record.RiskStatus);
        ZoneStatus = getData.getStringExtra(record.ZoneStatus);

        LocationName.setText(locationName);
        LocationFullAddress.setText(locationFullAddress);
        DateTime.setText(dateTime);

        if(ZoneStatus.toLowerCase().equals("Green".toLowerCase())){
            StatusText.setText(R.string.you_are_in_green_zone);
            CardViewStatus.setBackgroundResource(R.color.green_zone);
        }else if(ZoneStatus.toLowerCase().equals("Orange".toLowerCase())){
            StatusText.setText(R.string.you_are_in_orange_zone);
            CardViewStatus.setCardBackgroundColor(R.color.orange_zone);
        }else{
            StatusText.setText(R.string.you_are_in_red_zone);
            CardViewStatus.setCardBackgroundColor(R.color.red_zone);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_details);
        Declare();
        MethodSettings();
    }
}
