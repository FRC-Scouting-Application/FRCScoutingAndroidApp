package ca.tnoah.frc.scouting.models.template;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import ca.tnoah.customviews.Counter;
import ca.tnoah.frc.scouting.R;

public class TemplateDataViews {
    private final Context context;
    private final TemplateData templateData;

    private List<SectionView> sectionViews;

    public TemplateDataViews(Context context, TemplateData templateData) {
        this.context = context;
        this.templateData = templateData;
    }

    public void generate(LinearLayout layout) {
        layout.removeAllViews();
        this.sectionViews = new ArrayList<>();
        for (TemplateData.Section section: templateData.sections) {
            SectionView sectionView = new SectionView(section);
            sectionViews.add(sectionView);
            layout.addView(sectionView.generate(context));
        }
    }

    public void update() {
        for (SectionView sectionView : sectionViews)
            sectionView.update();
    }

    public TemplateData getTemplateData() {
        update();
        return templateData;
    }

    public static class SectionView extends TemplateView<TemplateData.Section> {
        public List<TemplateFieldView> fieldViews;

        public SectionView(TemplateData.Section section) {
            super(section);

            fieldViews = new ArrayList<>();
            for (TemplateData.Field field : data.fields) {

                if (field instanceof TemplateData.Field.Text)
                    fieldViews.add(new TemplateTextView((TemplateData.Field.Text) field));

                if (field instanceof TemplateData.Field.Radio)
                    fieldViews.add(new TemplateRadioView((TemplateData.Field.Radio) field));

                if (field instanceof TemplateData.Field.Checklist)
                    fieldViews.add(new TemplateChecklist((TemplateData.Field.Checklist) field));

                if (field instanceof TemplateData.Field.Checkbox)
                    fieldViews.add(new TemplateCheckbox((TemplateData.Field.Checkbox) field));

                if (field instanceof TemplateData.Field.Counter)
                    fieldViews.add(new TemplateCounter((TemplateData.Field.Counter) field));
            }
        }

        @Override
        public View generate(Context context) {
             inflate(context, R.layout.form_section);

             ((TextView) view.findViewById(R.id.form_section_header)).setText(data.header);
             LinearLayout layout = view.findViewById(R.id.form_section_linear_Layout);
             layout.removeAllViews();

             for (TemplateFieldView fieldView : fieldViews) {
                 layout.addView(fieldView.generate(context));
             }

             return view;
        }

        @Override
        public void update() {
            for (TemplateFieldView fieldView : fieldViews)
                fieldView.update();
        }
    }

    public static class TemplateTextView extends TemplateFieldView {
        private EditText value;

        public TemplateTextView(TemplateData.Field.Text data) {
            super(data);
        }

        @Override
        public View generate(Context context) {
            inflate(context, R.layout.form_text_field);
            setLabel(R.id.formTextFieldLabel);

            value = view.findViewById(R.id.formTextFieldValue);
            value.setText(getData().value);

            return view;
        }

        @Override
        public void update() {
            getData().value = this.value.getText().toString();
        }

        @Override
        public TemplateData.Field.Text getData() {
            return (TemplateData.Field.Text) super.getData();
        }
    }

    public static class TemplateRadioView extends TemplateFieldView {
        private RadioGroup radioGroup;

        public TemplateRadioView(TemplateData.Field.Radio data) {
            super(data);
        }

        @Override
        public View generate(Context context) {
            inflate(context, R.layout.form_radio_list);
            setLabel(R.id.formRadioListLabel);

            radioGroup = view.findViewById(R.id.formRadioListRadioGroup);

            for (TemplateData.RadioItem item : getData().items) {
                RadioButton radioButton = new RadioButton(context);
                radioButton.setText(item.label);
                radioGroup.addView(radioButton);
            }

            if (getData().other) {
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
                getData().selected = null;
            else
                getData().selected = checked;
        }

        @Override
        public TemplateData.Field.Radio getData() {
            return (TemplateData.Field.Radio) super.getData();
        }
    }

    public static class TemplateChecklist extends TemplateFieldView {
        private List<TemplateCheckbox> checkboxes;

        public TemplateChecklist(TemplateData.Field.Checklist data) {
            super(data);
        }

        @Override
        public View generate(Context context) {
            inflate(context, R.layout.form_check_list);
            setLabel(R.id.formCheckListLabel);

            LinearLayout checkGroup = view.findViewById(R.id.formCheckListCheckGroup);
            checkboxes = new ArrayList<>();

            for (TemplateData.Field.Checkbox item : getData().items) {
                TemplateCheckbox checkbox = new TemplateCheckbox(item);
                checkboxes.add(checkbox);
                checkGroup.addView(checkbox.generate(context));
            }

            return view;
        }

        @Override
        public void update() {
            for (TemplateCheckbox checkbox : checkboxes)
                checkbox.update();
        }

        @Override
        public TemplateData.Field.Checklist getData() {
            return (TemplateData.Field.Checklist) super.getData();
        }
    }

    public static class TemplateCheckbox extends TemplateFieldView {
        private CheckBox checkBox;

        public TemplateCheckbox(TemplateData.Field.Checkbox data) {
            super(data);
        }

        @Override
        public View generate(Context context) {
            checkBox = new CheckBox(context);
            checkBox.setText(data.label);

            return checkBox;
        }

        @Override
        public void update() {
            getData().checked = checkBox.isChecked();
        }

        @Override
        public TemplateData.Field.Checkbox getData() {
            return (TemplateData.Field.Checkbox) super.getData();
        }
    }

    public static class TemplateCounter extends TemplateFieldView {
        private Counter counter;

        public TemplateCounter(TemplateData.Field.Counter data) {
            super(data);
        }

        @Override
        public View generate(Context context) {
            counter = new Counter(context);
            counter.setText(data.label);

            Integer value = getData().value;
            if (value == null)
                counter.setValue(0);
            else
                counter.setValue(value);

            counter.setMin(getData().min);
            counter.setMax(getData().max);

            return counter;
        }

        @Override
        public void update() {
            getData().value = this.counter.getValue();
        }

        @Override
        public TemplateData.Field.Counter getData() {
            return (TemplateData.Field.Counter) super.getData();
        }
    }


    public abstract static class TemplateView<T> {
        protected View view;
        protected T data;

        public TemplateView(T data) {
            this.data = data;
        }

        public abstract View generate(Context context);
        public abstract void update();

        @SuppressLint("InflateParams")
        protected void inflate(Context context, @LayoutRes int resource) {
            this.view = LayoutInflater.from(context).inflate(resource, null);
        }

        public View getView() {
            return this.view;
        }

        public T getData() {
            return this.data;
        }
    }

    public abstract static class TemplateFieldView extends TemplateView<TemplateData.Field> {

        public TemplateFieldView(TemplateData.Field field) {
            super(field);
        }

        protected void setLabel(@IdRes int resource) {
            TextView textView = this.view.findViewById(resource);
            textView.setText(this.data.label);
        }
    }
}