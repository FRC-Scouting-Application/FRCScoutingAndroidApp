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
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.UUID;

import ca.tnoah.frc.scouting.R;
import ca.tnoah.frc.scouting.models.dbo.Note;
import ca.tnoah.frc.scouting.models.dbo.Scout;
import ca.tnoah.frc.scouting.models.dbo.Template;
import ca.tnoah.frc.scouting.models.template.TemplateData;
import ca.tnoah.frc.scouting.models.template.TemplateDataViews;
import ca.tnoah.frc.scouting.models.template.TemplateSerializer;
import ca.tnoah.frc.scouting.services.DatabaseService;
import ca.tnoah.frc.scouting.services.localdb.AppDatabase;

public class FormActivity extends AppCompatActivity {

    private static final String TAG = "==FormActivity==";
    private final AppDatabase db = DatabaseService.getInstance().getDB();

    private TemplateDataViews templateDataViews;
    private LinearLayout linearLayout;

    private static final Gson gson = new Gson();

    // Intent Params
    private static final String INTENT_SCOUT = "intent_scout";
    private static final String INTENT_TEMPLATE = "intent_template";

    // Data
    private Scout scout;

    // Data from Scout
    private TemplateData templateData;
    private String eventKey;
    private String teamKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        if (!getIntentData()) {
            finish();
            return;
        }

        linearLayout = findViewById(R.id.form_linear_layout);

        createView();
    }

    private void createView() {
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

        scout.data = TemplateSerializer.serializeToJson(templateData);
        scout.scoutName = scout.getScoutName();
        db.scoutsDAO().insertOrUpdate(scout);

        finish();
    }

    private boolean getIntentData() {
        Intent intent = getIntent();

        // Get the Scout from the intent
        String json = intent.getStringExtra(INTENT_SCOUT);
        if (json == null) {
            String templateJson = intent.getStringExtra(INTENT_TEMPLATE);
            Template template = gson.fromJson(templateJson, Template.class);

            if (template == null || template.data == null)
                return fail("Scout data invalid!");

            templateData = TemplateSerializer.deserializeFromJson(template.data);
            if (templateData == null) return fail("Template Data invalid!");
            return true;
        }

        scout = gson.fromJson(json, Scout.class);

        // Get Event and Team Key
        eventKey = scout.eventKey;
        teamKey = scout.teamKey;

        // Check if data exists already
        if (scout.data != null) {
            templateData = TemplateSerializer.deserializeFromJson(scout.data);
        } else {
            Template template = db.templatesDAO().get(scout.templateId, scout.templateVersion);
            if (template == null) return fail("Template invalid!");

            templateData = TemplateSerializer.deserializeFromJson(template.data);
        }

        if (templateData == null) return fail("Template Data invalid!");

        return true;
    }

    private boolean fail(String msg) {
        Toast toast = Toast.makeText(this, msg, Toast.LENGTH_LONG);
        toast.show();
        Log.d(TAG, "FAIL: " + msg);

        return false;
    }

    public static Intent editFromExisting(Context context, Scout scout) {
        Intent intent = new Intent(context, FormActivity.class);
        intent.putExtra(INTENT_SCOUT, gson.toJson(scout));
        return intent;
    }

    public static Intent editFromNew(Context context, String teamKey, String eventKey, Template template) {
        Intent intent = new Intent(context, FormActivity.class);
        Scout scout = new Scout(teamKey, eventKey, template);
        intent.putExtra(INTENT_SCOUT, gson.toJson(scout));

        return intent;
    }

    public static Intent viewOnly(Context context, Template template) {
        Intent intent = new Intent(context, FormActivity.class);
        intent.putExtra(INTENT_TEMPLATE, gson.toJson(template));
        return  intent;
    }
}