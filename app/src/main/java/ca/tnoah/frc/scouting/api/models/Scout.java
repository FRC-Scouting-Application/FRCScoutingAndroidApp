package ca.tnoah.frc.scouting.api.models;

import androidx.room.Entity;

import org.jetbrains.annotations.NotNull;

@Entity(tableName = "scouts")
public class Scout extends ScoutBase {

    @NotNull
    public int templateId;

    @NotNull
    public int templateVersion;

    @NotNull
    public String matchKey;

    @NotNull
    public byte[] XML;

}
