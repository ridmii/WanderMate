package com.example.wandermate;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WeatherActivity extends AppCompatActivity {
    private EditText cityInput;
    private Button getWeatherBtn;
    private TextView cityName, temperature, weatherDescription, humidity, windSpeed;
    private CardView weatherCard;
    private Handler mainHandler;
    private BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Weather");
        }

        cityInput = findViewById(R.id.cityInput);
        getWeatherBtn = findViewById(R.id.getWeatherBtn);
        cityName = findViewById(R.id.cityName);
        temperature = findViewById(R.id.temperature);
        weatherDescription = findViewById(R.id.weatherDescription);
        humidity = findViewById(R.id.humidity);
        windSpeed = findViewById(R.id.windSpeed);
        weatherCard = findViewById(R.id.weatherCard);
        bottomNavigation = findViewById(R.id.bottom_navigation);

        mainHandler = new Handler(Looper.getMainLooper());

        getWeatherBtn.setOnClickListener(v -> fetchWeather());
        bottomNavigation.setOnItemSelectedListener(this::onNavigationItemSelected);
        bottomNavigation.setSelectedItemId(R.id.navigation_weather); // Highlight weather item
    }

    private boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.navigation_home) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
            return true;
        } else if (itemId == R.id.navigation_location) {
            startActivity(new Intent(this, LocationSearchActivity.class));
            return true;
        } else if (itemId == R.id.navigation_weather) {
            return true; // Already here
        } else if (itemId == R.id.navigation_profile) {
            startActivity(new Intent(this, MainActivity.class)
                    .putExtra("navigate_to_profile", true));
            finish();
            return true;
        }
        return false;
    }

    private void fetchWeather() {
        String city = cityInput.getText().toString().trim();
        if (city.isEmpty()) {
            Toast.makeText(this, "Please enter a city name", Toast.LENGTH_SHORT).show();
            return;
        }

        String apiKey = getString(R.string.weather_api_key);
        String url = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + apiKey + "&units=metric";

        new Thread(() -> {
            try {
                String jsonResponse = getApiResponse(url);
                if (jsonResponse != null && !jsonResponse.isEmpty()) {
                    JSONObject jsonObject = new JSONObject(jsonResponse);
                    updateUI(jsonObject);
                }
            } catch (Exception e) {
                mainHandler.post(() -> Toast.makeText(WeatherActivity.this,
                        "Error fetching weather data", Toast.LENGTH_SHORT).show());
                e.printStackTrace();
            }
        }).start();
    }

    private String getApiResponse(String urlString) throws Exception {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();
        connection.disconnect();

        return response.toString();
    }

    private void updateUI(JSONObject jsonObject) throws Exception {
        mainHandler.post(() -> {
            try {
                String city = jsonObject.getString("name");
                JSONObject main = jsonObject.getJSONObject("main");
                double temp = main.getDouble("temp");
                int humid = main.getInt("humidity");
                JSONObject wind = jsonObject.getJSONObject("wind");
                double windSpd = wind.getDouble("speed");
                String description = jsonObject.getJSONArray("weather")
                        .getJSONObject(0)
                        .getString("description");

                cityName.setText(city);
                temperature.setText(String.format("%.1f°C", temp));
                weatherDescription.setText(description.substring(0, 1).toUpperCase() + description.substring(1));
                humidity.setText("Humidity: " + humid + "%");
                windSpeed.setText("Wind: " + windSpd + " m/s");
                weatherCard.setVisibility(View.VISIBLE);
            } catch (Exception e) {
                Toast.makeText(this, "Error parsing weather data", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        });
    }
}