package in.apps.maitreya.travelalarm;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.View;

public class FeedbackActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        //
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_feedback);
        toolbar.setNavigationIcon(R.drawable.ic_action_arrow_back);
        setSupportActionBar(toolbar);
        //
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //
    }
    public void shareEmail(View V)
    {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{getString(R.string.feedback_email_id)});
        intent.putExtra(Intent.EXTRA_SUBJECT, R.string.feedback_title);
        //intent.putExtra(Intent.EXTRA_TEXT, "");
        //startActivity(Intent.createChooser(intent, "Send Email"));
        startActivity(intent);
    }
}
