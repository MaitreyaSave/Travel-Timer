package com.example.maitreya.traveltimer;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.plus.Plus;

import java.text.DateFormat;
import java.util.Date;

import static com.google.android.gms.plus.Plus.SCOPE_PLUS_LOGIN;

public class MainActivity extends Activity {
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 0;
    static final int MAP_SOURCE_REQ = 0;  // The request code for source
    static final int MAP_DESTINATION_REQ = 1;  // The request code for source
    boolean destinationYN;
    //Delete below
    TextView v1, v2, v3, v4, v5;
    Context ctx=this;
    Intent global;
    //
    Vibrator v;
    float alarm_dis, actual_dis;
    static MediaPlayer mMediaPlayer;
    LatLng source, destination, currentLocation;
    LocationManager locationManager;
    //
    /*
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            Toast.makeText(context,"Received",Toast.LENGTH_SHORT).show();
            String message = intent.getStringExtra("Status");
            Bundle b = intent.getBundleExtra("Location");
            Location lastKnownLoc = b.getParcelable("Location");
            if (lastKnownLoc != null) {
                currentLocation=new LatLng(lastKnownLoc.getLatitude(),lastKnownLoc.getLongitude());
                Toast.makeText(context,message+"",Toast.LENGTH_SHORT).show();
            }

        }
    };
    */
    //

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        v1 = (TextView) findViewById(R.id.textView);
        v2 = (TextView) findViewById(R.id.textView2);
        v3 = (TextView) findViewById(R.id.textView3);
        v4 = (TextView) findViewById(R.id.textView4);
        v5 = (TextView) findViewById(R.id.textView5);
        mMediaPlayer = new MediaPlayer();
        /*
        LocalBroadcastManager.getInstance(this).registerReceiver(
                mMessageReceiver, new IntentFilter("GPSLocationUpdates"));
                */
}

    private void showGPSDisabledAlertToUser() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("GPS is disabled in your device. Would you like to enable it?")
                .setCancelable(false)
                .setPositiveButton("Open Location Settings",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent callGPSSettingIntent = new Intent(
                                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(callGPSSettingIntent);
                            }
                        });
        alertDialogBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
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
        } else {
            //Toast.makeText(this,"hasPermission",Toast.LENGTH_SHORT).show();
            /*
            Intent callGPSSettingIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            int x=0;
            this.startActivityForResult(callGPSSettingIntent,x);
            Toast.makeText(this,"value = "+x,Toast.LENGTH_SHORT).show();
            */
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                Toast.makeText(this, "GPS is Enabled in your device", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, MapsActivity.class);
                if (destinationYN)
                    startActivityForResult(intent, MAP_DESTINATION_REQ);
                else
                    startActivityForResult(intent, MAP_SOURCE_REQ);
            } else {
                showGPSDisabledAlertToUser();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void mapD(View v) {
        destinationYN = true;
        mapSD(v);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void mapS(View v) {
        destinationYN = false;
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
                    if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                        Toast.makeText(this, "GPS is Enabled in your devide", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(this, MapsActivity.class);
                        startActivityForResult(intent, MAP_SOURCE_REQ);
                    } else {
                        showGPSDisabledAlertToUser();
                    }
                } else {
                    Toast.makeText(this, "Sorry! Location Access Permission needed!", Toast.LENGTH_SHORT).show();
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
                Bundle b = data.getExtras();
                source = (LatLng) b.get("marker_latlng");
                v1.setText("source " + source);
                //Toast.makeText(this,"source "+source,Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == MAP_DESTINATION_REQ) {
            if (resultCode == RESULT_OK) {
                Bundle b = data.getExtras();
                destination = (LatLng) b.get("marker_latlng");
                v2.setText("destination " + destination);
                //Toast.makeText(this,"destination "+destination,Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void calDis(View v) {
        if (source != null && destination != null) {
            Location s = new Location("S");
            Location d = new Location("D");
            s.setLatitude(source.latitude);
            s.setLongitude(source.longitude);
            d.setLongitude(destination.longitude);
            d.setLatitude(destination.latitude);
            float disf = actual_dis = s.distanceTo(d);
            int dis = (int) disf;
            v3.setText("Distance = " + dis / 1000.0 + " kms");
        } else {
            if (source == null)
                Toast.makeText(this, "Source not selected!", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(this, "Destination not selected!", Toast.LENGTH_SHORT).show();
        }
    }

    public void ringButton(View v) {
        ring();
    }

    public void ring() {
        //
        // Get instance of Vibrator from current Context
        v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        // Start without a delay
        // Vibrate for 100 milliseconds
        // Sleep for 1000 milliseconds
        long[] pattern = {0, 1000, 1000};

        // The '0' here means to repeat indefinitely
        // '0' is actually the index at which the pattern keeps repeating from (the start)
        // To repeat the pattern from any other point, you could increase the index, e.g. '1'
        //v.vibrate(pattern, 0);
        //
        try {
            Uri alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
            if (!mMediaPlayer.isPlaying()) {
                mMediaPlayer = new MediaPlayer();
                v.vibrate(pattern, 0);
            }
            mMediaPlayer.setDataSource(this, alert);
            final AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            if (audioManager.getStreamVolume(AudioManager.STREAM_RING) != 0) {
                mMediaPlayer.setAudioStreamType(AudioManager.STREAM_RING);
                mMediaPlayer.setLooping(true);
                mMediaPlayer.prepare();
                mMediaPlayer.start();
            }
        } catch (Exception e) {
        }
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Turn off Alarm?")
                .setCancelable(false)
                .setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                mMediaPlayer.stop();
                                v.cancel();
                            }
                        });
        alertDialogBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    public void setAlarmDistance(View v) {
        final Dialog dialog = new Dialog(this);
        LayoutInflater li = getLayoutInflater();
        View v1 = li.inflate(R.layout.distance_list, null, false);
        dialog.setContentView(v1);
        dialog.show();
        RadioGroup rg = (RadioGroup) v1.findViewById(R.id.radioGroup1);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radioButton500m:
                        alarm_dis = 500;
                        v4.setText("500 m " + (actual_dis - alarm_dis));
                        break;
                    case R.id.radioButton1km:
                        alarm_dis = 1000;
                        v4.setText("1 km " + (actual_dis - alarm_dis));
                        break;
                    case R.id.radioButton2km:
                        alarm_dis = 2000;
                        v4.setText("2 km " + (actual_dis - alarm_dis));
                        break;
                    case R.id.radioButton5km:
                        alarm_dis = 5000;
                        v4.setText("5 km " + (actual_dis - alarm_dis));
                        break;
                }
                dialog.cancel();
                if (actual_dis - alarm_dis <= 0)
                    ring();
            }
        });
    }

    public void startBLS(View v) {
        Intent intent=new Intent(this,BackgroundLocationService.class);
        global=intent;
        startService(intent);
        //
        //LocalBroadcastManager.getInstance(this).registerReceiver(
          //      mMessageReceiver, new IntentFilter("GPSLocationUpdates"));
        //
        /*
        Intent i=getIntent();
        Bundle b=i.getBundleExtra("Location");
        Location l= b.getParcelable("Location");
        currentLocation=new LatLng(l.getLatitude(),l.getLongitude());
        */
        //
        v5.setText("Current = " + currentLocation);
    }
    public void stopBLS(View v){
        stopService(global);
        Toast.makeText(this,"Stopped Service",Toast.LENGTH_SHORT).show();
    }
}
