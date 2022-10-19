package ca.tnoah.frc.scouting.ui.form;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import ca.tnoah.customviews.Counter;
import ca.tnoah.frc.scouting.R;
import ca.tnoah.frc.scouting.models.template.TemplateData;
import ca.tnoah.frc.scouting.models.template.TemplateSerializer;

public class FormActivity extends AppCompatActivity {

    LinearLayout linearLayout;
    LayoutInflater inflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        inflater = LayoutInflater.from(this);
        linearLayout = findViewById(R.id.form_linear_layout);

        TemplateData templateData = TemplateSerializer.getTestPit(this);
        createForm(templateData);
    }

    private void createForm(TemplateData templateData) {
        for (TemplateData.Section section : templateData.sections)
            createSection(section);
    }

    @SuppressLint("MissingInflatedId")
    private void createSection(TemplateData.Section section) {
        View child = inflater.inflate(R.layout.form_section, null);
        linearLayout.addView(child);

        ((TextView) child.findViewById(R.id.form_section_header)).setText(section.header);

        for (TemplateData.Field field : section.fields)
            createField(child.findViewById(R.id.form_section_linear_Layout), field);
    }

    private void createField(LinearLayout layout, TemplateData.Field field) {
        if (field instanceof TemplateData.Field.Text)
            createTextField(layout, (TemplateData.Field.Text) field);
        if (field instanceof TemplateData.Field.Radio)
            createRadioGroup(layout, (TemplateData.Field.Radio) field);
        if (field instanceof TemplateData.Field.Checklist)
            createCheckList(layout, (TemplateData.Field.Checklist) field);
        if (field instanceof TemplateData.Field.Counter)
            createCounter(layout, (TemplateData.Field.Counter) field);
    }

    private void createTextField(LinearLayout parent, TemplateData.Field.Text textField) {
        View child = inflater.inflate(R.layout.form_text_field, null);
        parent.addView(child);

        ((TextView) child.findViewById(R.id.formTextFieldLabel)).setText(textField.label);
        ((TextView) child.findViewById(R.id.formTextFieldValue)).setText(textField.value);
    }

    private void createRadioGroup(LinearLayout parent, TemplateData.Field.Radio radioList) {
        View child = inflater.inflate(R.layout.form_radio_list, null);
        parent.addView(child);

        ((TextView) child.findViewById(R.id.formRadioListLabel)).setText(radioList.label);
        RadioGroup radioGroup = child.findViewById(R.id.formRadioListRadioGroup);

        for (TemplateData.Item item : radioList.items) {
            RadioButton radioButton = new RadioButton(this);
            radioButton.setText(item.label);
            radioGroup.addView(radioButton);
        }

        if (radioList.other) {
            RadioButton radioButton = new RadioButton(this);
            radioButton.setText(R.string.other);
            radioGroup.addView(radioButton);
        }
    }

    private void createCheckList(LinearLayout parent, TemplateData.Field.Checklist checkList) {
        View child = inflater.inflate(R.layout.form_check_list, null);
        parent.addView(child);

        ((TextView) child.findViewById(R.id.formCheckListLabel)).setText(checkList.label);
        LinearLayout checkGroup = findViewById(R.id.formCheckListCheckGroup);

        for (TemplateData.Item item : checkList.items) {
            CheckBox checkBox = new CheckBox(this);
            checkBox.setText(item.label);
            checkGroup.addView(checkBox);
        }
    }

    private void createCounter(LinearLayout parent, TemplateData.Field.Counter counter) {
        Counter counterView = new Counter(this);
        counterView.setValue(counter.value);
        counterView.setLabel(counter.label);

        parent.addView(counterView);
    }
}