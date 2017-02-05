package com.example.maitreya.traveltimer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Maitreya on 05-Feb-17.
 */

public class MyIntentRec extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.v("IntentRec", "got it");
        Bundle b = intent.getBundleExtra("Location");
        Location lastKnownLoc = b.getParcelable("Location");
        Toast.makeText(context,"BR working "+lastKnownLoc.getLatitude(),Toast.LENGTH_SHORT).show();
        /*
        Intent i=new Intent(context,MainActivity.class);
        Bundle b1=new Bundle();
        b1.putParcelable("Location",lastKnownLoc);
        i.putExtra("Location",b1);
        context.startActivity(i);
        */
    }
}
