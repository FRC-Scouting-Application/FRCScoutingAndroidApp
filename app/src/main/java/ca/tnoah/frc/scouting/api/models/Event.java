package ca.tnoah.frc.scouting.api.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Date;

@Entity(tableName = "teams")
public class Event extends Location {

    @PrimaryKey
    @NotNull
    public String key;

    @NotNull
    public String name;

    @Nullable
    public String shortName;

    @NotNull
    public Date startDate;

    @NotNull
    public Date endDate;

    @NotNull
    public int year;

    @NotNull
    public String eventType;

    @Nullable
    public int week;

}
