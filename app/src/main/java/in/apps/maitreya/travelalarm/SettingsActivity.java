package in.apps.maitreya.travelalarm;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import static in.apps.maitreya.travelalarm.MainActivity.MY_PREFS_NAME;

public class SettingsActivity extends AppCompatActivity {
    int minAlarmDistance,maxAlarmDistance;
    EditText et1,et2;
    SwitchCompat aSwitch,lockSwitch, useMilesSwitch;
    TextView kmMiles1, kmMiles2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        et1 = findViewById(R.id.min_alarm_et);
        et2 = findViewById(R.id.max_alarm_et);
        aSwitch =  findViewById(R.id.settings_notification_switch);
        lockSwitch =  findViewById(R.id.settings_lock_switch);
        useMilesSwitch = findViewById(R.id.units_switch);
        kmMiles1 = findViewById(R.id.km_miles_1);
        kmMiles2 = findViewById(R.id.km_miles_2);

        useMilesSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    kmMiles1.setText(getString(R.string.miles));
                    kmMiles2.setText(getString(R.string.miles));
                }
                else{
                    kmMiles1.setText(getString(R.string.kilometer));
                    kmMiles2.setText(getString(R.string.kilometer));
                }
            }
        });

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        aSwitch.setChecked(prefs.getBoolean("notif",false));
        lockSwitch.setChecked(prefs.getBoolean("lock",false));
        useMilesSwitch.setChecked(prefs.getBoolean("useMiles", false));
        et1.setText(""+prefs.getInt("minAlarm",-1)+"");
        et2.setText(""+prefs.getInt("maxAlarm",-1)+"");
        //
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_settings);
        toolbar.setNavigationIcon(R.drawable.ic_action_arrow_back);
        setSupportActionBar(toolbar);
        //
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exitSettings();
            }
        });
        //
        //
    }
    @Override
    public void onBackPressed(){
        exitSettings();
    }
    public void exitSettings(){
        String e1=et1.getText().toString();
        if(e1.equals(""))
            e1="1";
        String e2=et2.getText().toString();
        if(e2.equals(""))
            e2="100";
        minAlarmDistance=Integer.parseInt(e1);
        maxAlarmDistance=Integer.parseInt(e2);
        if(minAlarmDistance>maxAlarmDistance)
        {
            Toast.makeText(this,"Minimum value cannot be greater than maximum value!",Toast.LENGTH_SHORT).show();
        }
        else {
            SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
            editor.putBoolean("notif",aSwitch.isChecked());
            editor.putBoolean("lock",lockSwitch.isChecked());
            editor.putBoolean("useMiles", useMilesSwitch.isChecked());
            editor.putInt("minAlarm",minAlarmDistance);
            editor.putInt("maxAlarm",maxAlarmDistance);
            editor.apply();
            setResult(RESULT_OK);
            finish();
        }
    }
    public void openFeedback(View v){
        Intent i=new Intent(this,FeedbackActivity.class);
        startActivity(i);
    }
}
