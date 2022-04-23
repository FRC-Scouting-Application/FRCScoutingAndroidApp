package ca.tnoah.frc.scouting.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.Locale;

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

    public String getShortLocationString() {
        return String.format(Locale.CANADA, "%s, %s, %s", city, stateProv, country);
    }

    public boolean filter(String filter) {
        filter = filter.toLowerCase();
        String[] filters = filter.split(" ");

        Field[] fields = this.getClass().getDeclaredFields();

        for (String value : filters) {
            boolean match = false;
            for (Field f : fields) {
                try {
                    String fValue = String.valueOf(f.get(this));
                    if (fValue.toLowerCase().contains(value)) {
                        match = true;
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            if (!match) return false;
        }

        return true;
    }
}
