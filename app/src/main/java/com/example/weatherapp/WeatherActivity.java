package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.media.Image;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class WeatherActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        Bundle bundle = getIntent().getExtras();

        TextView dateText = (TextView) findViewById(R.id.indDateText);
        TextView cityText = (TextView) findViewById(R.id.indCityText);
        TextView tempText = (TextView) findViewById(R.id.indTempText);
        TextView descText = (TextView) findViewById(R.id.indDescText);
        TextView humidText = (TextView) findViewById(R.id.indHumidText);
        ImageView weatherIcon = (ImageView) findViewById(R.id.indWeatherIcon);

        if(android.os.Build.VERSION.SDK_INT>=android.os.Build.VERSION_CODES.O) {
            LocalDateTime time = LocalDateTime.now();
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            dateText.setText(String.valueOf(time.format(dateTimeFormatter)));
        }

        cityText.setText(bundle.getString("city"));
        tempText.setText(bundle.getString("temperature"));
        descText.setText(bundle.getString("description"));
        humidText.setText(bundle.getString("humidity"));
        weatherIcon.setImageResource(bundle.getInt("icon"));
    }
}