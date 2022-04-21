package ca.tnoah.frc.scouting.services;

import androidx.room.Room;

import ca.tnoah.frc.scouting.App;
import ca.tnoah.frc.scouting.services.localdb.AppDatabase;

public class DatabaseService {
    private static DatabaseService instance;

    private final AppDatabase db;

    public static synchronized DatabaseService getInstance() {
        if (instance == null)
            instance = new DatabaseService();
        return instance;
    }

    private DatabaseService() {
        db = Room.databaseBuilder(App.getContext(), AppDatabase.class, "Scouting")
                .allowMainThreadQueries()
                .build();
    }

    public AppDatabase getDB() {
        return db;
    }
}
