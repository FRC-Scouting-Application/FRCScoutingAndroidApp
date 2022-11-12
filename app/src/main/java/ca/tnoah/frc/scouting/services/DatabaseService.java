package ca.tnoah.frc.scouting.services;

import android.content.Context;

import androidx.room.Room;

import ca.tnoah.frc.scouting.App;
import ca.tnoah.frc.scouting.services.localdb.AppDatabase;

public class DatabaseService {
    private static DatabaseService instance;
    private static final String DB_NAME = "Scouting";

    private final AppDatabase db;

    public static synchronized DatabaseService getInstance() {
        if (instance == null)
            instance = new DatabaseService();
        return instance;
    }

    private DatabaseService() {
        db = Room.databaseBuilder(App.getContext(), AppDatabase.class, DB_NAME)
                .allowMainThreadQueries()
                .build();
    }

    public AppDatabase getDB() {
        return db;
    }

    public void resetDB(Context context) {
        context.deleteDatabase(DB_NAME);
        instance = new DatabaseService();
    }
}
