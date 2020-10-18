package com.jan.localization;

        import android.Manifest;
        import android.app.AlertDialog;
        import android.content.DialogInterface;
        import android.content.Intent;
        import android.content.pm.PackageManager;
        import android.os.Bundle;
        import android.app.Activity;
        import android.content.Context;
        import android.location.Location;
        import android.location.LocationListener;
        import android.location.LocationManager;
        import android.widget.TextView;

        import android.util.Log;
        import android.widget.Toast;


        import androidx.core.app.ActivityCompat;
        import androidx.core.content.ContextCompat;

        import java.util.logging.Logger;

public class MainActivity extends Activity implements LocationListener {
    protected LocationManager locationManager;
    protected LocationListener locationListener;
    protected Context context;
    TextView txtLon;
    TextView txtLat;
    TextView speed;
    TextView txtAlt;
    boolean flag = true;
    String lat;
    String provider;
    protected String latitude,longitude;
    protected boolean gps_enabled,network_enabled;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {  //&& ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
//            Toast.makeText(MainActivity.this, "First enable LOCATION ACCESS in settings.", Toast.LENGTH_LONG).show();
//            return;
            Intent intent = new Intent(getApplicationContext(),KilometersPerHour.class); // przejscie do drugiego activity
            startActivity(intent);
        }

//        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//        speed = (TextView) findViewById(R.id.speed);
//        Location lastKnownLocationLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//        speed.setText("Waiting for GPS");
//
//        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        checkLocationPermission();
//        checkLocationPermission2();
    }


    @Override
    public void onLocationChanged(Location location) {
        if (flag) {
            Toast.makeText(MainActivity.this, "connected", Toast.LENGTH_SHORT).show();
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
        Toast.makeText(MainActivity.this, "no gps", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d("Latitude","enable");
        Toast.makeText(MainActivity.this, "connected", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d("Latitude","status");
    }

///////////////////////////////////////////////////////////////////////////////////////////////////////


//    public void  checkLocationPermission2(){
//        ActivityCompat.requestPermissions(this,
//                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
//                1);
//
//    }
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle("R.string.title_location_permission")
                        .setMessage("R.string.text_location_permission")
                        .setPositiveButton("R.string.ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(MainActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        Log.w("Request code:",String.valueOf(requestCode));

        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.


                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        //Request location updates:
//                        locationManager.requestLocationUpdates(provider, 400, 1, this);  // oryginalna linijka

                        Intent intent = new Intent(getApplicationContext(),KilometersPerHour.class); //przejscie do drugiego activity
                        startActivity(intent);

//                        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);   // moje 5 linijek
//                        speed = (TextView) findViewById(R.id.speed);
//                        Location lastKnownLocationLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//                        speed.setText("Waiting for GPS");
//                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);

                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.

                }
                return;
            }

        }
    }

}