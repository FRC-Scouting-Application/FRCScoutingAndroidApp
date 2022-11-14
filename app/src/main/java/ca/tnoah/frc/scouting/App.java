package ca.tnoah.frc.scouting;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.util.Log;

import java.io.File;
import java.util.List;
import java.util.Objects;

import ca.tnoah.frc.scouting.models.dbo.Scout;
import ca.tnoah.frc.scouting.models.dbo.Template;
import ca.tnoah.frc.scouting.services.DatabaseService;
import ca.tnoah.frc.scouting.services.localdb.AppDatabase;
import ca.tnoah.frc.scouting.services.sync.SyncService;

public class App extends Application {

    private static final String TAG = "==App==";

    @SuppressLint("StaticFieldLeak")
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;

        SyncService.getInstance(context);
        //startUpTest();
    }

    public static Context getContext() {
        return context;
    }

    public void startUpTest() {
        Log.d(TAG, "Startup Test Start!");

        List<Scout> scouts =  DatabaseService.getInstance().getDB().scoutsDAO().getAll();
        List<Scout> scouts1 = DatabaseService.getInstance().getDB().scoutsDAO().getAll("notes", "2022on325", "frc1");

        Log.d(TAG, "Startup Test End!");
    }
}
