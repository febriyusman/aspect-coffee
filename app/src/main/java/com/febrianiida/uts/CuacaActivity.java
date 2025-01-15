package com.febrianiida.uts;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CuacaActivity extends AppCompatActivity {
    private TextView tempTextView, descriptionTextView;
    private SearchView searchView;
    private TextView humidityTextView, windSpeedTextView, lastUpdatedTextView;
    private ImageView weatherDetailImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cuaca); // Layout baru untuk Activity ini

        // Inisialisasi elemen UI
        tempTextView = findViewById(R.id.tempTextView);
        descriptionTextView = findViewById(R.id.descriptionTextView);
        searchView = findViewById(R.id.searchView);
        humidityTextView = findViewById(R.id.humidityTextView);
        windSpeedTextView = findViewById(R.id.windSpeedTextView);
        weatherDetailImage = findViewById(R.id.weatherDetailImage);
        lastUpdatedTextView = findViewById(R.id.lastUpdatedTextView);

        // Tangkap data dari Intent
        if (getIntent() != null) {
            String temp = getIntent().getStringExtra("temp");
            String description = getIntent().getStringExtra("description");

            tempTextView.setText(String.format("Temperature: %s°C", temp != null ? temp : "--"));
            descriptionTextView.setText(String.format("Condition: %s", description != null ? description : "--"));
        }

        // Tambahkan listener untuk SearchView
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                fetchWeatherData(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private void fetchWeatherData(String city) {
        WeatherApiService apiService = ApiClient.getClient().create(WeatherApiService.class);
        Call<WeatherResponse> call = apiService.getWeather("610c8937ce09473d9c972811251501", city);

        call.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    WeatherResponse weather = response.body();

                    tempTextView.setText(String.format("Temperature: %.1f°C", weather.current.tempC));
                    descriptionTextView.setText(String.format("Condition: %s", weather.current.condition.text));
                    humidityTextView.setText(String.format("%d%%", weather.current.humidity));
                    windSpeedTextView.setText(String.format("%.1f kph", weather.current.windKph));
                    lastUpdatedTextView.setText(weather.current.lastUpdated);

                    Glide.with(CuacaActivity.this)
                            .load("https:" + weather.current.condition.icon)
                            .into(weatherDetailImage);
                } else {
                    Toast.makeText(CuacaActivity.this, "City not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                Toast.makeText(CuacaActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
