package com.triangle.weathermaster.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.triangle.weathermaster.R;
import com.triangle.weathermaster.adapters.LocationsAdapter;
import com.triangle.weathermaster.api.ApiClient;
import com.triangle.weathermaster.api.ApiConstants;
import com.triangle.weathermaster.api.WeatherService;
import com.triangle.weathermaster.helpers.LocationHelper;
import com.triangle.weathermaster.models.api.WeatherResponseModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LocationsActivity extends AppCompatActivity {
    private ArrayList<WeatherResponseModel> mLocations;
    private FloatingActionButton mFAB;
    private RecyclerView mRecycleView;
    LocationsAdapter mAdapter;
    WeatherService mServiceInterface;
    private SwipeRefreshLayout mSwipeRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locations);

        mFAB = findViewById(R.id.fab_locations);
        mRecycleView = findViewById(R.id.recyleview_locations);
        mSwipeRefresh = findViewById(R.id.swipe_refresh);

        mFAB.setOnClickListener(v -> {
            startActivity(new Intent(LocationsActivity.this, LocationSearchActivity.class));
        });

        mSwipeRefresh.setOnRefreshListener(() -> {
            loadLocations();
            mSwipeRefresh.setRefreshing(false);
        });

        startRecycleView();
        loadLocations();
    }

    private void startRecycleView(){
        mLocations = new ArrayList<>();
        mAdapter = new LocationsAdapter(mLocations, LocationsActivity.this);
        mRecycleView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRecycleView.setAdapter(mAdapter);
    }

    private void loadLocations() {
        mLocations = new ArrayList<>();
        List<String> locations = new LocationHelper().getLocationNames(LocationsActivity.this);

        if (locations.size() == 0){
            mSwipeRefresh.setRefreshing(false);
        }
        try {
            for (String i : locations){
                if (!i.trim().equals("")) {
                    mServiceInterface = ApiClient.getClient().create(WeatherService.class);
                    Call<WeatherResponseModel> responseModelCall = mServiceInterface.getWeatherByCity(
                            new HashMap<String, String>() {{
                                put("q", i);
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
                            if (model != null) {
                                mLocations.add(model);
                                mAdapter.setLocations(mLocations);
                                mAdapter.notifyDataSetChanged();

                                mSwipeRefresh.setRefreshing(false);
                            }
                        }

                        @Override
                        public void onFailure(@NotNull Call<WeatherResponseModel> call, @NotNull Throwable t) {
                            Log.e("hjhj", responseModelCall.request().url().toString());
                            Toast.makeText(LocationsActivity.this, "error", Toast.LENGTH_LONG).show();
                            mSwipeRefresh.setRefreshing(false);
                        }

                    });
                }
                else{
                    mSwipeRefresh.setRefreshing(false);
                }
            }

        }
        catch (Exception ex){
            Log.e("dafdsa", ex.getMessage());
            mSwipeRefresh.setRefreshing(false);
        }
    }
}