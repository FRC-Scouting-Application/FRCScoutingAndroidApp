package ca.tnoah.frc.scouting.ui.form;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import ca.tnoah.frc.scouting.R;
import ca.tnoah.frc.scouting.models.template.TemplateData;
import ca.tnoah.frc.scouting.models.template.TemplateDataViews;
import ca.tnoah.frc.scouting.models.template.TemplateDataViewsOld;
import ca.tnoah.frc.scouting.models.template.TemplateSerializer;

public class FormActivity extends AppCompatActivity {

    private static final String TAG = "==FormActivity==";

    private TemplateData templateData;
    private TemplateDataViews templateDataViews;

    public static final String TEMPLATE = "template";
    public static final String TEMPLATE_FILE = "template_file";

    public static final String TEAM_KEY = "team_key";
    public static final String EVENT_KEY = "event_key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        LinearLayout linearLayout = findViewById(R.id.form_linear_layout);

        Intent intent = getIntent();
        String templateJSON = intent.getStringExtra(TEMPLATE);
        String templateFile = intent.getStringExtra(TEMPLATE_FILE);
        String teamKey = intent.getStringExtra(TEAM_KEY);
        String eventKey = intent.getStringExtra(EVENT_KEY);

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

    public void onSubmit(View view) {
        templateData = templateDataViews.getTemplateData();
        Log.d(TAG, "Form Submitted");
    }
}