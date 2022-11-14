package ca.tnoah.frc.scouting.ui.form;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;

import ca.tnoah.frc.scouting.R;
import ca.tnoah.frc.scouting.databinding.ActivityFormBinding;
import ca.tnoah.frc.scouting.models.dbo.Scout;
import ca.tnoah.frc.scouting.models.dbo.Team;
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
    private static final String INTENT_NEW_SCOUT = "intent_new_scout";

    // Data
    private Scout scout;
    private boolean newScout;

    // Data from Scout
    private TemplateData templateData;

    private ActivityFormBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityFormBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (!getIntentData()) {
            finish();
            return;
        }

        setSupportActionBar(binding.appBarForm.formToolbar);

        linearLayout = findViewById(R.id.form_linear_layout);
        createView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.form_menu, menu);

        Team team = db.teamsDAO().get(scout.teamKey);
        if (team == null) {
            finish();
            return false;
        }
        setTitle(team.nickname);

        MenuItem delete = menu.findItem(R.id.action_delete);
        delete.setOnMenuItemClickListener(item -> {
            delete();
            return true;
        });

        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private void delete() {
        if (!newScout) {
            scout.deleted = true;
            save();
        }

        Toast toast = Toast.makeText(this, R.string.successful_delete, Toast.LENGTH_SHORT);
        toast.show();

        finish();
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
        save();

        finish();
    }

    private void save() {
        db.scoutsDAO().insertOrUpdate(scout);
    }

    private boolean getIntentData() {
        Intent intent = getIntent();

        newScout = intent.getBooleanExtra(INTENT_NEW_SCOUT, true);

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
        intent.putExtra(INTENT_NEW_SCOUT, false);
        return intent;
    }

    public static Intent editFromNew(Context context, String teamKey, String eventKey, Template template) {
        Intent intent = new Intent(context, FormActivity.class);
        Scout scout = new Scout(teamKey, eventKey, template);
        intent.putExtra(INTENT_SCOUT, gson.toJson(scout));
        intent.putExtra(INTENT_NEW_SCOUT, true);

        return intent;
    }

    public static Intent viewOnly(Context context, Template template) {
        Intent intent = new Intent(context, FormActivity.class);
        intent.putExtra(INTENT_TEMPLATE, gson.toJson(template));
        return  intent;
    }
}