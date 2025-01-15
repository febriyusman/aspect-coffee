package com.febrianiida.uts;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CuacaFragment extends Fragment {
    private TextView tempTextView, descriptionTextView;
    private SearchView searchView;
    private TextView humidityTextView, windSpeedTextView, lastUpdatedTextView;
    private ImageView weatherDetailImage;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cuaca, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tempTextView = view.findViewById(R.id.tempTextView);
        descriptionTextView = view.findViewById(R.id.descriptionTextView);
        searchView = view.findViewById(R.id.searchView);

        humidityTextView = view.findViewById(R.id.humidityTextView);
        windSpeedTextView = view.findViewById(R.id.windSpeedTextView);
        weatherDetailImage = view.findViewById(R.id.weatherDetailImage);
        lastUpdatedTextView = view.findViewById(R.id.lastUpdatedTextView);

        if (getArguments() != null) {
            tempTextView.setText(String.format("Temperature: %s°C", getArguments().getString("temp", "N/A")));
            descriptionTextView.setText(String.format("Condition: %s", getArguments().getString("description", "N/A")));
        }

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                // Handle search query here
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

                    Glide.with(requireContext())
                            .load("https:" + weather.current.condition.icon)
                            .into(weatherDetailImage);
                } else {
                    Toast.makeText(getContext(), "City not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}