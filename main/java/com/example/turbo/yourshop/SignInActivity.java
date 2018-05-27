package com.example.turbo.yourshop;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.turbo.yourshop.Common.Common;
import com.example.turbo.yourshop.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class SignInActivity extends AppCompatActivity {


    EditText edtPhone,edtPassword;
    Button btnSignIn;
    com.rey.material.widget.CheckBox ckbRemember;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        edtPhone = findViewById(R.id.edtphone);
        edtPassword = findViewById(R.id.edtpassword);
        btnSignIn = findViewById(R.id.btnSignin);
        ckbRemember = findViewById(R.id.ckbRemember);

        Paper.init(this);

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference = firebaseDatabase.getReference("User");

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String phone = "+91"+edtPhone.getText().toString();

                if (Common.isConnectedToInternet(getBaseContext())) {

                    if (ckbRemember.isChecked()){
                        Paper.book().write(Common.USER_KEY,phone);
                        Paper.book().write(Common.PWD_KEY,edtPassword.getText().toString());
                    }

                    final ProgressDialog progressDialog = new ProgressDialog(SignInActivity.this);
                    progressDialog.setMessage("Please Waiting...");
                    progressDialog.show();

                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (phone.matches("") && phone.isEmpty()) {
                                Toast.makeText(SignInActivity.this, "Please enter register phone number", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            } else {
                                if (dataSnapshot.child(phone).exists()) {
                                    progressDialog.dismiss();
                                    User user = dataSnapshot.child(phone).getValue(User.class);
                                    assert user != null;
                                    user.setPhone(phone);
                                    if (user.getPassword().equals(edtPassword.getText().toString())) {
                                        Intent intentHome = new Intent(SignInActivity.this, Home.class);
                                        Common.currentUser = user;
                                        startActivity(intentHome);
                                        finish();

                                    } else {
                                        Toast.makeText(SignInActivity.this, "Log in failed", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    progressDialog.dismiss();
                                    Toast.makeText(SignInActivity.this, "User does not exist", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
                else {
                    Toast.makeText(SignInActivity.this,"Please check your internet connection",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
