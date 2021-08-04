package com.example.weatherapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class TemperatureSettings extends Fragment {

    private RadioGroup temperatureOptionsGroup;
    private RadioButton selectedOptionButton;
    private Button submitButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_temperature_settings, container, false);
        initiateViews(view);
        initiateListeners(view);
        return view;
    }

    private void initiateViews(View view) {
        temperatureOptionsGroup = (RadioGroup) view.findViewById(R.id.temperatureOptionsGroup);
        submitButton = (Button) view.findViewById(R.id.temperatureSubmitBtn);
    }

    private void initiateListeners(final View view) {
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int checkedRadioButtonId = temperatureOptionsGroup.getCheckedRadioButtonId();
                selectedOptionButton = (RadioButton) view.findViewById(checkedRadioButtonId);

                Intent intent = new Intent(getContext(), MainActivity.class);
                intent.putExtra("temp_unit", selectedOptionButton.getText());
                startActivity(intent);
            }
        });
    }
}