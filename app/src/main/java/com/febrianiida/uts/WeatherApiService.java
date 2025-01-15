package com.febrianiida.uts;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherApiService {
    @GET("current.json")
    Call<WeatherResponse> getWeather(
            @Query("key") String apiKey,
            @Query("q") String cityName
    );
}
