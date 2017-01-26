package com.example.maitreya.traveltimer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity{
    Button sign_in_home;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //View
        sign_in_home= (Button) findViewById(R.id.sign_in_home);
    }
    public void sign_In(View v) {
        Intent intent=new Intent(this,SignInActivity.class);
        startActivity(intent);
    }
    public void mapG(View v) {
        Intent intent=new Intent(this,MapsActivity.class);
        startActivity(intent);
    }
}
