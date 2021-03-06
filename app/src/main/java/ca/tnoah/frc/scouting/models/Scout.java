package ca.tnoah.frc.scouting.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

@Entity(tableName = "scouts")
public class Scout {

    @PrimaryKey
    @NotNull
    public int id;

    @NotNull
    public String teamKey;

    @NotNull
    public String eventKey;

    @NotNull
    public String scoutName;

    @NotNull
    public int templateId;

    @NotNull
    public int templateVersion;

    @NotNull
    public String matchKey;

    @NotNull
    public byte[] XML;

}
