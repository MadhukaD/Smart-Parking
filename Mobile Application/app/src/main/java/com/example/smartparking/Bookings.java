package com.example.smartparking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class Bookings extends AppCompatActivity {
    EditText duration, name, email, contact;
    Spinner slotList;
    Button back, book, info;
    int slotSum;

    DatabaseReference ref_slot;
    int s1, s2, s3, bs1, bs2, bs3, ss1, ss2, ss3;
    ArrayAdapter<CharSequence> adapter;

    public static HashMap<String, String[]> data;
    public  static String contactNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookings);

        duration = findViewById(R.id.editDuration);
        slotList = findViewById(R.id.spinnerSlot);
        back = findViewById(R.id.backBtn);
        book = findViewById(R.id.signupBtn);
        info = findViewById(R.id.viewBookingBtn);

        ref_slot = FirebaseDatabase.getInstance().getReference();

        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ref_slot.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.hasChild(Login.contactNumber))
                        {
                            Intent intent = new Intent(Bookings.this, MyBooking.class);
                            startActivity(intent);
                        }
                        else
                        {
                            Toast.makeText(Bookings.this, "You don't have a booking!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
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
                slotSum = s1 + s2 + s3;

                if (slotSum == 3)
                {
                    adapter = ArrayAdapter.createFromResource(Bookings.this, R.array.slotArray123, android.R.layout.simple_spinner_item);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    slotList.setAdapter(adapter);
                }
                else if (slotSum == 2)
                {
                    if (s1 == 1 && s2 == 1)
                    {
                        adapter = ArrayAdapter.createFromResource(Bookings.this, R.array.slotArray12, android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        slotList.setAdapter(adapter);
                    }
                    else if (s1 == 1 && s3 == 1)
                    {
                        adapter = ArrayAdapter.createFromResource(Bookings.this, R.array.slotArray13, android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        slotList.setAdapter(adapter);
                    }
                    else if (s2 == 1 && s3 == 1)
                    {
                        adapter = ArrayAdapter.createFromResource(Bookings.this, R.array.slotArray23, android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        slotList.setAdapter(adapter);
                    }
                }
                else if (slotSum == 1)
                {
                    if (s1 == 1)
                    {
                        adapter = ArrayAdapter.createFromResource(Bookings.this, R.array.slotArray1, android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        slotList.setAdapter(adapter);
                    }
                    else if (s2 == 1)
                    {
                        adapter = ArrayAdapter.createFromResource(Bookings.this, R.array.slotArray2, android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        slotList.setAdapter(adapter);
                    }
                    else if (s3 == 1)
                    {
                        adapter = ArrayAdapter.createFromResource(Bookings.this, R.array.slotArray3, android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        slotList.setAdapter(adapter);
                    }

                }
                else
                {
                    adapter = ArrayAdapter.createFromResource(Bookings.this, R.array.slotArray0, android.R.layout.simple_spinner_item);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    slotList.setAdapter(adapter);
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

        book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Duration = duration.getText().toString();
                String Contact = Login.contactNumber;
                String sSlot = slotList.getSelectedItem().toString();

                if (slotSum == 0)
                {
                    Toast.makeText(Bookings.this, "No available slots! Please try again later!", Toast.LENGTH_SHORT).show();
                }
                else if (TextUtils.isEmpty(Duration))
                {
                    duration.setError("This field cannot be empty!");
                }
                else
                {
                    ref_slot.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String Name = snapshot.child("Accounts").child(Contact).child("name").getValue().toString();
                            data = new HashMap<>();
                            contactNumber = Login.contactNumber;
                            String[] dataArray = {Name, Contact, sSlot, Duration};
                            data.put(Contact, dataArray);
                            duration.getText().clear();
                            Intent intent = new Intent(Bookings.this, BookingInformation.class);
                            startActivity(intent);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
            }
        });

    }
}