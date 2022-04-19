package ca.tnoah.frc.scouting;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import ca.tnoah.frc.scouting.services.sync.SyncService;

public class App extends Application {

    @SuppressLint("StaticFieldLeak")
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;

        SyncService.getInstance().Sync();
    }

    public static Context getContext() {
        return context;
    }
}
