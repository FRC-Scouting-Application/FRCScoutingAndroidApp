package ca.tnoah.frc.scouting.models.template;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ca.tnoah.frc.scouting.R;

public class TemplateDataViewsOld extends TemplateData {

    private final Context context;
    private final LayoutInflater inflater;
    private LinearLayout linearLayout;

    public List<Section> sections;

    public TemplateDataViewsOld(Context context, TemplateData data) {
        this.name = data.name;
        this.type = data.type;
        this.year = data.year;
        this.sections = new ArrayList<>();

        for (TemplateData.Section section : data.sections) {
            this.sections.add(new Section(section));
        }

        this.context = context;
        this.inflater = LayoutInflater.from(context);
        generate();
    }

    public LinearLayout getLinearLayout() {
        return this.linearLayout;
    }

    public void generate() {
        linearLayout = new LinearLayout(context);

        for (Section section : sections) {
            linearLayout.addView(section.generate(inflater, context));
        }
    }

    public void update() {
        for (Section section : sections) {
            section.update();
        }
    }

    public static class Section extends TemplateData.Section implements IUpdate {
        protected View view;
        protected List<IField> fields;

        public Section(TemplateData.Section section) {
            this.fields = new ArrayList<>();

            for (TemplateData.Field field : section.fields) {
                IField addField;

                if (field instanceof TemplateData.Field.Text) addField = (Text) field;
            }
        }

        @SuppressLint("InflateParams")
        public View generate(LayoutInflater inflater, Context context) {
            view = inflater.inflate(R.layout.form_section, null);

            for (IField field : fields) {
                LinearLayout layout = view.findViewById(R.id.form_section_linear_Layout);
                layout.addView(field.generate(inflater, context));
            }

            return view;
        }

        @Override
        public void update() {
            for (IField field : fields) {
                field.update();
            }
        }
    }

    public static class Text extends TemplateData.Field.Text implements IField {
        protected View view;
        private EditText _value;

        @SuppressLint("InflateParams")
        @Override
        public View generate(LayoutInflater inflater, Context context) {
            view = inflater.inflate(R.layout.form_text_field, null);

            ((TextView) view.findViewById(R.id.formTextFieldLabel)).setText(this.label);

            _value = view.findViewById(R.id.formTextFieldValue);
            _value.setText(this.value);

            return view;
        }

        @Override
        public void update() {
            this.value = _value.getText().toString();
        }
    }

    public static class Radio extends TemplateData.Field.Radio implements IField {
        protected View view;
        private RadioGroup radioGroup;

        @SuppressLint("InflateParams")
        @Override
        public View generate(LayoutInflater inflater, Context context) {
            view = inflater.inflate(R.layout.form_radio_list, null);

            ((TextView) view.findViewById(R.id.formRadioListLabel)).setText(this.label);
            radioGroup = view.findViewById(R.id.formRadioListRadioGroup);

            for (TemplateData.RadioItem item : items) {
                RadioButton radioButton = new RadioButton(context);
                radioButton.setText(item.label);
                radioGroup.addView(radioButton);
            }

            if (other) {
                RadioButton radioButton = new RadioButton(context);
                radioButton.setText(R.string.other);
                radioGroup.addView(radioButton);
            }

            return view;
        }

        @Override
        public void update() {
            int checked = radioGroup.getCheckedRadioButtonId();

            if (checked == -1)
                selected = null;
            else
                selected = checked;
        }
    }

    public interface IField extends IUpdate {
        View generate(LayoutInflater inflater, Context context);
    }

    public interface IUpdate {
        void update();
    }
}