package com.example.smartparking;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class HomePage extends AppCompatActivity {

    Button availability;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        availability = findViewById(R.id.button);

        availability.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent checkAvailability = new Intent(HomePage.this, Availability.class);
                startActivity(checkAvailability);
            }
        });
    }

    @Override
    public void onBackPressed() {
        // super.onBackPressed();
        finish();
    }
}