package com.example.petlet;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class HelpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setTitle(R.string.help_activity_action_bar_title);

        TextView helpActivityTextView=findViewById(R.id.help_activity_text_view);


        helpActivityTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sendHelpEmail();

            }
        });



    }

    private void sendHelpEmail() {
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

}
