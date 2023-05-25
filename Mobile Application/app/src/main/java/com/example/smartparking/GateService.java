package com.example.smartparking;

import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class GateService extends android.app.Service {
    DatabaseReference ref_slot;
    String contact = Login.contactNumber;

    final class MyThreadClass implements Runnable{
        int serviceID;

        public MyThreadClass(int serviceID) {
            this.serviceID = serviceID;
        }

        @Override
        public void run() {
            int i = 0;
            synchronized (this)
            {
                while (i<10)
                {
                    try {
                        wait(500);
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
        ref_slot.child("Gate").setValue("1");
        Thread thread = new Thread(new MyThreadClass(startId));
        thread.start();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        ref_slot = FirebaseDatabase.getInstance().getReference();
        ref_slot.child("Gate").setValue("0");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
