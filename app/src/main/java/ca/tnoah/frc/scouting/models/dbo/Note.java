package ca.tnoah.frc.scouting.models.dbo;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

@Entity(tableName = "notes")
public class Note {

    @PrimaryKey
    @NotNull
    public String id;

    @NotNull
    public String teamKey;

    @NotNull
    public String eventKey;

    @NotNull
    public String scoutName;

    @NotNull
    public String text;

}
