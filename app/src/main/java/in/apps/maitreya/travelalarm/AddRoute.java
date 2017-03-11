package in.apps.maitreya.travelalarm;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class AddRoute extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 0;
    static final int MAP_SOURCE_REQ_ADD = 0;  // The request code for source
    static final int MAP_DESTINATION_REQ_ADD = 1;  // The request code for source
    TextView v1, v2;
    LatLng source,destination;
    LocationManager locationManagerAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_route);
        v1= (TextView) findViewById(R.id.source_tv_add);
        v2= (TextView) findViewById(R.id.destination_tv_add);
        locationManagerAdd = (LocationManager) getSystemService(LOCATION_SERVICE);

    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void mapAddD(View v){
        Functions.mapSD(v,this,MAP_DESTINATION_REQ_ADD,MY_PERMISSIONS_REQUEST_LOCATION,locationManagerAdd);
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void mapAddS(View v){
        Functions.mapSD(v,this,MAP_SOURCE_REQ_ADD,MY_PERMISSIONS_REQUEST_LOCATION,locationManagerAdd);
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
        setResult(RESULT_OK);
        finish();
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
