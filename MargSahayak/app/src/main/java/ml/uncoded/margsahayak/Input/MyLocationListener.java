package ml.uncoded.margsahayak.Input;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import ml.uncoded.margsahayak.R;

public class MyLocationListener implements LocationListener {
    private static final String TAG = "MyLocationListener";
    public String lat,lon;
    public int acc = 1000;
    private static MyLocationListener single_instance = null;

    public static MyLocationListener getInstance()
    {
        if (single_instance == null)
            single_instance = new MyLocationListener();

        return single_instance;
    }

    private MyLocationListener(){}

    @Override
    public void onLocationChanged(Location loc) {

        String longitude = "Longitude: " + loc.getLongitude();
        String latitude = "Latitude: " + loc.getLatitude();
        Log.d(TAG, longitude + " " + latitude + " " + loc.getAccuracy());
        if(loc.getAccuracy() < acc){
            lon = String.valueOf(loc.getLongitude());
            lat = String.valueOf(loc.getLatitude());
            Log.d(TAG, "Updated : " + lon + " " + lat + " " + loc.getAccuracy());

        }
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.d(TAG,"onProviderDisabled");

    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d(TAG, "onProviderEnabled: ");
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {}
}