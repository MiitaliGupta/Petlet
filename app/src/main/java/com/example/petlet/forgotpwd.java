package com.example.petlet;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class forgotpwd extends AppCompatActivity {
    private Button resetbtn;
    private EditText emailId;
    private String email;
    private FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgotpwd);

        auth = FirebaseAuth.getInstance();
        resetbtn = findViewById(R.id.resetbtn);
        emailId = findViewById(R.id.mobile);
    }
    public void validateData(View view) {
        email = emailId.getText().toString();
        if(email.isEmpty())
        {
            emailId.setError("Required");
        }
        else { forgetPass();}

    }

    private void forgetPass() {

        auth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(forgotpwd.this,"Check Your Mail",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(forgotpwd.this, com.example.petlet.Login.class));
                            finish();
                        }
                        else
                        {
                            Toast t = Toast.makeText(forgotpwd.this,task.getException().getMessage(),Toast.LENGTH_SHORT);
                            t.show();
                        }
                    }
                });
    }


    public void toEmailSignUp(View view) {
        Intent i = new Intent(this, EmailSignUp.class);
        startActivity(i);
    }
    public void toMobileSignUp(View view) {
        Intent i = new Intent(this, com.example.petlet.MobileSignUp.class);
        startActivity(i);
        finish();
    }

    @Override
    public void onBackPressed() {
//        finishAffinity();
        startActivity(new Intent(this, com.example.petlet.Login.class));
        finish();
    }

}