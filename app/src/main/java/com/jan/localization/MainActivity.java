package com.jan.localization;

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

public class MainActivity extends Activity implements LocationListener{
    protected LocationManager locationManager;
    protected LocationListener locationListener;
    protected Context context;
    TextView txtLon;
    TextView txtLat;
    TextView speed;
    TextView altitude;
    boolean flag = true;
    String lat;
    String provider;
    protected String latitude,longitude;
    protected boolean gps_enabled,network_enabled;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {  //&& ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
            Toast.makeText(MainActivity.this, "First enable LOCATION ACCESS in settings.", Toast.LENGTH_LONG).show();
            return;
        }
        speed = (TextView) findViewById(R.id.speed);
        Location lastKnownLocationLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        speed.setText("Waiting for GPS");


        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);

    }
    @Override
    public void onLocationChanged(Location location) {
        if (flag) {
            Toast.makeText(MainActivity.this, "connected", Toast.LENGTH_SHORT).show();
            flag = false;
        }
        txtLon = (TextView) findViewById(R.id.longitude);
        txtLat = (TextView) findViewById(R.id.latitude);
        altitude = (TextView) findViewById(R.id.altitude);
        txtLon.setText("Longitude: " + String.valueOf(location.getLongitude()).substring(0,7));
        txtLat.setText("Latitude: "  + String.valueOf(location.getLatitude()).substring(0,7));
        altitude.setText("Altitude: " + String.valueOf(location.getAltitude()));
        speed = (TextView) findViewById(R.id.speed);
        float speed = location.getSpeed();
        this.speed.setText(String.valueOf(speed * 3.6).substring(0,3) + " km/h");
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.d("Latitude","disable");
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d("Latitude","enable");
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d("Latitude","status");
    }
}