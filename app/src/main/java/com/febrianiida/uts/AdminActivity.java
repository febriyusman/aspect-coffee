package com.febrianiida.uts;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;
    private CardView weatherCard;
    private TextView weatherName, weatherTemp, weatherLocation;
    private ImageView weatherImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        weatherCard = findViewById(R.id.weatherCard);
        weatherName = findViewById(R.id.weatherName);
        weatherTemp = findViewById(R.id.weatherTemp);
        weatherLocation = findViewById(R.id.weatherLocation);
        weatherImage = findViewById(R.id.weatherImage);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        databaseHelper = new DatabaseHelper(this);

        // Load data dari database
        List<Product> products = getProductsFromDatabase();
        ProductAdapter adapter = new ProductAdapter(products);
        recyclerView.setAdapter(adapter);

        // Tombol Tambah Produk
        findViewById(R.id.addButton).setOnClickListener(v -> {
            Intent intent = new Intent(AdminActivity.this, MenuInput.class);
            startActivity(intent);
        });

        fetchWeatherData("pontianak");
    }

    private void fetchWeatherData(String city) {
        WeatherApiService apiService = ApiClient.getClient().create(WeatherApiService.class);
        Call<WeatherResponse> call = apiService.getWeather("610c8937ce09473d9c972811251501", city);

        call.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    updateWeatherCard(response.body());
                } else {
                    Toast.makeText(AdminActivity.this, "City not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                Toast.makeText(AdminActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateWeatherCard(WeatherResponse weather) {
        weatherName.setText(weather.current.condition.text);
        weatherTemp.setText(String.format("%.1f°C", weather.current.tempC));
        weatherLocation.setText(weather.location.name);

        Glide.with(this)
                .load("https:" + weather.current.condition.icon)
                .into(weatherImage);

        weatherCard.setOnClickListener(v -> openWeatherDetailFragment(weather));
    }


    private void openWeatherDetailFragment(WeatherResponse weather) {
        CuacaFragment fragment = new CuacaFragment();
        Bundle bundle = new Bundle();
        bundle.putString("temp", String.valueOf(weather.current.tempC));
        bundle.putString("description", weather.current.condition.text);
        fragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }
    private List<Product> getProductsFromDatabase() {
        List<Product> productList = new ArrayList<>();
        Cursor cursor = databaseHelper.getAllItems();

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                String description = cursor.getString(cursor.getColumnIndexOrThrow("description"));
                int price = cursor.getInt(cursor.getColumnIndexOrThrow("price"));
                String imageBase64 = cursor.getString(cursor.getColumnIndexOrThrow("image"));
                productList.add(new Product(id, name, description, price, imageBase64));
            } while (cursor.moveToNext());
            cursor.close();
        }

        return productList;
    }



    @Override
    protected void onResume() {
        super.onResume();
        List<Product> products = getProductsFromDatabase();
        ProductAdapter adapter = new ProductAdapter(products);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setAdapter(adapter);
    }

}
