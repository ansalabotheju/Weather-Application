package com.example.weatherapp;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.example.weatherapp.R;

public class WeatherListAdapter extends ArrayAdapter<String> {
    private final Activity activityContext;
    private final String[] day;
    private final String[] temperature;
    private final String[] description;
    private final Integer[] icon;

    public WeatherListAdapter(Activity activityContext, String[] day, String[] temperature, String[] description, Integer[] icon) {
        super(activityContext, R.layout.weather_list_items, day);
        this.activityContext = activityContext;
        this.day = day;
        this.temperature = temperature;
        this.description = description;
        this.icon = icon;
    }

    @Override
    public View getView(int index, View view, ViewGroup viewGroup) {
        LayoutInflater layoutInflater =activityContext.getLayoutInflater();

        View itemListView = layoutInflater.inflate(R.layout.weather_list_items, null, true);
        TextView dayText = (TextView) itemListView.findViewById(R.id.dayText);
        TextView tempText = (TextView) itemListView.findViewById(R.id.tempText);
        TextView descText = (TextView) itemListView.findViewById(R.id.descText);
        ImageView weatherIcon = (ImageView) itemListView.findViewById(R.id.weather_icon);

        dayText.setText(day[index]);
        tempText.setText(temperature[index]);
        descText.setText(description[index]);
        weatherIcon.setImageResource(icon[index]);

        return itemListView;
    }
}
