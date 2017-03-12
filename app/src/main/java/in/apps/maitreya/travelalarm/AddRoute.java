package in.apps.maitreya.travelalarm;

import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static in.apps.maitreya.travelalarm.MainActivity.MY_PREFS_NAME;


public class AddRoute extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 0;
    static final int MAP_SOURCE_REQ_ADD = 0;  // The request code for source
    static final int MAP_DESTINATION_REQ_ADD = 1;  // The request code for source
    TextView v1, v2;
    EditText e1;
    LatLng source,destination;
    LocationManager locationManagerAdd;
    Route route;
    List<Route> routeList;
    Gson gson;
    SharedPreferences appSharedPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_route);
        v1= (TextView) findViewById(R.id.source_tv_add);
        v2= (TextView) findViewById(R.id.destination_tv_add);
        e1= (EditText) findViewById(R.id.title_route_et);
        locationManagerAdd = (LocationManager) getSystemService(LOCATION_SERVICE);

        //
        appSharedPrefs = getSharedPreferences(MY_PREFS_NAME,MODE_PRIVATE);
        gson = new Gson();
        String json = appSharedPrefs.getString("Route","");
        Type type = new TypeToken<List<Route>>(){}.getType();
        routeList =gson.fromJson(json, type);
        //
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void mapAddD(View v){
        Functions.mapSD(this,MAP_DESTINATION_REQ_ADD,MY_PERMISSIONS_REQUEST_LOCATION,locationManagerAdd);
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void mapAddS(View v){
        Functions.mapSD(this,MAP_SOURCE_REQ_ADD,MY_PERMISSIONS_REQUEST_LOCATION,locationManagerAdd);
    }
    //


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Geocoder geocoder;
        List<Address> addressList = null;
        geocoder = new Geocoder(this, Locale.getDefault());
        if (requestCode == MAP_SOURCE_REQ_ADD) {
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
            }
        }
        else if (requestCode == MAP_DESTINATION_REQ_ADD) {
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

            }
        }
    }

    //
    public void done(View v){
        String title=e1.getText().toString();
        if(source==null||destination==null||title.equals("")){
            Toast.makeText(this,"All fields need to be filled!",Toast.LENGTH_SHORT).show();
        }
        else {
            route = new Route(title);
            route.setSource(source);
            route.setDestination(destination);
            //
            Geocoder geocoder;
            List<Address> addressList = null;
            geocoder = new Geocoder(this, Locale.getDefault());
            String s = "";
            LatLng l = route.getSource();
            if (l != null)
                try {
                    addressList = geocoder.getFromLocation(l.latitude, l.longitude, 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            if (addressList != null)
                s = Functions.getAddressAsString(addressList.get(0));
            route.setSourceString(s);
            l = route.getDestination();
            if (l != null)
                try {
                    addressList = geocoder.getFromLocation(l.latitude, l.longitude, 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            if (addressList != null)
                s = Functions.getAddressAsString(addressList.get(0));
            //
            route.setDestinationString(s);
            //
            if(routeList==null)
                routeList=new ArrayList<>();
            routeList.add(route);
            //
            SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
            Gson gson = new Gson();
            String json = gson.toJson(routeList);
            prefsEditor.putString("Route", json);
            prefsEditor.apply();
            //
            setResult(RESULT_OK);
            finish();
        }
    }
    public void closeView(View v){
        close();
    }
    public void close(){
        setResult(RESULT_CANCELED);
        finish();
    }

    @Override
    public void onBackPressed() {
        close();
    }

}
