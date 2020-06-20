package com.team.mobileworld.core.handle;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.team.mobileworld.activity.MainActivity;
import com.team.mobileworld.core.task.Worker;

public class LocationInfo {
    public static LatLng latLng = new LatLng(21.00, 105.80);
    public static Activity activity;
    public static Worker worker;

    public static void register(Activity activity) {
        LocationInfo.activity = activity;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (activity.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity, new String[]{
                        Manifest.permission.ACCESS_COARSE_LOCATION
                        , Manifest.permission.ACCESS_FINE_LOCATION
                }, MainActivity.REQUEST_LOCATION);
            } else
                getCurrentLocation();
        }
    }

    public static void getCurrentLocation() {
        LocationServices.getFusedLocationProviderClient(activity)
                .getLastLocation().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Location location = task.getResult();
                if (location != null) {
                    latLng = new LatLng(location.getLatitude(), location.getLongitude());
                    Log.d("location", latLng.toString());
                }
            }
            if (worker != null) worker.hanlde();

        });
    }

    public static LatLng getLatLng() {
        return latLng;
    }
}
