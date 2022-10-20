package ca.tnoah.frc.scouting.models.template;

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
        public String type;
        public String label;

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
}