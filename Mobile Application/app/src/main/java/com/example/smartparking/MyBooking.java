package com.example.smartparking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MyBooking extends AppCompatActivity {
    String contact = Login.contactNumber;
    Button back, open;
    TextView name, slot, date, duration;
    DatabaseReference ref_slot, ref_slot1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_booking);

        back = findViewById(R.id.myBackBtn);
        open = findViewById(R.id.OpenGateBtn);
        name = findViewById(R.id.textName);
        slot = findViewById(R.id.textSlot);
        date = findViewById(R.id.textDate);
        duration = findViewById(R.id.textDuration);

        ref_slot = FirebaseDatabase.getInstance().getReference();

        ref_slot.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChild(contact))
                {
                    name.setText(snapshot.child(contact).child("name").getValue().toString());
                    slot.setText(snapshot.child(contact).child("slot").getValue().toString());
                    date.setText(snapshot.child(contact).child("date").getValue().toString());
                    duration.setText(snapshot.child(contact).child("duration").getValue().toString());
                }
                else
                {
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ref_slot1 = FirebaseDatabase.getInstance().getReference();

                //Gate
                Intent intent = new Intent(MyBooking.this, GateService.class);
                startService(intent);
                Toast.makeText(MyBooking.this, "Opening Gate...", Toast.LENGTH_SHORT).show();

            }
        });
    }
}