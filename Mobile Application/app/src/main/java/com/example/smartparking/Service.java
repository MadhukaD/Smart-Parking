package com.example.smartparking;

import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Service extends android.app.Service {
    DatabaseReference ref_slot;
    String duration = Bookings.data.get(Bookings.contactNumber)[3];
    String slot = Bookings.data.get(Bookings.contactNumber)[2];

    final class MyThreadClass implements Runnable{
        int serviceID;

        public MyThreadClass(int serviceID) {
            this.serviceID = serviceID;
        }

        @Override
        public void run() {
            ref_slot = FirebaseDatabase.getInstance().getReference();
            int i = 0;
            synchronized (this)
            {
                while (i<10)
                {
                    try {
                        wait(Integer.parseInt(duration) * 100);
                        i++;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                stopSelf(serviceID);
            }
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        ref_slot = FirebaseDatabase.getInstance().getReference();

//        Toast.makeText(this, "Service Started...", Toast.LENGTH_SHORT).show();
        ref_slot.child("Booked" + slot).setValue("0");
        ref_slot.child(Bookings.contactNumber).setValue(BookingInformation.bookingClass);
        Thread thread = new Thread(new MyThreadClass(startId));
        thread.start();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        ref_slot = FirebaseDatabase.getInstance().getReference();
//        Toast.makeText(this, "Service Destroyed...", Toast.LENGTH_SHORT).show();
        ref_slot.child("Booked" + slot).setValue("1");
        ref_slot.child(Bookings.data.get(Bookings.contactNumber)[1]).removeValue();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
