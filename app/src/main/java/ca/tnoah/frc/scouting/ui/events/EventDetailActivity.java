package ca.tnoah.frc.scouting.ui.events;

import androidx.annotation.IdRes;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import ca.tnoah.frc.scouting.R;
import ca.tnoah.frc.scouting.models.dbo.Event;
import ca.tnoah.frc.scouting.services.DatabaseService;
import ca.tnoah.frc.scouting.services.localdb.AppDatabase;

public class EventDetailActivity extends AppCompatActivity {
    private static final String TAG = "==EventDetailActivity==";

    private final AppDatabase db = DatabaseService.getInstance().getDB();
    public static final String EVENT_KEY = "event_key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);

        Intent intent = getIntent();
        String eventKey = intent.getStringExtra(EVENT_KEY);

        Event event = db.eventsDAO().get(eventKey);
        if (event != null) {

            setText(R.id.event_name, event.getName());
            setText(R.id.event_type, event.eventType + " " + getString(R.string.event_type));
            setText(R.id.event_date, event.getDateString());
            setText(R.id.event_location, event.getLongLocationString());

            TextView week = findViewById(R.id.event_week);
            if (event.week >= 0) {
                String weekStr = getString(R.string.week) + " " + event.week;
                week.setText(weekStr);
            } else {
                week.setVisibility(View.INVISIBLE);
            }
        }
    }

    private void setText(@IdRes int id, String text) {
        ((TextView) findViewById(id)).setText(text);
    }
}