package ca.tnoah.frc.scouting.api.models;

import androidx.room.Entity;

import org.jetbrains.annotations.NotNull;

@Entity(tableName = "notes")
public class Note extends ScoutBase {

    @NotNull
    public String text;

}
