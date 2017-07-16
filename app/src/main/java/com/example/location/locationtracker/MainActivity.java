package com.example.location.locationtracker;

import android.app.ActivityManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.location.locationtracker.service.LocationService;

import static com.example.location.locationtracker.LocationStatusManager.GPS_SETTING_ON;

public class MainActivity extends AppCompatActivity implements OnLocationCallback{

    public static String LOCATION = "android.permission.ACCESS_FINE_LOCATION";
    public static int LOCATION_REQUEST_CODE = 1;
    public static final String MY_PREFERENCES = "MyPrefs" ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkLocationPermission();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void startTracking(View view) {
        startTracking();
    }

    private void startTracking() {
        LocationStatusManager locationStatusManager = new LocationStatusManager(this, this);
        locationStatusManager.checkGpsStatus();
    }

    public void stopTracking(View view) {

        if (isServiceRunning()) {
            Intent service = new Intent(MainActivity.this, LocationService.class);
            stopService(service);
        } else {
            Toast.makeText(this, "CustomLocation Service not running", Toast.LENGTH_SHORT).show();
        }

    }

    private boolean isServiceRunning() {
        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (LocationService.LOCATION_SERVICE.equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    private void checkLocationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{LOCATION}, LOCATION_REQUEST_CODE);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        boolean locationAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
        if (locationAccepted == false) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, LOCATION)) {
                //permission just denied
                checkLocationPermission();
            } else {
                //permission denied permanently
                showAlertDialog();
            }
        }

    }

    private void showAlertDialog() {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(this, android.R.style.Theme_DeviceDefault_Light_Dialog);
        } else {
            builder = new AlertDialog.Builder(this);
        }
        builder.setTitle("Location permission")
                .setMessage("Location permission is required in order to run this app.")
                .setCancelable(false)
                .setPositiveButton("SETTINGS", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent myAppSettings = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + getPackageName()));
                        myAppSettings.addCategory(Intent.CATEGORY_DEFAULT);
                        startActivityForResult(myAppSettings, LOCATION_REQUEST_CODE);
                        dialog.dismiss();
                    }
                })
                .show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case GPS_SETTING_ON:
                onLocationOn();
                break;
            default:
                checkLocationPermission();
                break;
        }
    }

    @Override
    public void onLocationOn() {

        if (!isServiceRunning()) {
            Intent service = new Intent(MainActivity.this, LocationService.class);
            startService(service);
        } else {
            Toast.makeText(this, "CustomLocation Service already running", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onLocationOff() {

    }
}
