package com.example.weatherapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class CitySettings extends Fragment {

    private EditText newCityText;
    private Button newCitySubmitButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_city_settings, container, false);
        initiateViews(view);
        initiateListeners();
        return view;
    }

    private void initiateViews(View view) {
        newCityText = (EditText) view.findViewById(R.id.newCityText);
        newCitySubmitButton = (Button) view.findViewById(R.id.newCitySubmitBtn);
    }

    private void initiateListeners() {
        newCitySubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MainActivity.class);
                intent.putExtra("loc_name", newCityText.getText().toString());
                startActivity(intent);
            }
        });
    }
}