package ca.tnoah.frc.scouting.ui.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import ca.tnoah.frc.scouting.App;
import ca.tnoah.frc.scouting.Preferences;
import ca.tnoah.frc.scouting.R;
import ca.tnoah.frc.scouting.databinding.ActivityWelcomeBinding;
import ca.tnoah.frc.scouting.ui.MainActivity;

public class WelcomeActivity extends AppCompatActivity {
    private ActivityWelcomeBinding binding;

    private static final String APP_MODE_PREF = "APP_MODE";
    private SharedPreferences settings;

    public interface AppMode {
        String STANDALONE = "STANDALONE";
        String WITH_TEAM = "WITH_TEAM";
        String CUSTOM = "CUSTOM";
        String UNSET = "UNSET";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWelcomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnWithTeam.setOnClickListener((v) -> buttonPressed(AppMode.WITH_TEAM));
        binding.btnCustom.setOnClickListener((v) -> buttonPressed(AppMode.CUSTOM));
        binding.btnStandalone.setOnClickListener((v) -> buttonPressed(AppMode.STANDALONE));

        settings = Preferences.get(this, Preferences.SETTINGS);

        String currentMode = settings.getString(APP_MODE_PREF, AppMode.UNSET);
        if (!currentMode.equals(AppMode.UNSET))
            buttonPressed(currentMode);
    }

    private void buttonPressed(String appMode) {
        switch (appMode) {
            case AppMode.WITH_TEAM:
            case AppMode.CUSTOM:
                startLoginActivity(appMode);
                break;
            case AppMode.STANDALONE:
            default:
                startStandalone();
                break;
        }
    }

    private void startLoginActivity(String appMode) {
        setAppModePref(appMode);

        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra(LoginActivity.INTENT_MODE, appMode);
        startActivity(intent);
    }

    private void startStandalone() {
        setAppModePref(AppMode.STANDALONE);

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(LoginActivity.INTENT_MODE, AppMode.STANDALONE);
        startActivity(intent);
    }

    private void setAppModePref(String appMode) {
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(APP_MODE_PREF, appMode);
        editor.apply();
    }

    public static String getAppMode(@NotNull Context context) {
        SharedPreferences settings = Preferences.get(context, Preferences.SETTINGS);
        return settings.getString(APP_MODE_PREF, AppMode.UNSET);
    }

    public static void reset(@NotNull Context context) {
        SharedPreferences.Editor editor = Preferences.get(context, Preferences.SETTINGS).edit();
        editor.putString(APP_MODE_PREF, AppMode.UNSET);
        editor.apply();
    }
}