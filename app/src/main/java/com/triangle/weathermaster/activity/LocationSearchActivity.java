package com.triangle.weathermaster.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.triangle.weathermaster.R;
import com.triangle.weathermaster.adapters.LocationsAdapter;
import com.triangle.weathermaster.api.ApiClient;
import com.triangle.weathermaster.api.ApiConstants;
import com.triangle.weathermaster.api.WeatherService;
import com.triangle.weathermaster.models.api.WeatherResponseModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LocationSearchActivity extends AppCompatActivity {

    private RecyclerView mRecycleView;
    private ArrayList<WeatherResponseModel> mLocations;
    private EditText mSearch;
    WeatherService mServiceInterface;

    LocationsAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_search);

        mSearch = findViewById(R.id.edit_search);
        mRecycleView = findViewById(R.id.recycleview_locations);

        mSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                searchLocations(s.toString());
            }
        });

        startRecycleView();
    }

    private void startRecycleView(){
        mLocations = new ArrayList<>();
        mAdapter = new LocationsAdapter(mLocations, LocationSearchActivity.this);
        mAdapter.isSearch = true;
        mRecycleView.setLayoutManager(new LinearLayoutManager(this));
        mRecycleView.setAdapter(mAdapter);
    }

    private void searchLocations(String q) {
        mLocations = new ArrayList<>();
        try {
            mServiceInterface = ApiClient.getClient().create(WeatherService.class);
            Call<WeatherResponseModel> responseModelCall = mServiceInterface.getWeatherByCity(
                    new HashMap<String, String>() {{
                        put("q", q);
                        put("appid", ApiConstants.API_KEY);
                        put("units", "metric");
                    }}
            );

            Log.e("hjhj", responseModelCall.request().url().toString());

            responseModelCall.enqueue(new Callback<WeatherResponseModel>() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onResponse(@NotNull Call<WeatherResponseModel> call, @NotNull Response<WeatherResponseModel> response) {
                    Log.e("hjhj", "dfa;jksdfasd");
                    WeatherResponseModel model = response.body();
                    if (model != null){
                        mLocations.add(model);
                        mAdapter.setLocations(mLocations);
                        mAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onFailure(@NotNull Call<WeatherResponseModel> call, @NotNull Throwable t) {
                    Log.e("hjhj", responseModelCall.request().url().toString());
                }

            });
        }
        catch (Exception ex){
            Log.e("dafdsa", ex.getMessage());
        }
    }
}