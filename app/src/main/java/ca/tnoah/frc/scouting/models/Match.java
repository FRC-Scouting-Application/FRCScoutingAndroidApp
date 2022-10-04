package ca.tnoah.frc.scouting.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Date;

@Entity(tableName = "matches")
public class Match {

    @PrimaryKey
    @NotNull
    public String id;

    @NotNull
    public int matchNumber;

    @Nullable
    public String red1;

    @Nullable
    public String red2;

    @Nullable
    public String red3;

    @Nullable
    public String blue1;

    @Nullable
    public String blue2;

    @Nullable
    public String blue3;

    @NotNull
    public String eventKey;

    @Nullable
    public int redScore;

    @Nullable
    public int blueScore;

    @Nullable
    public String winningAlliance;

    @Nullable
    public Date time;

    @Nullable
    public Date actualTime;

    @Nullable
    public Date predictedTime;

}
