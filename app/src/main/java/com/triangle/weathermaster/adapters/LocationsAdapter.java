package com.triangle.weathermaster.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.triangle.weathermaster.R;
import com.triangle.weathermaster.activity.MainActivity;
import com.triangle.weathermaster.helpers.LocationHelper;
import com.triangle.weathermaster.models.api.WeatherResponseModel;

import java.util.ArrayList;
import java.util.List;

public class LocationsAdapter extends RecyclerView.Adapter<LocationsAdapter.LocationViewHolder> {
    private ArrayList<WeatherResponseModel> mLocations;
    private Activity context;
    public boolean isSearch = false;

    public LocationsAdapter(ArrayList<WeatherResponseModel> mLocations, Activity context) {
        this.mLocations = mLocations;
        this.context = context;
    }

    public void setLocations(ArrayList<WeatherResponseModel> mLocations) {
        this.mLocations = mLocations;
    }

    @NonNull
    @Override
    public LocationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mMainView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_location, parent, false);
        return new LocationViewHolder(mMainView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull LocationViewHolder holder, int position) {
        WeatherResponseModel model = mLocations.get(position);
        if (model != null){
            // Icon
            Glide.with(context)
                    .load("https://openweathermap.org/img/wn/"+model.getWeather().get(0).getIcon()+".png")
                    .centerCrop()
                    .placeholder(R.drawable.clouds)
                    .into(holder.icon);

            holder.locationName.setText(model.getName() + " (" + model.getSys().getCountry()+")");
            holder.description.setText(model.getWeather().get(0).getDescription());
            holder.shortDescription.setText(model.getWeather().get(0).getMain());
            holder.temp.setText(model.getMain().getTemp() + " ℃");

            if (new LocationHelper().getDefaultLocation((Activity) context).equals(model.getName())){
                holder.layout.setBackgroundColor(context.getColor(R.color.colorAccent));
            }

            holder.layout.setOnClickListener(v -> {
                LocationHelper helper = new LocationHelper();
                if (isSearch) {
                    helper.saveLocation((Activity) context, model.getName());
                }
                helper.setDefaultLocation((Activity) context, model.getName());
                ((Activity) context).finish();
            });
        }
    }

    @Override
    public int getItemCount() {
        return mLocations.size();
    }


    static class LocationViewHolder extends RecyclerView.ViewHolder{
        private ImageView icon;
        private TextView locationName;
        private TextView temp;
        private TextView description;
        private TextView shortDescription;
        private ConstraintLayout layout;


        public LocationViewHolder(@NonNull View itemView) {
            super(itemView);

            icon = itemView.findViewById(R.id.item_icon);
            locationName = itemView.findViewById(R.id.item_location_name);
            temp = itemView.findViewById(R.id.item_temp);
            description = itemView.findViewById(R.id.item_location_description);
            shortDescription = itemView.findViewById(R.id.item_location_short_description);
            layout = itemView.findViewById(R.id.item_layout);
        }
    }
}
