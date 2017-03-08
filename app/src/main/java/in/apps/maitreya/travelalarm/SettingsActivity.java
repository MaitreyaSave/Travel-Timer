package in.apps.maitreya.travelalarm;

import android.app.ActionBar;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import static in.apps.maitreya.travelalarm.MainActivity.MY_PREFS_NAME;

public class SettingsActivity extends AppCompatActivity {
    private int minAlarmDistance,maxAlarmDistance;
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
        /*android.support.v7.app.ActionBar a=getSupportActionBar();
        if(a!=null) {
            a.setDisplayHomeAsUpEnabled(true);
            a.setHomeButtonEnabled(true);
        }*/
        //
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.settings_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // action with ID action_settings was selected
            case R.id.action_back:
                exitSettings();
                break;
            default:
                break;
        }
        return true;
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
