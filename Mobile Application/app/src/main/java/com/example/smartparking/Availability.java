package com.example.smartparking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Availability extends AppCompatActivity {

    Button map, book;
    TextView Slots, Spots;
    DatabaseReference ref_slot;
    int s1 = 1, s2 = 1, s3 = 1, ss1, ss2, ss3, bs1, bs2, bs3;
    int nslots;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_availability);

        map = findViewById(R.id.OpenGateBtn);
        Slots = findViewById(R.id.slots);
        Spots = findViewById(R.id.spots);
        book = findViewById(R.id.myBackBtn);

        ref_slot = FirebaseDatabase.getInstance().getReference();

        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Availability.this, Mapp.class);
                startActivity(intent);
//                Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
            }
        });

        book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Availability.this, Bookings.class);
                startActivity(intent);
            }
        });

        ref_slot.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ss1 = Integer.parseInt(snapshot.child("Slot1").getValue().toString());
                ss2 = Integer.parseInt(snapshot.child("Slot2").getValue().toString());
                ss3 = Integer.parseInt(snapshot.child("Slot3").getValue().toString());
                bs1 = Integer.parseInt(snapshot.child("BookedSlot1").getValue().toString());
                bs2 = Integer.parseInt(snapshot.child("BookedSlot2").getValue().toString());
                bs3 = Integer.parseInt(snapshot.child("BookedSlot3").getValue().toString());

                if(ss1 == 1 && bs1 == 1)
                {
                    s1 = 1;
                }
                else
                {
                    s1 = 0;
                }
                if(ss2 == 1 && bs2 == 1)
                {
                    s2 = 1;
                }
                else
                {
                    s2 = 0;
                }
                if(ss3 == 1 && bs3 == 1)
                {
                    s3 = 1;
                }
                else
                {
                    s3 = 0;
                }

                nslots = s1 + s2 + s3;
                Slots.setText(String.valueOf(nslots));

                if (nslots == 0)
                {
                    Spots.setText("Full ☹️️");
                }
                else if (nslots == 3)
                {
                    Spots.setText("A,B,C");
                }
                else if (nslots == 2)
                {
                    if(s1 == 1 && s2 == 1)
                    {
                        Spots.setText("A,B");
                    }
                    else if(s1 == 1 && s3 == 1)
                    {
                        Spots.setText("A,C");
                    }
                    else if(s3 == 1 && s2 == 1)
                    {
                        Spots.setText("B,C");
                    }
                }
                else if (nslots == 1)
                {
                    if(s1 == 1)
                    {
                        Spots.setText("A");
                    }
                    else if(s2 == 1)
                    {
                        Spots.setText("B");
                    }
                    else if(s3 == 1)
                    {
                        Spots.setText("C");
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}