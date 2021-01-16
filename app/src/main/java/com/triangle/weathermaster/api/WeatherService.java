package com.triangle.weathermaster.api;

import com.triangle.weathermaster.models.api.WeatherResponseModel;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

public interface WeatherService {
    @GET("/data/2.5/weather?")
    Call<WeatherResponseModel> getWeatherByCity(@QueryMap Map<String, String> options);
}
