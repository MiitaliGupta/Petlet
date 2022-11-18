package com.example.petlet;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class SettingFragment extends Fragment {

    public SettingFragment() {
        // Required empty public constructor
    }

    ImageView call;
    ImageView web;
    ImageView visit;
    ImageView mail;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_setting, container, false);
        call=(ImageView) v.findViewById(R.id.call);
        web=(ImageView) v.findViewById(R.id.web);
        mail=(ImageView) v.findViewById(R.id.mail);
        visit=(ImageView) v.findViewById(R.id.visit);
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i= new Intent(Intent.ACTION_DIAL);
                i.setData(Uri.parse("tel:+919519274242"));
                startActivity(i);
            }
        });
        web.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url="https://christuniversity.in/";
                Intent s=new Intent(Intent.ACTION_VIEW);
                s.setData(Uri.parse(url));
                startActivity(s);
            }
        });
        mail.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("IntentReset")
            @Override
            public void onClick(View view) {
                Intent j=new Intent(Intent.ACTION_SEND);
                j.setData(Uri.parse("email"));
                String[] s={"mitali.gupta@christuniversity.in"};
                j.putExtra(Intent.EXTRA_EMAIL,s);
                j.putExtra(Intent.EXTRA_SUBJECT,"Complaint register");
                j.putExtra(Intent.EXTRA_TEXT,"");
                j.setType("message/rfc822");
                Intent chooser = Intent.createChooser(j,"Mail Us Via:");
                startActivity(chooser);
            }
        });
        visit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent a=new Intent(Intent.ACTION_VIEW);
                a.setData(Uri.parse("geo:12.9362362,77.6039948"));
                Intent chooser= Intent.createChooser(a, "Reach us via: ");
                startActivity(chooser);
            }
        });
        return v;
    }
}