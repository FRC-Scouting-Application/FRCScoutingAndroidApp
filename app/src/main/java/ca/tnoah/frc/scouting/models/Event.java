package ca.tnoah.frc.scouting.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Date;

@Entity(tableName = "events")
public class Event {

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

    @Nullable
    public String city;

    @Nullable
    public String stateProv;

    @Nullable
    public String country;

    @Nullable
    public String address;

    @Nullable
    public String postalCode;

    @Nullable
    public String locationName;

    @Nullable
    public String website;

}
