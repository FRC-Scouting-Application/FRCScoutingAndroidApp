package ca.tnoah.frc.scouting.models;

import androidx.annotation.DrawableRes;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Setting {

    public String title;

    public String[] values;
    public int valuePos;

    public String description;
    public SettingType type;

    public int image;

    public enum SettingType {
        NONE,
        TITTLE,
        TOGGLE,
        LINK,
        MENU,
        CUSTOM
    }

    public Setting(@NotNull String title, @Nullable String[] values, @Nullable String description,
                   @Nullable SettingType type, @DrawableRes int image) {
        this.title = title;
        this.values = values;
        this.description = description;

        if (type == null)
            this.type = SettingType.NONE;
        else
            this.type = type;

        this.valuePos = 0;
        this.image = image;
    }

    public Setting(@NotNull String title) {
        this(title, null, null, SettingType.TITTLE, -1);
    }

}
