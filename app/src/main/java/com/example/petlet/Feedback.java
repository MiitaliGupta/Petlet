package com.example.petlet;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RatingBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class Feedback extends AppCompatActivity {
    Dialog dialog;
    Button ShowDialog, WebViewBtn, SubmitBtn;
    Switch switch1;
    RadioButton r1, r2, r3, r4;
    CheckBox d, c, r, h;
    View layout;
    ToggleButton toggleBtn;
    TextView header,name;
    RatingBar rate;

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        switch1 = findViewById(R.id.switch1);
        layout = findViewById(R.id.lay);
        toggleBtn = findViewById(R.id.toggle_Btn);
        WebViewBtn = findViewById(R.id.webviewBtn);
        SubmitBtn = findViewById(R.id.submit);
        r1 = findViewById(R.id.radio1);
        r2 = findViewById(R.id.radio2);
        r3 = findViewById(R.id.radio3);
        r4 = findViewById(R.id.radio4);
        d = findViewById(R.id.check_dogs);
        c = findViewById(R.id.check_cats);
        r = findViewById(R.id.check_rabbits);
        h = findViewById(R.id.check_hamsters);
        header = findViewById(R.id.header);
        name = findViewById(R.id.name);
        rate = findViewById(R.id.ratingBar);

//        if(toggleBtn.isChecked())
//        {
//            Toast.makeText(Feedback.this, "ON", Toast.LENGTH_SHORT).show();
//        }
//        else
//        {
//            Toast.makeText(Feedback.this, "OFF", Toast.LENGTH_SHORT).show();
//        }

        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @SuppressLint("ResourceAsColor")
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // do something, the isChecked will be
                // true if the switch is in the On position

                if (switch1.isChecked()) {
            layout.setBackgroundResource(R.color.P_orange);
            rate.setProgressBackgroundTintList(getResources().getColorStateList(R.color.P_Yellow));
            rate.setProgressTintList(getResources().getColorStateList(R.color.P_DarkPurple));
            name.setBackgroundTintList(getResources().getColorStateList(R.color.P_Yellow));
            header.setTextColor(getResources().getColorStateList(R.color.P_DarkPurple));
//            toggleBtn.setBackgroundTintList(getResources().getColorStateList(R.color.P_Yellow));
//            toggleBtn.setTextColor(R.color.P_DarkPurple);
//            switch1.setBackground(getResources().getDrawable(R.color.P_LightPurple));
//            r1.setBackground(getResources().getDrawable(R.color.P_orange));
//            r2.setBackground(getResources().getDrawable(R.color.P_orange));
//            r3.setBackground(getResources().getDrawable(R.color.P_orange));
//            r4.setBackground(getResources().getDrawable(R.color.P_orange));
//            c.setBackground(getResources().getDrawable(R.color.P_orange));
//            d.setBackground(getResources().getDrawable(R.color.P_orange));
//            r.setBackground(getResources().getDrawable(R.color.P_orange));
//            h.setBackground(getResources().getDrawable(R.color.P_orange));
//                    Toast.makeText(Feedback.this, "ON", Toast.LENGTH_SHORT).show();
                } else {
//                    Toast.makeText(Feedback.this, "OFF", Toast.LENGTH_SHORT).show();
                    header.setTextColor(getResources().getColorStateList(R.color.P_Yellow));
                    layout.setBackgroundResource(R.color.P_DarkPurple);
                    rate.setProgressBackgroundTintList(getResources().getColorStateList(R.color.semi_transparent));
                    rate.setProgressTintList(getResources().getColorStateList(R.color.P_Yellow));
                    name.setBackgroundTintList(getResources().getColorStateList(R.color.semi_transparent));
                }
            }
        });

        ShowDialog = findViewById(R.id.submit);
        //Create the Dialog here
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.custom_alert);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.alert_bg
            ));
        }
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false); //Optional
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
//Setting the animations to dialog
        Button Okay = dialog.findViewById(R.id.btn_okay);
        Button Cancel = dialog.findViewById(R.id.btn_cancel);
        Okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Feedback.this,MainActivity2.class);
                startActivity(i);
                Toast.makeText(Feedback.this, "Okay", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Feedback.this,MainActivity2.class);
                startActivity(i);
                Toast.makeText(Feedback.this, "Cancel", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        ShowDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show(); // Showing the dialog here
            }
        });
    }

    public void openWebView(View view) {
        Intent i2 = new Intent(this, WebviewDemo.class);
        startActivity(i2);
    }

    public void onToggleClicked(View view) {
        boolean on = ((ToggleButton)view).isChecked();
        if(on)
        {
            Toast.makeText(Feedback.this, "OFF", Toast.LENGTH_SHORT).show();
        } else
        {
            Toast.makeText(Feedback.this, "ON", Toast.LENGTH_SHORT).show();
        }
    }
}