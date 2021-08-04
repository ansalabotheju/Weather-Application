package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SettingsActivity extends AppCompatActivity {
    Button temperatureSettingsButton, citySettingButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getSupportActionBar().setTitle("Settings");

        temperatureSettingsButton = (Button) findViewById(R.id.temperatureSettingButton);
        citySettingButton = (Button) findViewById(R.id.citySettingButton);

        citySettingButton.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new CitySettings()).commit();
            }
        }));

        temperatureSettingsButton.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new TemperatureSettings()).commit();
            }
        }));
    }
}