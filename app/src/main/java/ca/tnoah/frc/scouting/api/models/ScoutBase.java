package ca.tnoah.frc.scouting.api.models;

import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

public class ScoutBase {

    @PrimaryKey
    @NotNull
    public int id;

    @NotNull
    public String teamKey;

    @NotNull
    public String eventKey;

    @NotNull
    public String scoutName;

}
