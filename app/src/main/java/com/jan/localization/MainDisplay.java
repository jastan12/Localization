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

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;

public class MainDisplay extends Activity implements LocationListener {
    protected LocationManager locationManager;
    private TextView txtLat;
    private TextView txtLon;
    private TextView txtAlt;
    private TextView txtSpeed;
    private static TextView txtMaxSpeed;

    private static boolean flag = true;
    private static boolean kmph = true;
    private static boolean mps;
    private static boolean ktsph;
    private static double maxSpeed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_display);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {  //&& ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
            Toast.makeText(MainDisplay.this, "First enable LOCATION ACCESS in settings.", Toast.LENGTH_LONG).show();
            return;
        }
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);   // moje 5 linijek
        txtSpeed = (TextView) findViewById(R.id.speed);
        Location lastKnownLocationLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        txtSpeed.setText("Waiting for GPS");
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);


/////////   swipe:
        ConstraintLayout layout = findViewById(R.id.mainDisplay);
        layout.setOnTouchListener(new OnSwipeTouchListener(MainDisplay.this) {
            @Override
            public void onSwipeLeft() {
                super.onSwipeLeft();
//                Toast.makeText(KilometersPerHour.this, "Swipe Left gesture detected", Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(getApplicationContext(),MetersPerSecond.class); // przejscie do drugiego activity
//                startActivity(intent);
                if (kmph){
                    kmph = false;
                    mps = true;
                }else if(mps){
                    mps = false;
                    ktsph = true;
                }
            }
            @Override
            public void onSwipeRight() {
                super.onSwipeRight();
//                Toast.makeText(KilometersPerHour.this, "Swipe Right gesture detected", Toast.LENGTH_SHORT).show();
                if (mps){
                    mps = false;
                    kmph = true;
                }else if (ktsph){
                    ktsph = false;
                    mps = true;
                }

            }
        });
        ////////////////// swipe koniec
    }



    @Override
    public void onLocationChanged(Location location) {
        if (flag) {
            Toast.makeText(MainDisplay.this, "connected", Toast.LENGTH_SHORT).show();
            flag = false;
        }
        txtLon = (TextView) findViewById(R.id.longitude);
        txtLat = (TextView) findViewById(R.id.latitude);
        txtAlt = (TextView) findViewById(R.id.altitude);
        txtLon.setText("Longitude: " + String.valueOf(location.getLongitude()).substring(0,7));
        txtLat.setText("Latitude: "  + String.valueOf(location.getLatitude()).substring(0,7));
        txtAlt.setText("Altitude: " + returnRounded(location.getAltitude() - 30.) + " m");
        txtSpeed = (TextView) findViewById(R.id.speed);
        txtMaxSpeed = (TextView) findViewById(R.id.max_speed);
        double speed = location.getSpeed();
        maxSpeed(speed);
        if (kmph) {
            this.txtSpeed.setText(returnRounded(speed * 3.6) + " km/h");
            this.txtMaxSpeed.setText("max: " + returnRounded(maxSpeed * 3.6) + " km/h");
        }else if (mps) {
            this.txtSpeed.setText(returnRounded(speed) + " m/s");
            this.txtMaxSpeed.setText("max: " + returnRounded(maxSpeed) + " m/s");
        }else  if(ktsph){
            this.txtSpeed.setText(returnRounded(speed * 3.6 / 1.852) + " kts");
            this.txtMaxSpeed.setText("max: " + returnRounded(maxSpeed * 3.6 / 1.852) + " kts");
        }

    }

    private static String returnRounded(Double inValue){
        return (inValue < 100 ? String.valueOf(inValue).substring(0,3) : String.valueOf(inValue).split("\\.")[0]);
    }

    private void maxSpeed(double speed){
        if (speed > maxSpeed)  maxSpeed = speed;
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.d("Latitude","disable");
        Toast.makeText(MainDisplay.this, "no gps", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d("Latitude","enable");
        Toast.makeText(MainDisplay.this, "connected", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d("Latitude","status");
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        maxSpeed = 0.0;
//    }

//    @Override
//    protected void onRestart() {
//        super.onRestart();
//        maxSpeed = 0.0;
//    }

//    @Override
//    protected void onPause() {
//        super.onPause();
//    }
}
