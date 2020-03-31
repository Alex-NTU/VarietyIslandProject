package com.example.varietyislandproject;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.varietyislandproject.Common.Common;
import com.example.varietyislandproject.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

public class SignUp extends AppCompatActivity {

    MaterialEditText Phone,Name,Password;
    Button btnSignup;
    Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        Name = (MaterialEditText)findViewById(R.id.Name);
        Password = (MaterialEditText)findViewById(R.id.Password);
        Phone = (MaterialEditText)findViewById(R.id.Phone);

        btnSignup = (Button)findViewById(R.id.btnSignUp);
        btnBack  = (Button)findViewById(R.id.btnBack);

        //Initialize firebase aspect
        FirebaseDatabase Database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = Database.getReference("User");

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final ProgressDialog mDialog = new ProgressDialog(SignUp.this);
                mDialog.setMessage("Please Wait");
                mDialog.show();

                table_user.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //Check if user already exists
                        if(dataSnapshot.child(Phone.getText().toString()).exists())
                        {
                            mDialog.dismiss();
                            Toast.makeText(SignUp.this, "This phone number is already registered", Toast.LENGTH_SHORT).show();
                        }
                        if(Phone.getText().toString().length() <= 11)
                        {
                            mDialog.dismiss();
                            User user = new User (Phone.getText().toString(),Phone.getText().toString());
                            table_user.child(Phone.getText().toString()).setValue(user);
                            Toast.makeText(SignUp.this, "Your account is now registered", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                        else
                        {
                            mDialog.dismiss();
                            Toast.makeText(SignUp.this, "Please check details", Toast.LENGTH_SHORT).show();
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
                Intent backintent = new Intent (SignUp.this, MainActivity.class);
                startActivity(backintent);
                finish();
            }
        });

    }
}
