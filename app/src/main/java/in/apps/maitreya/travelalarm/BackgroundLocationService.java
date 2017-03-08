package in.apps.maitreya.travelalarm;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import static in.apps.maitreya.travelalarm.MainActivity.MY_PREFS_NAME;

/**
 * Created by Maitreya on 05-Feb-17.
 *
 */

public class BackgroundLocationService extends Service {
    private static final String TAG = "TESTBLS";
    final static String MY_ACTION = "MY_ACTION";
    private LocationManager mLocationManager = null;
    private int LOCATION_INTERVAL;
    private float LOCATION_DISTANCE;

    private class LocationListener implements android.location.LocationListener {
        Location mLastLocation;

        private LocationListener(String provider) {
            Log.e(TAG, "LocationListener " + provider);
            mLastLocation = new Location(provider);
        }

        @Override
        public void onLocationChanged(Location location) {
            Log.e(TAG, "onLocationChanged: " + location);
            sendMessageToActivity(location);
            mLastLocation.set(location);
        }

        @Override
        public void onProviderDisabled(String provider) {
            Log.e(TAG, "onProviderDisabled: " + provider);
        }

        @Override
        public void onProviderEnabled(String provider) {
            Log.e(TAG, "onProviderEnabled: " + provider);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.e(TAG, "onStatusChanged: " + provider);
        }
    }

    LocationListener[] mLocationListeners = new LocationListener[]{
            new LocationListener(LocationManager.GPS_PROVIDER),
            new LocationListener(LocationManager.NETWORK_PROVIDER)
    };

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "onStartCommand");
        //
        SharedPreferences prefs=getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        boolean notification_flag=prefs.getBoolean("notif",false);
        setIntervals(intent.getFloatExtra("act_dist",-1),notification_flag);
        //Toast.makeText(this,"INTERVALS = "+LOCATION_INTERVAL/1000+"s "+LOCATION_DISTANCE,Toast.LENGTH_SHORT).show();
        //
        manageLocation();
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        Log.e(TAG, "onCreate");
        Toast.makeText(this,"Service started",Toast.LENGTH_SHORT).show();
    }
    public void manageLocation(){
        initializeLocationManager();
        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    mLocationListeners[1]);
        } catch (java.lang.SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "network provider does not exist, " + ex.getMessage());
        }
        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    mLocationListeners[0]);
        } catch (java.lang.SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "gps provider does not exist " + ex.getMessage());
        }
    }
    @Override
    public void onDestroy() {
        Log.e(TAG, "onDestroy");
        super.onDestroy();
        if (mLocationManager != null) {
            for (int i = 0; i < mLocationListeners.length; i++) {
                try {
                    if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    mLocationManager.removeUpdates(mLocationListeners[i]);
                } catch (Exception ex) {
                    Log.i(TAG, "fail to remove location listners, ignore", ex);
                }
            }
        }
    }

    private void initializeLocationManager() {
        Log.e(TAG, "initializeLocationManager");
        if (mLocationManager == null) {
            mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }
    }
    private void sendMessageToActivity(Location l) {
        Intent intent = new Intent("GPSLocationUpdates");
        Bundle b = new Bundle();
        b.putParcelable("Location", l);
        intent.putExtra("Location", b);
        intent.setAction(MY_ACTION);
        //Toast.makeText(this,"Sent "+l.getLatitude(),Toast.LENGTH_SHORT).show();
        this.sendBroadcast(intent);
    }
    private void setIntervals(float actual_distance,boolean notification_flag){
        if(notification_flag){
            LOCATION_DISTANCE = 100f;
            LOCATION_INTERVAL = 1000;
        }
        else {
            if (actual_distance < 10000) {
                LOCATION_DISTANCE = 100f;
                LOCATION_INTERVAL = 10 * 1000;
            } else {
                int distance_factor = (int) actual_distance / 10000;
                LOCATION_DISTANCE = 1000 * distance_factor;
                LOCATION_INTERVAL = 30 * 1000 * distance_factor;
            }
        }
    }
}
