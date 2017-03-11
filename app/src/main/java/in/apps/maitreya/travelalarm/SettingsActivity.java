package in.apps.maitreya.travelalarm;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import static in.apps.maitreya.travelalarm.MainActivity.MY_PREFS_NAME;

public class SettingsActivity extends AppCompatActivity {
    int minAlarmDistance,maxAlarmDistance;
    EditText et1,et2;
    Switch aSwitch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        et1= (EditText) findViewById(R.id.min_alarm_et);
        et2= (EditText) findViewById(R.id.max_alarm_et);
        aSwitch= (Switch) findViewById(R.id.settings_notification_switch);
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        aSwitch.setChecked(prefs.getBoolean("notif",false));
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
            editor.putInt("minAlarm",minAlarmDistance);
            editor.putInt("maxAlarm",maxAlarmDistance);
            editor.apply();
            setResult(RESULT_OK);
            finish();
        }
    }
}
