package in.apps.maitreya.travelalarm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener {
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 0;
    static final int MAP_SOURCE_REQ = 0;  // The request code for source
    static final int MAP_DESTINATION_REQ = 1;  // The request code for source
    static final int SETTINGS = 2; // The request code for settings
    static final int FAVORITES = 3; // The request code for FAVORITES
    boolean serviceStarted;
    NotificationManager notificationManager;
    int position = -1;
    //
    boolean exit;
    //
    TextView v1, v2, v3, v4;
    SeekBar seekBarAlarmDistance;
    Intent global;
    public static final String MY_PREFS_NAME = "MySharedPrefsFile";
    //
    Vibrator v;
    float alarm_dis, actual_dis;
    int minAlarmDistance,maxAlarmDistance;
    boolean notification_flag;
    static MediaPlayer mMediaPlayer;
    LatLng source, destination, currentLocation;
    LocationManager locationManager;
    MyReceiver myReceiver;
    List<Route> routeList;
    Gson gson;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        v1 = (TextView) findViewById(R.id.source_tv);
        v2 = (TextView) findViewById(R.id.destination_tv);
        v3 = (TextView) findViewById(R.id.calDis_tv);
        v4 = (TextView) findViewById(R.id.AlarmDis_tv);
        seekBarAlarmDistance= (SeekBar) findViewById(R.id.alarm_seek_bar);
        seekBarAlarmDistance.setOnSeekBarChangeListener(this);
        mMediaPlayer = new MediaPlayer();
        if(!preferenceFileExist(MY_PREFS_NAME)) {
            //default values
            minAlarmDistance = 1;
            maxAlarmDistance = 20;
            notification_flag=false;
            //
            SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
            editor.putBoolean("notif", notification_flag);
            editor.putInt("minAlarm", minAlarmDistance);
            editor.putInt("maxAlarm", maxAlarmDistance);
            editor.apply();
        }
        sp=getSharedPreferences(MY_PREFS_NAME,MODE_PRIVATE);
        maxAlarmDistance=sp.getInt("maxAlarm",-1);
        minAlarmDistance=sp.getInt("minAlarm",-1);
        notification_flag=sp.getBoolean("notif",false);
        //
        getListfromSharedPreferences();
        //

        seekBarAlarmDistance.setMax(maxAlarmDistance - minAlarmDistance);
        //
}

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void mapD(View v) {
        Functions.mapSD(v,this,MAP_DESTINATION_REQ,MY_PERMISSIONS_REQUEST_LOCATION,locationManager);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void mapS(View v) {
        Functions.mapSD(v,this,MAP_SOURCE_REQ,MY_PERMISSIONS_REQUEST_LOCATION,locationManager);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull  String permissions[],@NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                        Intent intent = new Intent(this, MapsActivity.class);
                        startActivityForResult(intent, MAP_SOURCE_REQ);
                    } else {
                        Functions.showGPSDisabledAlertToUser(this);
                    }
                } else {
                    Toast.makeText(this, "Sorry! Location Access Permission needed!", Toast.LENGTH_SHORT).show();
                    //nothing can be done
                }
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to

        if (requestCode == SETTINGS){
            if (resultCode == RESULT_OK){
                SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                minAlarmDistance=prefs.getInt("minAlarm",-1);
                maxAlarmDistance=prefs.getInt("maxAlarm",-1);
                notification_flag=prefs.getBoolean("notif",false);
                seekBarAlarmDistance.setMax(maxAlarmDistance-minAlarmDistance);
            }
        }
        else if(requestCode == FAVORITES){
            if(resultCode == RESULT_OK){
                position=data.getIntExtra("pos",-1);
                getListfromSharedPreferences();
                setValues();
            }
        }
        else {
            Geocoder geocoder;
            List<Address> addressList = null;
            geocoder = new Geocoder(this, Locale.getDefault());
            if (requestCode == MAP_SOURCE_REQ) {
                // Make sure the request was successful
                if (resultCode == RESULT_OK) {
                    Bundle b = data.getExtras();
                    source = (LatLng) b.get("marker_latlng");
                    //
                    if (source != null)
                        try {
                            addressList = geocoder.getFromLocation(source.latitude, source.longitude, 1);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    //
                    if (addressList != null)
                        v1.setText(Functions.getAddressAsString(addressList.get(0)));
                    //
                    if (destination != null)
                        calculateDistance(source, destination);
                }
            } else if (requestCode == MAP_DESTINATION_REQ) {
                if (resultCode == RESULT_OK) {
                    Bundle b = data.getExtras();
                    destination = (LatLng) b.get("marker_latlng");
                    //
                    if (destination != null)
                        try {
                            addressList = geocoder.getFromLocation(destination.latitude, destination.longitude, 1);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    //
                    if (addressList != null)
                        v2.setText(Functions.getAddressAsString(addressList.get(0)));
                    //
                    if (source != null)
                        calculateDistance(source, destination);
                }
            }
        }
    }

    public void calculateDistance(LatLng s,LatLng d) {
        if (source != null && destination != null) {
            Location s1 = new Location("S");
            Location d1 = new Location("D");
            s1.setLatitude(s.latitude);
            s1.setLongitude(s.longitude);
            d1.setLongitude(d.longitude);
            d1.setLatitude(d.latitude);
            actual_dis = s1.distanceTo(d1);
            int dis = (int) actual_dis;
            actual_dis/=1000;
            String distance_string=dis/1000.0+" km";
            v3.setText(distance_string);
        } else {
            if (source == null)
                Toast.makeText(this, "Source not selected!", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(this, "Destination not selected!", Toast.LENGTH_SHORT).show();
        }
    }

    public void ring() {
        //
        // Get instance of Vibrator from current Context
        sendNotification("Your are almost there","Click to turn off alarm!",true,true);
        //
        v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        // Start without a delay
        // Vibrate for 1000 milliseconds
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
            if (audioManager.getStreamVolume(AudioManager.STREAM_ALARM) != 0) {
                mMediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
                mMediaPlayer.setLooping(true);
                mMediaPlayer.prepare();
                mMediaPlayer.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
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
    public void startBLS(View v) {
        //
        if(alarm_dis>=actual_dis){
            Toast.makeText(this,"Alarm Distance cannot be larger than actual distance!",Toast.LENGTH_SHORT).show();
        }
        else
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            if(source!=null) {
                if(destination!=null) {
                    if(alarm_dis>0) {
                        //Pending distance notification
                        if(notification_flag) {
                            int dis= (int) (actual_dis*1000);
                            sendNotification("Pending distance", dis/1000.0 + " km", true, true);
                        }
                        //
                        myReceiver = new MyReceiver();
                        IntentFilter intentFilter = new IntentFilter();
                        intentFilter.addAction(BackgroundLocationService.MY_ACTION);
                        registerReceiver(myReceiver, intentFilter);
                        Intent intent = new Intent(this, BackgroundLocationService.class);
                        //
                        intent.putExtra("act_dist", actual_dis);
                        intent.putExtra("alarm_dist", alarm_dis);
                        intent.putExtra("notif",notification_flag);
                        //
                        global = intent;
                        serviceStarted = true;
                        startService(intent);
                    }
                    else
                        Toast.makeText(this,"Alarm Distance is not set",Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(this,"Destination not selected!",Toast.LENGTH_SHORT).show();
            }
            else
                Toast.makeText(this,"Source not selected!",Toast.LENGTH_SHORT).show();
        }
        else
            Functions.showGPSDisabledAlertToUser(this);
    }
    public void stopBLS(View v){
        stopBackgroundLocationService();
        if(notificationManager!=null)
        notificationManager.cancelAll();

    }
    public void stopBackgroundLocationService(){
        if(serviceStarted) {
            unregisterReceiver(myReceiver);
            stopService(global);
            Toast.makeText(this, "Service Stopped", Toast.LENGTH_SHORT).show();
            serviceStarted=false;
        }
        else
            Toast.makeText(this,"Service has not started yet!",Toast.LENGTH_SHORT).show();
    }
    private class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context arg0, Intent arg1) {
            Bundle b=arg1.getBundleExtra("Location");
            Location l= b.getParcelable("Location");
            if(l!=null)
            currentLocation=new LatLng(l.getLatitude(),l.getLongitude());
            //Toast.makeText(ctx,"Difference: " + (actual_dis-alarm_dis)+" m",Toast.LENGTH_SHORT).show();
            calculateDistance(currentLocation,destination);
            //Pending distance notification
            if(notification_flag) {
                int dis= (int) (actual_dis*1000);
                sendNotification("Pending distance", dis/1000.0 + " km", true, true);
            }
            //
            if (actual_dis <= alarm_dis) {
                ring();
                stopBackgroundLocationService();
            }
        }

    }

    //notification function
    public void sendNotification(String title,String content, boolean autocancel,boolean ongoing){
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_stat_ic_travel_alarm)
                        .setContentTitle(title)
                        .setAutoCancel(autocancel)
                        .setOngoing(ongoing)
                        .setContentText(content);

        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);

        // Add as notification
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, builder.build());

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // action with ID action_settings was selected
            case R.id.action_settings:
                Intent i=new Intent(this,SettingsActivity.class);
                startActivityForResult(i,SETTINGS);
                break;
            case R.id.action_favorites:
                Intent i1=new Intent(this,FavoritesActivity.class);
                startActivityForResult(i1,FAVORITES);
                break;
            default:
                break;
        }
        return true;
    }
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        alarm_dis=progress+minAlarmDistance;
        v4.setText(""+alarm_dis+" km");
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
    public boolean preferenceFileExist(String fileName) {
        File f = new File(getApplicationContext().getApplicationInfo().dataDir + "/shared_prefs/"
                + fileName + ".xml");
        return f.exists();
    }

    @Override
    public void onBackPressed() {
        if(exit)
            super.onBackPressed();
        else {
            exit = true;
            Toast.makeText(this, "Press back again to exit!", Toast.LENGTH_SHORT).show();
        }
    }
    public void setValues(){
        if (position!=-1){
            Route r=routeList.get(position);
            v1.setText(r.getSourceString());
            source=r.getSource();
            v2.setText(r.getDestinationString());
            destination=r.getDestination();
            calculateDistance(source,destination);
        }
    }
    public void getListfromSharedPreferences(){
        //
        gson = new Gson();
        String json = sp.getString("Route","");
        Type type = new TypeToken<List<Route>>(){}.getType();
        routeList =gson.fromJson(json, type);
        //
    }

}
