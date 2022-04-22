package ca.tnoah.frc.scouting.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Entity(tableName = "teams")
public class Team {

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

    @Nullable
    public byte[] image;
}
