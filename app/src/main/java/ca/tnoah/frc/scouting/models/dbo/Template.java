package ca.tnoah.frc.scouting.models.dbo;

import androidx.room.Entity;

import org.jetbrains.annotations.NotNull;

import java.util.Date;

@Entity(tableName = "templates", primaryKeys = {"id", "version"})
public class Template {

    public enum Type {
        PIT, MATCH, NOTES
    }

    @NotNull
    public int id;

    @NotNull
    public int version;

    @NotNull
    public String type;

    @NotNull
    public String name;

    @NotNull
    public boolean defaultTemplate;

    @NotNull
    public Date created;

    @NotNull
    public byte[] XML;

}
