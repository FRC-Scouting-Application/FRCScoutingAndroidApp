package ca.tnoah.frc.scouting.models.template;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import ca.tnoah.frc.scouting.helpers.RuntimeTypeAdapterFactory;

public class TemplateSerializer {

    private static final RuntimeTypeAdapterFactory<TemplateData.Field> typeFactory =
            RuntimeTypeAdapterFactory.of(TemplateData.Field.class, "type")
                    .registerSubtype(TemplateData.Field.Checkbox.class, "checkbox")
                    .registerSubtype(TemplateData.Field.Checklist.class, "checklist")
                    .registerSubtype(TemplateData.Field.Counter.class, "counter")
                    .registerSubtype(TemplateData.Field.Radio.class, "radio")
                    .registerSubtype(TemplateData.Field.Text.class, "text");



    //region Deserialize

    public static TemplateData deserializeFromJson(Context context, String path) {
        try {
            InputStream is = context.getAssets().open(path);

            byte[] buffer = new byte[10];
            StringBuilder sb = new StringBuilder();

            while (is.read(buffer) != -1) {
                sb.append(new String(buffer, StandardCharsets.UTF_8));
                buffer = new byte[10];
            }

            return deserializeFromJson(sb.toString());
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static TemplateData deserializeFromJson(String json) {
        Gson gson = new GsonBuilder().registerTypeAdapterFactory(typeFactory).create();
        return gson.fromJson(json.trim(), TemplateData.class);
    }
    //endregion

    //region Serialize
    //endregion

    //region Examples

    public static TemplateData getTestPit(Context context) {
        return deserializeFromJson(context, "templates/template_pit_example.json");
    }

    public static TemplateData getNotes(Context context) {
        return deserializeFromJson(context, "templates/template_notes.json");
    }

    //endregion

}