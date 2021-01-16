package com.triangle.weathermaster.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.triangle.weathermaster.R;
import com.triangle.weathermaster.api.ApiClient;
import com.triangle.weathermaster.api.ApiConstants;
import com.triangle.weathermaster.api.WeatherService;
import com.triangle.weathermaster.helpers.LocationHelper;
import com.triangle.weathermaster.models.api.WeatherResponseModel;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private TextView mLocation;
    private TextView mDateTime;
    private TextView mTemp;
    private TextView mHighTemp;
    private TextView mLowTemp;
    private TextView mHumidity;
    private TextView mPressure;
    private Button mViewOnMap;
    private Button mLocationsBtn;
    private ImageView mWeatherIcon;
    private String defaultLocation;
    private ImageView mRefreshImage;

    WeatherService mServiceInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        loadData();
    }

    private void initViews() {
        mLocation = findViewById(R.id.txt_location);
        mDateTime = findViewById(R.id.txt_date);
        mTemp = findViewById(R.id.txt_temp);
        mHighTemp = findViewById(R.id.txt_high);
        mLowTemp = findViewById(R.id.txt_low);
        mHumidity = findViewById(R.id.txt_humidity);
        mPressure = findViewById(R.id.txt_pressure);
        mViewOnMap = findViewById(R.id.btn_view_on_map);
        mWeatherIcon = findViewById(R.id.img_weather_icon);
        mLocationsBtn = findViewById(R.id.btn_locations);
        mRefreshImage = findViewById(R.id.img_refresh);

        mRefreshImage.setOnClickListener(v -> {
            loadData();
        });

        mLocationsBtn.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, LocationsActivity.class));
        });
    }

    private void loadData() {

        LocationHelper helper = new LocationHelper();
        defaultLocation = helper.getDefaultLocation(this);
        try {
            mServiceInterface = ApiClient.getClient().create(WeatherService.class);
            Call<WeatherResponseModel> responseModelCall = mServiceInterface.getWeatherByCity(
                    new HashMap<String, String>() {{
                        put("q", defaultLocation);
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
                        Log.e("hjhj", "https://openweathermap.org/img/wn/"+model.getWeather().get(0).getIcon()+"@2x.png");
                        mLocation.setText(model.getName() + " (" + model.getSys().getCountry()+")");
                        mHighTemp.setText("Low Temp : " + model.getMain().getTempMin() + " ℃");
                        mLowTemp.setText("Max Temp : " + model.getMain().getTempMax() + " ℃");
                        mDateTime.setText(model.getWeather().get(0).getDescription());
                        mTemp.setText(model.getMain().getTemp() + " ℃");
                        mHumidity.setText(model.getMain().getHumidity() + " g.kg-1");
                        mPressure.setText(model.getMain().getPressure() + " Pa");
                        Glide.with(MainActivity.this)
                                .load("https://openweathermap.org/img/wn/"+model.getWeather().get(0).getIcon()+".png")
                                .centerCrop()
                                .placeholder(R.drawable.clouds)
                                .into(mWeatherIcon);

                        mViewOnMap.setOnClickListener(v -> {
                            Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                            intent.putExtra("location", model.getName() + " - " + model.getMain().getTemp() + " ℃");
                            intent.putExtra("latitude", model.getCoord().getLat());
                            intent.putExtra("longitude", model.getCoord().getLon());
                            startActivity(intent);
                        });

                    }
                }

                @Override
                public void onFailure(@NotNull Call<WeatherResponseModel> call, @NotNull Throwable t) {
                    Log.e("hjhj", responseModelCall.request().url().toString());
                    Toast.makeText(MainActivity.this, "error", Toast.LENGTH_LONG).show();
                }

            });
        }
        catch (Exception ex){
            Log.e("dafdsa", ex.getMessage());
        }
    }
}