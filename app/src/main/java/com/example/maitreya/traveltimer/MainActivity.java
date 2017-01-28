package com.example.maitreya.traveltimer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void mapG(View v) {
        Intent intent=new Intent(this,MapsActivity.class);
        startActivity(intent);
    }
}
