package com.example.smartparking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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

public class Signup extends AppCompatActivity {
    EditText name, contact, password, cpassword;
    Button signup;
    TextView login;
    DatabaseReference ref_slot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        name = findViewById(R.id.editNameCreate);
        contact = findViewById(R.id.editContactCreate);
        password = findViewById(R.id.editPasswordCreate);
        cpassword = findViewById(R.id.editConfirmPasswordCreate);
        signup = findViewById(R.id.signupBtn);
        login = findViewById(R.id.loginBtnCreate);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent checkAvailability = new Intent(Signup.this, Login.class);
                startActivity(checkAvailability);
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ref_slot = FirebaseDatabase.getInstance().getReference();

                if (!isEmpty())
                {
                    String Name = name.getText().toString();
                    String Contact = contact.getText().toString();
                    String Password = password.getText().toString();
                    String CPassword = cpassword.getText().toString();

                    if(Password.equals(CPassword))
                    {
                        AccountClass account = new AccountClass(Name, Contact, Password);
                        ref_slot.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.child("Accounts").hasChild(Contact))
                                {
                                    Toast.makeText(Signup.this, "This contact is already registered!", Toast.LENGTH_SHORT).show();
                                }
                                else
                                {
                                    ref_slot.child("Accounts").child(Contact).setValue(account);
                                    name.getText().clear();
                                    contact.getText().clear();
                                    password.getText().clear();
                                    cpassword.getText().clear();
                                    Toast.makeText(Signup.this, "Account created successfully!", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    }
                    else
                    {
                        Toast.makeText(Signup.this, "Passwords do not match!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }

    private boolean isEmpty() {
        if (TextUtils.isEmpty(name.getText().toString()))
        {
            name.setError("This field cannot be empty!");
            return true;
        }
        else if (TextUtils.isEmpty(contact.getText().toString()))
        {
            contact.setError("This field cannot be empty!");
            return true;
        }
        else if (TextUtils.isEmpty(password.getText().toString()))
        {
            password.setError("This field cannot be empty!");
            return true;
        }
        else if (TextUtils.isEmpty(cpassword.getText().toString()))
        {
            cpassword.setError("This field cannot be empty!");
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