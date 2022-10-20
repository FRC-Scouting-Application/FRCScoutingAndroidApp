package ca.tnoah.frc.scouting.ui.form;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.UUID;

import ca.tnoah.frc.scouting.R;
import ca.tnoah.frc.scouting.models.dbo.Note;
import ca.tnoah.frc.scouting.models.template.TemplateData;
import ca.tnoah.frc.scouting.models.template.TemplateDataViews;
import ca.tnoah.frc.scouting.models.template.TemplateSerializer;
import ca.tnoah.frc.scouting.services.DatabaseService;
import ca.tnoah.frc.scouting.services.localdb.AppDatabase;

public class FormActivity extends AppCompatActivity {

    private static final String TAG = "==FormActivity==";

    private final AppDatabase db = DatabaseService.getInstance().getDB();

    private TemplateData templateData;
    private TemplateDataViews templateDataViews;

    public static final String TEMPLATE = "template";
    public static final String TEMPLATE_FILE = "template_file";

    public static final String TEAM_KEY = "team_key";
    public static final String EVENT_KEY = "event_key";

    private String teamKey;
    private String eventKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        LinearLayout linearLayout = findViewById(R.id.form_linear_layout);

        Intent intent = getIntent();
        String templateJSON = intent.getStringExtra(TEMPLATE);
        String templateFile = intent.getStringExtra(TEMPLATE_FILE);
        teamKey = intent.getStringExtra(TEAM_KEY);
        eventKey = intent.getStringExtra(EVENT_KEY);

        if (templateJSON != null)
            templateData = TemplateSerializer.deserializeFromJson(templateJSON);
        else if (templateFile != null)
            templateData = TemplateSerializer.deserializeFromJson(this, templateFile);
        else {
            TextView textView = new TextView(this);
            textView.setText(R.string.template_not_found);
            linearLayout.addView(textView);

            Button button = new Button(this);
            button.setText(R.string.go_back);
            button.setOnClickListener((l) -> finish());
            linearLayout.addView(button);

            return;
        }

        if (teamKey == null || eventKey == null) {
            TextView textView = new TextView(this);
            textView.setText(R.string.unable_to_save);
            linearLayout.addView(textView);
        }

        templateDataViews = new TemplateDataViews(this, templateData);
        templateDataViews.generate(linearLayout);

        Button submit = new Button(this);
        submit.setText(R.string.submit);
        submit.setOnClickListener(this::onSubmit);
        linearLayout.addView(submit);
    }

    private void onSubmit(View view) {
        templateData = templateDataViews.getTemplateData();
        Log.d(TAG, "Form Submitted");

        if (eventKey == null || teamKey == null) {
            saveToTestFile();
            return;
        }

        if (templateData.type.equals("notes")) {
            addNoteToDB();
        } else if (templateData.type.equals("pit") || templateData.type.equals("match")) {
            addScoutToDB();
        } else {
            TemplateSerializer.serializeToJson(templateData, this, "test.json");
        }

        finish();
    }

    private void addScoutToDB() {
        // TODO: Implement Scout to db
    }

    private void addNoteToDB() {
        Note note = new Note();
        note.id = UUID.randomUUID();
        note.eventKey = eventKey;
        note.teamKey = teamKey;
        note.scoutName = ((TemplateData.Field.Text) templateData.sections.get(0).fields.get(0)).value;
        note.text = getSerializedTemplateData();

        db.notesDAO().insertOrUpdate(note);
    }

    private void saveToTestFile() {
        TemplateSerializer.serializeToJson(templateData, this, "test.json");
    }

    private String getSerializedTemplateData() {
        return TemplateSerializer.serializeToJson(templateData);
    }

    public static Intent createIntent(Context context, String json, String teamKey, String eventKey) {
        Intent intent = new Intent(context, FormActivity.class);

        intent.putExtra(TEMPLATE, json);
        intent.putExtra(TEAM_KEY, teamKey);
        intent.putExtra(EVENT_KEY, eventKey);

        return intent;
    }

    public static Intent createIntentFile(Context context, String file, String teamKey, String eventKey) {
        Intent intent = new Intent(context, FormActivity.class);

        intent.putExtra(TEMPLATE_FILE, file);
        intent.putExtra(TEAM_KEY, teamKey);
        intent.putExtra(EVENT_KEY, eventKey);

        return intent;
    }
}