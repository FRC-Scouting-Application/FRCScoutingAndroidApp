package ca.tnoah.frc.scouting.models.template;

import java.util.ArrayList;
import java.util.List;

public class TemplateData {
    public String name;
    public String type;
    public String year;
    public List<Section> sections;

    public TemplateData() {
        this.sections = new ArrayList<>();
    }

    public TemplateData(String name, String type) {
        this(name, type, "2022");
    }

    public TemplateData(String name, String type, String year) {
        this();

        this.name = name;
        this.type = type;
        this.year = year;
    }

    public static class Section {
        public String header;
        public List<Field> fields;
    }

    public abstract static class Field {
        public String label;
        public boolean required;
    }

    public static class TextField extends Field {
        public String value;
        public boolean multiline;
    }

    public static class RadioList extends Field {
        public boolean other;
        public List<Item> items;
    }

    public static class CheckList extends Field {
        public List<Item> items;
    }

    public static class Counter extends Field {
        public int max;
        public int min;
        public int value;
    }

    public static class Item {
        public String label;
        public boolean selected;
    }
}