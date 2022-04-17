package ca.tnoah.frc.scouting.api.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

import java.util.Date;

@Entity(tableName = "templates")
public class Template {

    @PrimaryKey
    @NotNull
    public int id;

    @PrimaryKey
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
