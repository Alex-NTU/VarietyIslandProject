package com.example.varietyislandproject;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.varietyislandproject.Common.Common;
import com.example.varietyislandproject.Model.User;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

public class SignIn extends AppCompatActivity {
    EditText Phone, Password;
    Button btnSignIn;
    Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        Password = (MaterialEditText)findViewById(R.id.Password);
        Phone = (MaterialEditText)findViewById(R.id.Phone);
        btnSignIn = (Button)findViewById(R.id.btnSignIn);
        btnBack  = (Button)findViewById(R.id.btnBack);

        //Initialize firebase aspect
        //FirebaseApp.initializeApp(this);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("User");

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final ProgressDialog mDialog = new ProgressDialog(SignIn.this);
                mDialog.setMessage("Please Wait");
                mDialog.show();

                table_user.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        //Check if the user exists in the firebase database
                        if (dataSnapshot.child(Phone.getText().toString()).exists()) {
                        // Gets the users information
                        mDialog.dismiss();
                        User user = dataSnapshot.child(Phone.getText().toString()).getValue(User.class);
                        user.setPhone(Phone.getText().toString()); // sets the phone number
                        if (user.getPassword().equals(Password.getText().toString()))
                        {
                          Intent homeintent = new Intent (SignIn.this, MainMenu.class);
                            Common.currentUser = user;
                            startActivity(homeintent);
                            finish();

                        } else {
                            Toast.makeText(SignIn.this, "Sign In Unsuccessful", Toast.LENGTH_SHORT).show();
                        }
                    }
                        else
                        {
                            mDialog.dismiss();
                            Toast.makeText(SignIn.this, "This user does not exist",Toast.LENGTH_SHORT).show();
                        }
                }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backintent = new Intent (SignIn.this, MainActivity.class);
                startActivity(backintent);
                finish();
            }
        });


    }
}
