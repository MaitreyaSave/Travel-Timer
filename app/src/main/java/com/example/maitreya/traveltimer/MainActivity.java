package com.example.maitreya.traveltimer;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

public class MainActivity extends Activity {
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 0;
    static final int MAP_SOURCE_REQ = 0;  // The request code for source
    static final int MAP_DESTINATION_REQ = 1;  // The request code for source
    boolean destinationYN=false;
    //Delete below
    TextView v1,v2,v3;
    //
    LatLng source,destination;
    LocationManager locationManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        v1= (TextView) findViewById(R.id.textView);
        v2=(TextView) findViewById(R.id.textView2);
        v3=(TextView) findViewById(R.id.textView3);
    }
    private void showGPSDisabledAlertToUser(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("GPS is disabled in your device. Would you like to enable it?")
                .setCancelable(false)
                .setPositiveButton("Open Location Settings",
                        new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int id){
                                Intent callGPSSettingIntent = new Intent(
                                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(callGPSSettingIntent);
                            }
                        });
        alertDialogBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id){
                        dialog.cancel();
                    }
                });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void mapSD(View v) {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_LOCATION);
        }
        else{
            //Toast.makeText(this,"hasPermission",Toast.LENGTH_SHORT).show();
            /*
            Intent callGPSSettingIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            int x=0;
            this.startActivityForResult(callGPSSettingIntent,x);
            Toast.makeText(this,"value = "+x,Toast.LENGTH_SHORT).show();
            */
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                Toast.makeText(this, "GPS is Enabled in your device", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, MapsActivity.class);
                if(destinationYN)
                    startActivityForResult(intent,MAP_DESTINATION_REQ);
                else
                    startActivityForResult(intent,MAP_SOURCE_REQ);
            }
            else{
                showGPSDisabledAlertToUser();
            }
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void mapD(View v){
        destinationYN=true;
        mapSD(v);
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void mapS(View v){
        destinationYN=false;
        mapSD(v);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    /*
                    Toast.makeText(this,"onReq",Toast.LENGTH_SHORT).show();
                    Intent callGPSSettingIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);

                    this.startActivityForResult(callGPSSettingIntent,x);
                    */
                    if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                        Toast.makeText(this, "GPS is Enabled in your devide", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(this, MapsActivity.class);
                        startActivityForResult(intent,MAP_SOURCE_REQ);
                    }
                    else{
                        showGPSDisabledAlertToUser();
                    }
                } else {
                    Toast.makeText(this,"Sorry! Location Access Permission needed!",Toast.LENGTH_SHORT).show();
                    //nothing can be done
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == MAP_SOURCE_REQ) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                Bundle b=data.getExtras();
                source= (LatLng) b.get("marker_latlng");
                v1.setText("source "+source);
                //Toast.makeText(this,"source "+source,Toast.LENGTH_SHORT).show();
            }
        }
        else if(requestCode == MAP_DESTINATION_REQ){
            if(resultCode == RESULT_OK){
                Bundle b=data.getExtras();
                destination= (LatLng) b.get("marker_latlng");
                v2.setText("destination "+destination);
                //Toast.makeText(this,"destination "+destination,Toast.LENGTH_SHORT).show();
            }
        }
    }
    public void calDis(View v){
        if(source!=null&&destination!=null) {
            Location s = new Location("S");
            Location d = new Location("D");
            s.setLatitude(source.latitude);
            s.setLongitude(source.longitude);
            d.setLongitude(destination.longitude);
            d.setLatitude(destination.latitude);
            float disf = s.distanceTo(d);
            int dis= (int) disf;
            v3.setText("Distance = " + dis/1000.0+ " kms");
        }
        else {
            if(source==null)
                Toast.makeText(this,"Source not selected!",Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(this,"Destination not selected!",Toast.LENGTH_SHORT).show();
        }
    }

}
