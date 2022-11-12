package ca.tnoah.frc.scouting.models.dbo;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

import ca.tnoah.frc.scouting.models.template.TemplateData;
import ca.tnoah.frc.scouting.models.template.TemplateSerializer;
import ca.tnoah.frc.scouting.services.DatabaseService;
import ca.tnoah.frc.scouting.services.localdb.AppDatabase;

@Entity(tableName = "scouts")
public class Scout {
    private static final AppDatabase db = DatabaseService.getInstance().getDB();

    @PrimaryKey
    @NotNull
    public String id;

    @NotNull
    public String teamKey;

    @NotNull
    public String eventKey;

    @Nullable
    public String scoutName;

    @NotNull
    public String templateId;

    @NotNull
    public int templateVersion;

    @Nullable
    public String matchKey;

    @Nullable
    public String data;

    public Scout() {

    }

    public Scout(@NonNull String teamKey, @NonNull String eventKey, @NonNull Template template) {
        this.id = String.valueOf(UUID.randomUUID());
        this.teamKey = teamKey;
        this.eventKey = eventKey;
        this.templateId = template.id;
        this.templateVersion = template.version;
        this.data = template.data;
    }

    @Nullable
    public String getMatchNumber() {
        int matchNumber = 0;

        if (matchKey != null)
            matchNumber = db.matchesDAO().get(matchKey).matchNumber;

        if (matchNumber > 0)
            return String.valueOf(matchNumber);

        if (data == null)
            return null;

        try {
            TemplateData templateData = TemplateSerializer.deserializeFromJson(data);

            TemplateData.Field.Text text = templateData.getText("match_number");
            if (text == null) text = templateData.getText("Match Number");

            if (text == null)
                return null;

            return text.value;
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    @Nullable
    public TemplateData getTemplateData() {
        if (data == null) {
            Template template = getTemplate();
            if (template == null) return null;

            data = template.data;
        }

        return TemplateSerializer.deserializeFromJson(data);
    }

    @Nullable
    public Template getTemplate() {
        return db.templatesDAO().get(this.templateId, this.templateVersion);
    }

    @Nullable
    public String getType() {
        Template template = getTemplate();
        if (template == null)
            return null;

        return template.type;
    }

    @Nullable
    public String getScoutName() {
        if (scoutName != null) return scoutName;

        TemplateData templateData = getTemplateData();
        if (templateData == null) return null;

        TemplateData.Field.Text text = getTemplateData().getText("scout_name");
        if (text == null) text = getTemplateData().getText("Scout Name");
        if (text == null) return null;
        return text.value;
    }
}
