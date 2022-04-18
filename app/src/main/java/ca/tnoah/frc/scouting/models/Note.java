package ca.tnoah.frc.scouting.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

@Entity(tableName = "notes")
public class Note {

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
    public String text;

}
