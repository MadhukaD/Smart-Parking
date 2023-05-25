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

import java.text.SimpleDateFormat;
import java.util.Date;

public class BookingInformation extends AppCompatActivity {

    TextView name, bill, contact, slot, date, duration;
    Button back, confirm;

    int slotSum;
    DatabaseReference ref_slot;
    public static BookingClass bookingClass;
    int s1, s2, s3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_information);

        name = findViewById(R.id.textName);
        bill = findViewById(R.id.totalBill);
        contact = findViewById(R.id.textContact);
        slot = findViewById(R.id.textSlot);
        date = findViewById(R.id.textDate);
        duration = findViewById(R.id.textDuration);
        back = findViewById(R.id.myBackBtn);
        confirm = findViewById(R.id.OpenGateBtn);

        ref_slot = FirebaseDatabase.getInstance().getReference();

        String cont = Bookings.contactNumber;

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date dateF = new Date();

        name.setText(Bookings.data.get(cont)[0]);
        contact.setText(Bookings.data.get(cont)[1]);
        slot.setText(Bookings.data.get(cont)[2]);
        date.setText(formatter.format(dateF));
        bill.setText(Integer.parseInt(Bookings.data.get(cont)[3]) * 10 + ".00");
        duration.setText(Bookings.data.get(cont)[3] + " seconds");

        ref_slot.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                s1 = Integer.parseInt(snapshot.child("Slot1").getValue().toString());
                s2 = Integer.parseInt(snapshot.child("Slot2").getValue().toString());
                s3 = Integer.parseInt(snapshot.child("Slot3").getValue().toString());
                slotSum = s1 + s2 + s3;
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

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ref_slot = FirebaseDatabase.getInstance().getReference();

                ref_slot.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.child(Bookings.data.get(cont)[2]).getValue().equals("0") ||
                                snapshot.child("Booked" + Bookings.data.get(cont)[2]).getValue().equals("0"))
                        {
                            Toast.makeText(BookingInformation.this, "Selected slot is already booked!", Toast.LENGTH_SHORT).show();

                            synchronized (this)
                            {
                                try {
                                    wait(1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }

                            Intent intent1 = new Intent(BookingInformation.this, Bookings.class);
                            intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent1);
                            finish();
                        }
                        else if (snapshot.hasChild(Bookings.data.get(cont)[1]))
                        {
                            Toast.makeText(BookingInformation.this, "You already have a booking!", Toast.LENGTH_SHORT).show();

                            synchronized (this)
                            {
                                try {
                                    wait(1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }

                            Intent intent1 = new Intent(BookingInformation.this, Bookings.class);
                            intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent1);
                            finish();
                        }
                        else
                        {
                            addBooking(Bookings.data.get(cont)[0], Bookings.data.get(cont)[1],
                                    Bookings.data.get(cont)[2], formatter.format(dateF), Bookings.data.get(cont)[3]);
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }

    private void addBooking(String name, String contact, String slot, String date, String duration) {
        bookingClass = new BookingClass(name,contact,slot,date,duration);
//        ref_slot.child(contact).setValue(bookingClass);
        Intent intent = new Intent(BookingInformation.this, Service.class);
        startService(intent);
        Intent intent1 = new Intent(BookingInformation.this, Confirmation.class);
        intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent1);
        finish();
    }

}