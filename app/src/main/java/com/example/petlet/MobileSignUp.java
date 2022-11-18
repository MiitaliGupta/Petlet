package com.example.petlet;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class MobileSignUp extends AppCompatActivity {
    private EditText countryCodeEdit , phoneNumberEdit;
    private FirebaseAuth auth;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBacks;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        Log.e("Error","HELLO");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mobile_signup);

        Button mSendOTPBtn = findViewById(R.id.resetbtn);
        countryCodeEdit = findViewById(R.id.countrycode);
        phoneNumberEdit = findViewById(R.id.mobile);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        auth = FirebaseAuth.getInstance();

        mSendOTPBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                String country_code = countryCodeEdit.getText().toString().trim();
                String phone = phoneNumberEdit.getText().toString().trim();
                String phoneNumber = "+" + country_code + "" + phone;
                if (!country_code.isEmpty() || !phone.isEmpty()){
                    PhoneAuthOptions options = PhoneAuthOptions.newBuilder(auth)
                            .setPhoneNumber(phoneNumber)
                            .setTimeout(60L , TimeUnit.SECONDS)
                            .setActivity(MobileSignUp.this)
                            .setCallbacks(mCallBacks)
                            .build();
                    PhoneAuthProvider.verifyPhoneNumber(options);
                }else{
//                    progressBar.setVisibility(View.INVISIBLE);
                    progressBar.setVisibility(View.GONE);
                    phoneNumberEdit.requestFocus();
                    Toast t = Toast.makeText(MobileSignUp.this,"Please Enter Country Code and Phone Number",Toast.LENGTH_SHORT);
                    t.show();

//                    processText.setText("Please Enter Country Code and Phone Number");
//                    processText.setTextColor(Color.RED);
//                    processText.setVisibility(View.VISIBLE);
                }
            }
        });

        mCallBacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                signIn(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                progressBar.setVisibility(View.GONE);
                Toast t = Toast.makeText(MobileSignUp.this,e.getMessage(),Toast.LENGTH_SHORT);
                t.show();
//
//                processText.setText(e.getMessage());
//                processText.setTextColor(Color.RED);
//                processText.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);

                //sometime the code is not detected automatically
                //so user has to manually enter the code
                progressBar.setVisibility(View.GONE);
                Toast t = Toast.makeText(MobileSignUp.this,"OTP has been Sent",Toast.LENGTH_SHORT);
                t.show();
//
//                processText.setText("OTP has been Sent");
//                processText.setVisibility(View.VISIBLE);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("Check","calling otp page");
                        Intent otpIntent = new Intent(MobileSignUp.this , otp.class);
                        otpIntent.putExtra("auth" , s);
                        startActivity(otpIntent);
                    }
                }, 0);

            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = auth.getCurrentUser();
        if (user !=null){
            sendToMain();
        }
    }
    private void sendToMain(){
        Intent mainIntent = new Intent(MobileSignUp.this , MainActivity2.class);
        startActivity(mainIntent);
        finish();
    }
    private void signIn(PhoneAuthCredential credential){
        auth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);
                if (task.isSuccessful()){
                    sendToMain();
                }else{
                    Toast t = Toast.makeText(MobileSignUp.this,task.getException().getMessage(),Toast.LENGTH_SHORT);
                    t.show();
//                    processText.setText(task.getException().getMessage());
//                    processText.setTextColor(Color.RED);
//                    processText.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    public void toEmailSignUp(View view) {
        Intent i = new Intent(this, EmailSignUp.class);
        startActivity(i);

    }
    @Override
    public void onBackPressed() {
//        finishAffinity();
        startActivity(new Intent(this, FirstPage.class));
        finish();
    }
}
