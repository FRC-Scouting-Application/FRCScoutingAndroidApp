package ca.tnoah.frc.scouting.models.template;

import com.google.gson.annotations.Expose;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class TemplateData {
    public String name;
    public String type;
    public String year;
    public List<Section> sections;

    public static class Section {
        public String header;
        public List<Field> fields;
    }

    public static class Field {

        @Expose
        public String type;
        public String label;
        public String key;

        public static class Text extends Field {
            public String value;
            public boolean multiline;
        }

        public static class Radio extends Field {
            public boolean other;
            public Integer selected;
            public String otherValue;
            public List<RadioItem> items;
        }

        public static class Checklist extends Field {
            public List<Checkbox> items;
        }

        public static class Counter extends Field {
            public Integer value;
            public Integer min;
            public Integer max;
        }

        public static class Checkbox extends Field {
            public boolean checked;
        }
    }

    public static class RadioItem {
        public String label;
    }

    @Nullable
    public Field get(@NotNull String key) {
        for (Section section : sections) {
            for (Field field : section.fields) {

                if (field.key != null && field.key.equals(key))
                    return field;
                if (field.label.equals(key))
                    return field;

            }
        }

        return null;
    }

    @Nullable
    public Field.Text getText(@NotNull String key) {
        Field field = get(key);

        if (field == null)
            return null;

        if (field instanceof Field.Text)
            return (Field.Text) field;

        return null;
    }
}