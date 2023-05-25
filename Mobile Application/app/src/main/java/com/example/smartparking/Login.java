package com.example.smartparking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {
    EditText contact, password;
    Button login;
    TextView create;
    DatabaseReference ref_slot;
    public static String contactNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        contact = findViewById(R.id.editContactCreate);
        password = findViewById(R.id.editPasswordCreate);
        login = findViewById(R.id.signupBtn);
        create = findViewById(R.id.loginBtnCreate);

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent checkAvailability = new Intent(Login.this, Signup.class);
                startActivity(checkAvailability);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isEmpty())
                {
                    String Contact = contact.getText().toString();
                    String Password = password.getText().toString();

                    ref_slot = FirebaseDatabase.getInstance().getReference();

                    ref_slot.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.child("Accounts").hasChild(Contact))
                            {
                                if(snapshot.child("Accounts").child(Contact).child("password").getValue().toString().equals(Password))
                                {
                                    Intent intent = new Intent(Login.this, HomePage.class);
                                    contactNumber = Contact;
                                    contact.getText().clear();
                                    password.getText().clear();
                                    startActivity(intent);
                                }
                                else
                                {
                                    Toast.makeText(Login.this, "Password do not match!", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else
                            {
                                Toast.makeText(Login.this, "Please register!", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


                }
            }
        });
    }

    private boolean isEmpty(){
        if(TextUtils.isEmpty(contact.getText().toString()))
        {
            contact.setError("This field cannot be empty!");
            return true;
        }
        else if(TextUtils.isEmpty(password.getText().toString()))
        {
            password.setError("This field cannot be empty!");
            return true;
        }
        else
        {
            return false;
        }
    }

    @Override
    public void onBackPressed() {
        // super.onBackPressed();
        return;
    }
}