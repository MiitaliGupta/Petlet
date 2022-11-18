package com.example.petlet;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ContactDevelopers extends AppCompatActivity {

    ImageView gmail,github, linkedin;
    Button aboutBackBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_developers);

        gmail =(ImageView) findViewById(R.id.mgmail);
        github=(ImageView) findViewById(R.id.mgithub);
        linkedin=(ImageView) findViewById(R.id.mlinkedin);
        aboutBackBtn = (Button)findViewById(R.id.aboutBackBtn);

//        gmail.setClickable(true);
//        github.setClickable(true);
//        linkedin.setClickable(true);

        gmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent j=new Intent(Intent.ACTION_SEND);
                j.setData(Uri.parse("email"));
                String[] s={"mitali.gupta@science.christuniversity.in"};
                j.putExtra(Intent.EXTRA_EMAIL,s);
                j.putExtra(Intent.EXTRA_SUBJECT,"Reason of Contact : ");
                j.putExtra(Intent.EXTRA_TEXT,"");
                j.setType("message/rfc822");
                Intent chooser = Intent.createChooser(j,"Mail Us Via:");
                startActivity(chooser);
            }
        });
        github.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url="https://github.com/MiitaliGupta";
                Intent s=new Intent(Intent.ACTION_VIEW);
                s.setData(Uri.parse(url));
                startActivity(s);
            }
        });
        linkedin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url="https://www.linkedin.com/in/gupta-mitali/";
                Intent s=new Intent(Intent.ACTION_VIEW);
                s.setData(Uri.parse(url));
                startActivity(s);
            }
        });

        aboutBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity2.class);
                startActivity(intent);
                finish();
            }
        });

    }
    @Override
    public void onBackPressed()
    {
        Toast.makeText(ContactDevelopers.this,"Back Press is Disables, Please use Back Button",Toast.LENGTH_SHORT).show();
    }

}