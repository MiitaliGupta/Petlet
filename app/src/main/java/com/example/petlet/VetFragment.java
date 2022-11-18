package com.example.petlet;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class VetFragment extends Fragment {

    public VetFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v;
        v = inflater.inflate(R.layout.fragment_vet, container, false);

        TextView doctor1 = v.findViewById(R.id.layoutTextDoc1);
        TextView doctor2 = v.findViewById(R.id.layoutTextDoc2);

        doctor1.setText(Html.fromHtml(getString(R.string.doctor1)));
        doctor2.setText(Html.fromHtml(getString(R.string.doctor2)));

        return v;
    }
}