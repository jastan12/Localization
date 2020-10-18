package com.jan.localization;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

public class KilometersPerHour extends Activity implements LocationListener {
    protected LocationManager locationManager;
    TextView txtLon;
    TextView txtLat;
    TextView speed;
    TextView txtAlt;
    boolean flag = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kilometer_per_hour);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {  //&& ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
            Toast.makeText(KilometersPerHour.this, "First enable LOCATION ACCESS in settings.", Toast.LENGTH_LONG).show();
            return;
        }
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);   // moje 5 linijek
        speed = (TextView) findViewById(R.id.speed);
        Location lastKnownLocationLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        speed.setText("Waiting for GPS");
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
    }



    @Override
    public void onLocationChanged(Location location) {
        if (flag) {
            Toast.makeText(KilometersPerHour.this, "connected", Toast.LENGTH_SHORT).show();
            flag = false;
        }
        txtLon = (TextView) findViewById(R.id.longitude);
        txtLat = (TextView) findViewById(R.id.latitude);
        txtAlt = (TextView) findViewById(R.id.altitude);
        txtLon.setText("Longitude: " + String.valueOf(location.getLongitude()).substring(0,7));
        txtLat.setText("Latitude: "  + String.valueOf(location.getLatitude()).substring(0,7));
        txtAlt.setText("Altitude: " + returnRounded(location.getAltitude() - 30.) + " m");
        speed = (TextView) findViewById(R.id.speed);
        float speed = location.getSpeed();
        this.speed.setText(returnRounded(speed * 3.6) + " km/h");
    }

    private static String returnRounded(Double inValue){
        return (inValue < 10 ? String.valueOf(inValue).substring(0,3) : String.valueOf(inValue).split("\\.")[0]);
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.d("Latitude","disable");
        Toast.makeText(KilometersPerHour.this, "no gps", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d("Latitude","enable");
        Toast.makeText(KilometersPerHour.this, "connected", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d("Latitude","status");
    }

    @Override
    public void onBackPressed() {
        // super.onBackPressed();
        finishAffinity();
    }

}
