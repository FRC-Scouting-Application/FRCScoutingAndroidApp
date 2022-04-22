package ca.tnoah.frc.scouting.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@Entity(tableName = "events")
public class Event {
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MMM d", Locale.CANADA);
    private static final SimpleDateFormat YEAR_FORMAT = new SimpleDateFormat("yyyy", Locale.CANADA);

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


    public String getDateString() {
        String startStr = DATE_FORMAT.format(startDate);
        String endStr = DATE_FORMAT.format(endDate);

        String date = startStr;
        if (!startStr.equalsIgnoreCase(endStr))
            date += " to " + endStr;

        date += ", " + YEAR_FORMAT.format(startDate);
        return date;
    }

    public String getShortLocationString() {
        return String.format(Locale.CANADA, "%s, %s, %s", city, stateProv, country);
    }

    public String getLongLocationString() {
        return String.format(Locale.CANADA, "%s in %s", locationName, getShortLocationString());
    }

    public String getName() {
        if (shortName == null || shortName.isEmpty())
            return name;
        return shortName;
    }
}
