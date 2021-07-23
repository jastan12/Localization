package com.jan.localization;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.Lifecycle;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class MainDisplay extends AppCompatActivity implements LocationListener {
    protected LocationManager locationManager;
    MainDisplaySwipeManager mainDisplaySwipeManager;

    private boolean kmph = true;
    private boolean mps;
    private boolean kts;
    private double maxSpeed;
    private boolean tracking;
    private double distanceInMeters;
    private double startLatitude;
    private double startLongitude;
    private double endLatitude;
    private double endLongitude;
    private double startAltitude;
    private double endAltitude;
    private double orthodrome;
    private double horizontalDistanceInMeters;
    private float accuracy;
    private boolean flag;

    private TextView txtMaxSpeed;
    private TextView txtLat;
    private TextView txtLon;
    private TextView txtAlt;
    private TextView txtSpeed;
    private TextView txtDistance;
    private TextView txtAccuracy;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_display);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {  //&& ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
            Toast.makeText(MainDisplay.this, "First enable LOCATION ACCESS in settings.", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(getApplicationContext(), MainActivity.class); // go to MainActivity
            startActivity(intent);
//            return;
        }
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 0, this);
        txtSpeed = findViewById(R.id.speed);
        txtMaxSpeed = findViewById(R.id.max_speed);
        txtLon = findViewById(R.id.longitude);
        txtLat = findViewById(R.id.latitude);
        txtAlt = findViewById(R.id.altitude);
        txtAccuracy = findViewById(R.id.accuracy);
        txtDistance = findViewById(R.id.distanceText);
        txtSpeed.setText("Waiting for GPS");

//        swipe:
        ConstraintLayout layout = findViewById(R.id.mainDisplay);
        mainDisplaySwipeManager = new MainDisplaySwipeManager(this, layout);

        Button clearDistanceButton = findViewById(R.id.clearButton);
        ToggleButton trackDistanceToggleButton = findViewById(R.id.trackToggleButton);

        setListenerOnClearButton(clearDistanceButton);
        setListenerOnTrackToggleButton(trackDistanceToggleButton);

    }

    @Override
    public void onLocationChanged(Location location) {
        txtAccuracy.setText("Accuracy: " + String.valueOf(location.getAccuracy()));
        txtLon.setText("Longitude: " + String.valueOf(location.getLongitude()).substring(0,7));
        txtLat.setText("Latitude: "  + String.valueOf(location.getLatitude()).substring(0,7));
        txtAlt.setText("Altitude: " + returnRounded(location.getAltitude() - 30.) + " m");

        double speed = location.getSpeed();
        maxSpeed(speed);
        if (kmph) {
            txtSpeed.setText(returnRounded(speed * 3.6) + " km/h");
            txtMaxSpeed.setText("max: " + returnRounded(maxSpeed * 3.6) + " km/h");
        }else if (mps) {
            txtSpeed.setText(returnRounded(speed) + " m/s");
            txtMaxSpeed.setText("max: " + returnRounded(maxSpeed) + " m/s");
        }else  if(kts){
            txtSpeed.setText(returnRounded(speed * 3.6 / 1.852) + " kts");
            txtMaxSpeed.setText("max: " + returnRounded(maxSpeed * 3.6 / 1.852) + " kts");
        }


        if (tracking && location.getAccuracy() < 5)
            measureDistance(location.getLatitude(), location.getLongitude(), location.getAltitude());

    }

    private void measureDistance(double latitude, double longitude, double altitude){
        if (!flag){
            endLatitude = latitude;
            endLongitude = longitude;
            endAltitude = altitude;
            flag = true;
        }
        startLatitude = endLatitude;
        startLongitude = endLongitude;
        startAltitude = endAltitude;
        endLatitude = latitude;
        endLongitude = longitude;
        endAltitude = altitude;
        orthodrome = Math.acos((Math.sin(startLatitude) * Math.sin(endLatitude)) +
                (Math.cos(startLatitude) * Math.cos(endLatitude) * Math.cos(endLongitude - startLongitude)));
        horizontalDistanceInMeters = orthodrome * 111.195 * 1000;
//        distanceInMeters += Math.sqrt(Math.pow(horizontalDistanceInMeters, 2) + Math.pow(endAltitude - startAltitude, 2));
        distanceInMeters += horizontalDistanceInMeters;
        txtDistance.setText(convertDistanceValueToString(distanceInMeters) + " m");

    }

    private static String returnRounded(double inValue){
        return (inValue < 100 ? String.valueOf(inValue).substring(0,4) : String.valueOf(inValue).split("\\.")[0]);
    }

    private void maxSpeed(double speed){
        if (speed > maxSpeed)  maxSpeed = speed;
    }

    @Override
    public void onProviderDisabled(String provider) {
        if (getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.RESUMED))
            Toast.makeText(MainDisplay.this, "no gps", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onProviderEnabled(String provider) {
        if (getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.RESUMED))
            Toast.makeText(MainDisplay.this, "gps enabled", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }

    private void setListenerOnClearButton(Button clearDistanceButton){
        clearDistanceButton.setOnClickListener(v -> {
            if (!tracking) {
                txtDistance.setText("");
                distanceInMeters = 0.;
            }
        });
    }

    private void setListenerOnTrackToggleButton(ToggleButton trackDistanceToggleButton){
        trackDistanceToggleButton.setOnClickListener(v -> {
            if (!tracking) {
                tracking = true;
            }else {
                tracking = false;
                flag = false;
            }
        });
    }


    private String convertDistanceValueToString(double distance){
        String stringDistance = String.valueOf(distance);
        int i;
        for (i = 0; i < stringDistance.length(); i++) {
            if (stringDistance.charAt(i) == '.') break;
        }
        if (distance < 1000 && distance > 0){
            return stringDistance.substring(0,i+2);
        } else if (distance >= 1000){
            return stringDistance.substring(0, i-3) + " " + stringDistance.substring(i-3, i);
        } else
            return stringDistance;

    }

}
