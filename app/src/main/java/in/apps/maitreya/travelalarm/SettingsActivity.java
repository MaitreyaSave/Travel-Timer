package in.apps.maitreya.travelalarm;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity {
    private int minAlarmDistance,maxAlarmDistance;
    EditText et1,et2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        et1= (EditText) findViewById(R.id.min_alarm_et);
        et2= (EditText) findViewById(R.id.max_alarm_et);
        Bundle b=getIntent().getExtras();
        minAlarmDistance=b.getInt("current_min_alarm");
        maxAlarmDistance=b.getInt("current_max_alarm");
        et1.setText(""+minAlarmDistance+"");
        et2.setText(""+maxAlarmDistance+"");
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
            Intent intent = new Intent();
            intent.putExtra("min_alarm", minAlarmDistance);
            intent.putExtra("max_alarm", maxAlarmDistance);
            setResult(RESULT_OK, intent);
            finish();
        }
    }
}
