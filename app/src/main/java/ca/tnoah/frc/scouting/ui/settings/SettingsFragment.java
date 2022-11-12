package ca.tnoah.frc.scouting.ui.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import ca.tnoah.frc.scouting.App;
import ca.tnoah.frc.scouting.Preferences;
import ca.tnoah.frc.scouting.R;
import ca.tnoah.frc.scouting.models.Setting;
import ca.tnoah.frc.scouting.services.DatabaseService;
import ca.tnoah.frc.scouting.ui.MainViewModel;

public class SettingsFragment extends PreferenceFragmentCompat {

    // DEVELOPER MODE
    private static final String DEV_MODE = "DEV_MODE";
    private static final int CLICKS_TO_DEV = 5;
    private int versionClicks = 0;
    private boolean developerMode = false;

    private PreferenceScreen screen;
    private Context context;

    private SharedPreferences settings;

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreatePreferences(@Nullable Bundle savedInstanceState, @Nullable String rootKey) {
        setPreferencesFromResource(R.xml.settings_main, rootKey);
        context = getPreferenceManager().getContext();
        screen = getPreferenceManager().getPreferenceScreen();

        settings = Preferences.get(context, Preferences.SETTINGS);
        if (settings.getBoolean(DEV_MODE, false))
            enableDeveloperMode();

        Preference version = findPreference("version");
        if (version != null) version.setOnPreferenceClickListener(this::onVersionClicked);
    }

    private void enableDeveloperMode() {
        developerMode = true;

        SharedPreferences.Editor settings = this.settings.edit();
        settings.putBoolean(DEV_MODE, true);
        settings.apply();

        Preference clearAllData = new Preference(context);
        clearAllData.setKey("clear_all_data");
        clearAllData.setTitle(R.string.clear_all_data);
        clearAllData.setSummary(R.string.clear_all_data_desc);
        clearAllData.setOnPreferenceClickListener(this::onClearAllData);

        PreferenceCategory developer = new PreferenceCategory(context);
        developer.setKey("developer");
        developer.setTitle(R.string.developer);
        screen.addPreference(developer);
        developer.addPreference(clearAllData);

        setPreferenceScreen(screen);
    }

    private boolean onVersionClicked(Preference preference) {
        versionClicks++;

        if (versionClicks >= CLICKS_TO_DEV && !developerMode) {
            Toast toast = Toast.makeText(context, "Developer Mode Enabled!", Toast.LENGTH_LONG);
            toast.show();
            enableDeveloperMode();
        }
        else if (versionClicks >= 3) {
            int clicksLeft = CLICKS_TO_DEV - versionClicks;

            Toast toast = Toast.makeText(context,
                    clicksLeft + " clicks to developer", Toast.LENGTH_SHORT);
            toast.show();
        }

        return true;
    }

    private boolean onClearAllData(Preference preference) {
        DatabaseService.getInstance().resetDB(context);
        return true;
    }
}