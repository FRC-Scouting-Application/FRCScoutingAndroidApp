package ca.tnoah.frc.scouting.api.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Entity(tableName = "teams")
public class Team extends Location {

    @PrimaryKey
    @NotNull
    public String key;

    @NotNull
    public int teamNumber;

    @Nullable
    public String nickname;

    @NotNull
    public String name;

    @Nullable
    public int rookieYear;

}
