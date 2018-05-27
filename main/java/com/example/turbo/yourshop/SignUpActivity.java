package com.example.turbo.yourshop;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.turbo.yourshop.Common.Common;
import com.example.turbo.yourshop.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.concurrent.TimeUnit;

public class SignUpActivity extends AppCompatActivity {

    MaterialEditText edtPhone,edtName,edtPassword,edtOtp;
    Button btnSignUp;
    FirebaseAuth mAuth;
    private int btnType = 0;
    ProgressBar progressBarPhone;
    private String mVerificationId;
    private String name,phone,password;

    PhoneAuthProvider.OnVerificationStateChangedCallbacks authCallBacK;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        edtPhone = findViewById(R.id.edtphone);
        edtName = findViewById(R.id.edtname);
        edtPassword = findViewById(R.id.edtpassword);
        edtOtp = findViewById(R.id.edtOtp);
        btnSignUp = findViewById(R.id.btnSignup);
        progressBarPhone = findViewById(R.id.progressBarPhone);

        mAuth = FirebaseAuth.getInstance();
        final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("User");



        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = edtName.getText().toString();
                phone = "+91"+edtPhone.getText().toString();
                password = edtPassword.getText().toString();
                if (btnType == 0) {
                    progressBarPhone.setVisibility(View.VISIBLE);
                    edtPhone.setEnabled(false);
                    edtName.setEnabled(false);
                    edtPassword.setEnabled(false);
                    btnSignUp.setEnabled(false);
                    PhoneAuthProvider.getInstance().verifyPhoneNumber(phone,
                            60,
                            TimeUnit.SECONDS,
                            SignUpActivity.this,
                            authCallBacK = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                                @Override
                                public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

                                    signInWithPhoneAuthCredential(phoneAuthCredential);

                                }
                                @Override
                                public void onVerificationFailed(FirebaseException e) {
                                    Toast.makeText(SignUpActivity.this,"Error in verification",Toast.LENGTH_SHORT).show();
                                    progressBarPhone.setVisibility(View.INVISIBLE);

                                }
                                @SuppressLint("SetTextI18n")
                                @Override
                                public void onCodeSent(String verificationId,
                                                       PhoneAuthProvider.ForceResendingToken token) {

                                    // Save verification ID and resending token so we can use them later
                                    mVerificationId = verificationId;


                                    btnType = 1;

                                    progressBarPhone.setVisibility(View.INVISIBLE);
                                    edtOtp.setVisibility(View.VISIBLE);
                                    btnSignUp.setText("Verify Code");
                                    btnSignUp.setEnabled(true);

                                }
                            });
                } else {
                    btnSignUp.setEnabled(false);
                    String verificationCode = edtOtp.getText().toString();
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, verificationCode);
                    signInWithPhoneAuthCredential(credential);
                }
            }
        });
    }
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            if (Common.isConnectedToInternet(getBaseContext())) {
                                    final ProgressDialog progressDialog = new ProgressDialog(SignUpActivity.this);
                                    progressDialog.setMessage("Please Waiting...");
                                    progressDialog.show();

                                    databaseReference.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.child(phone).exists()) {
                                                progressDialog.dismiss();
                                                Toast.makeText(SignUpActivity.this, "Phone Number already register", Toast.LENGTH_SHORT).show();
                                                progressBarPhone.setVisibility(View.INVISIBLE);
                                            }
                                            else {

                                                progressDialog.dismiss();
                                                    User user = new User(name, password);
                                                    databaseReference.child(phone).setValue(user);
                                                    Toast.makeText(SignUpActivity.this, "Sign Up successfully", Toast.LENGTH_SHORT).show();
                                                    Intent mainIntent = new Intent(SignUpActivity.this,MainActivity.class);
                                                    startActivity(mainIntent);
                                                    finish();

                                                }
                                            }
                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                                } else {
                                    Toast.makeText(SignUpActivity.this, "Please check your internet connection", Toast.LENGTH_SHORT).show();
                                }
                            }
                            // Sign in success, update UI with the signed-in user's information
                        else {
                            // Sign in failed, display a message and update the UI

                            Toast.makeText(SignUpActivity.this,"Something is wrong",Toast.LENGTH_SHORT).show();
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                Toast.makeText(SignUpActivity.this,"OTP is invalid",Toast.LENGTH_SHORT).show();
                                btnSignUp.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                });
    }
}
