package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    String locationName, temperatureUnit;

    private static String BASE_URL = "http://api.openweathermap.org/data/2.5/onecall?";
    private static String APP_ID = "&appid=ccf6813b7a5ea8bbc759aeb31c630cca";
    private static String EXCLUSIONS = "&exclude=minutely,hourly,alerts,current";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTemperatureUnit();
        setLocationName();

        WeatherDataClient weatherDataClient = new WeatherDataClient();
        weatherDataClient.execute();
    }

    private void setLocationName() {
        String loc_name = getIntent().getStringExtra("loc_name");

        if(loc_name != null) {
            this.locationName = loc_name;
        } else {
            this.locationName = "Colombo";
        }
    }

    private void setTemperatureUnit() {
        String temp_unit = getIntent().getStringExtra("temp_unit");

        if(temp_unit != null) {
            this.temperatureUnit = temp_unit;
        } else {
            this.temperatureUnit = "Celsius";
        }
    }

    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.overflow_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        switch (itemId){
            case R.id.about:
                Intent intent_about = new Intent(MainActivity.this, AboutActivity.class);
                startActivity(intent_about);
                return true;
            case R.id.settings:
                Intent intent_Settings = new Intent(MainActivity.this,SettingsActivity.class);
                startActivity(intent_Settings);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @SuppressLint("StaticFieldLeak")
    public class WeatherDataClient extends AsyncTask<String,Void,String> {
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        InputStream stream = null;
        StringBuffer buffer = null;
        String weatherData = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @SuppressLint("DefaultLocale")
        @Override
        protected void onPostExecute(String text) {
            try {
                ListView weatherListView = findViewById(R.id.weather_item_list);
                final List<String> disLocation = new ArrayList<>();

                final String[] day = new String[7];
                final String[] temperature = new String[7];
                final Integer[] icon = new Integer[7];
                final String[] description = new String[7];
                final String[] humidity = new String[7];

                JSONObject weatherObject = new JSONObject(weatherData);
                JSONArray weatherArray = weatherObject.getJSONArray("daily");

                locationName = locationName.substring(0, 1).toUpperCase() + locationName.substring(1);

                for(int i = 0; i < 7; i++) {
                    JSONObject obj = weatherArray.getJSONObject(i);

                    //Extract and process date from JSON
                    @SuppressLint("SimpleDataFormat")
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE");

                    String timeString = obj.getString("dt");
                    long time = Integer.parseInt(timeString);
                    Date dFormat = new java.util.Date(time * 1000);
                    String dayText = simpleDateFormat.format(dFormat);

                    if(i == 0) {
                        day[i] = dayText + " (TODAY)";
                    } else {
                        day[i] = dayText;
                    }

                    //Extract and process temperature data
                    JSONObject temperatureObj = obj.getJSONObject("temp");
                    Double tempValue = temperatureObj.getDouble("day");

                    switch(temperatureUnit) {
                        case "Celsius":
                            temperature[i] = String.format("%.2f" + " \u2103", (tempValue - 273.15));
                            break;
                        case "Fahrenheit":
                            Double tempInF = ((tempValue - 273.15) * (9.0f / 5.0f) + 32.0f);
                            temperature[i] = String.format("%.2f" + " \u2109", tempInF);
                            break;
                        default:
                            temperature[i] = String.format("%.2f" + " K", tempValue);
                            break;
                    }

                    //Process and extract weather description and icon
                    JSONArray weatherObj = obj.getJSONArray("weather");
                    JSONObject weatherDesc = weatherObj.getJSONObject(0);
                    String descString = weatherDesc.getString("description");
                    description[i] = descString.toUpperCase();

                    String iconCode = weatherDesc.getString("icon");
                    icon[i] = setWeatherIcon(iconCode);

                    //Extract humidity data
                    humidity[i] = "Humidity: " + obj.getString("humidity") + "%";

                    disLocation.add(locationName);
                }

                WeatherListAdapter weatherListAdapter = new WeatherListAdapter(MainActivity.this, day, temperature, description, icon);
                weatherListView.setAdapter(weatherListAdapter);

                weatherListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int index, long id) {
                        Intent intent = new Intent(MainActivity.this, WeatherActivity.class);

                        intent.putExtra("day", day[index]);
                        intent.putExtra("temperature", temperature[index]);
                        intent.putExtra("city", disLocation.get(index));
                        intent.putExtra("description", description[index]);
                        intent.putExtra("humidity", humidity[index]);
                        intent.putExtra("icon", icon[index]);

                        startActivity(intent);
                    }
                });
            } catch(JSONException e){
                Log.e("Error", "error at line 202", e);
            }
        }

        @Override
        public String doInBackground(String... strings) {
            try {
                Geocoder geocoder = new Geocoder(getApplicationContext());
                List<Address> addCoords = geocoder.getFromLocationName(locationName, 5);

                String FINAL_COORD_STRING = "";
                for(Address coord : addCoords) {
                    if(coord.hasLatitude() && coord.hasLongitude()) {
                        FINAL_COORD_STRING = "lat="+ coord.getLatitude() + "&lon=" + coord.getLongitude();
                    }
                }

                String FINAL_URL = BASE_URL + FINAL_COORD_STRING + EXCLUSIONS + APP_ID;
                URL url = new URL(FINAL_URL);

                //Start connection with OpenWeatherMap
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();

                //Read the response
                buffer = new StringBuffer();
                stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));

                String line;
                while((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                if(buffer.length() == 0) {
                    return null;
                }

                weatherData = buffer.toString();

            } catch (IOException e) {
                Log.e("Error", "error at line 246", e);
                return null;
            } finally {
                if(connection != null) {
                    connection.disconnect();
                }

                if(reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        Log.e("Error", "error at line 257", e);
                    }
                }
                return null;
            }
        }

        public Integer setWeatherIcon(String icon) {
            switch(icon) {
                case "01d":
                    return R.drawable.d01;
                case "01n":
                    return R.drawable.n01;
                case "02d":
                    return R.drawable.d02;
                case "02n":
                    return R.drawable.n02;
                case "03d":
                    return R.drawable.d03;
                case "03n":
                    return R.drawable.n03;
                case "04d":
                    return R.drawable.d04;
                case "04n":
                    return R.drawable.n04;
                case "09d":
                    return R.drawable.d09;
                case "09n":
                    return R.drawable.n09;
                case "10d":
                    return R.drawable.d10;
                case "10n":
                    return R.drawable.n10;
                case "11d":
                    return R.drawable.d11;
                case "11n":
                    return R.drawable.n11;
                case "13d":
                    return R.drawable.d13;
                case "13n":
                    return R.drawable.n13;
                case "50d":
                    return R.drawable.d50;
                case "50n":
                    return R.drawable.n50;
                default:
                    return null;
            }
        }
    }
}