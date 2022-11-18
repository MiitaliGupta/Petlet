package com.example.petlet;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FirstPage extends AppCompatActivity {
    private FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_page);
        auth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = auth.getCurrentUser();
        if (user !=null){
            Intent i = new Intent(this, MainActivity2.class);
            startActivity(i);
            finish();
        }
    }

//    public void logIn(View view) {
//        Intent i = new Intent(this, Login.class);
//        startActivity(i);
//        finish();
//    }

    public void signUp(View view) {
        Intent i = new Intent(this, com.example.petlet.MobileSignUp.class);
        startActivity(i);
        finish();
    }
    @Override
    public void onBackPressed() {
        finishAffinity();
        finish();
    }
}