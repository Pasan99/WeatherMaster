package com.triangle.weathermaster.helpers;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.triangle.weathermaster.R;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class LocationHelper {
    public void saveLocation(Activity activity, String location){
        SharedPreferences sharedPref = activity.getSharedPreferences(
                "Main", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        String[] locations = Objects.requireNonNull(sharedPref.getString("locations", "")).trim().split(",");
        ArrayList<String> locationList = new ArrayList<>(Arrays.asList(locations));
        locationList.add(location);

        String result = StringUtils.join(locationList, ',');
        Log.e("dalfjjdaf", result);
        editor.putString("locations", result);
        editor.apply();
    }
    public ArrayList<String> getLocationNames(Activity activity){
        SharedPreferences sharedPref = activity.getSharedPreferences(
            "Main", Context.MODE_PRIVATE);

        String[] locations = Objects.requireNonNull(sharedPref.getString("locations", "")).trim().split(",");
        return new ArrayList<>(Arrays.asList(locations));
    }

    public void setDefaultLocation(Activity activity, String location){
        SharedPreferences sharedPref = activity.getSharedPreferences(
                "Main", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putString("defaultLocation", location);
        editor.apply();
    }

    public String getDefaultLocation(Activity activity){
        SharedPreferences sharedPref = activity.getSharedPreferences(
                "Main", Context.MODE_PRIVATE);
        return sharedPref.getString("defaultLocation", "Horana");
    }
}
