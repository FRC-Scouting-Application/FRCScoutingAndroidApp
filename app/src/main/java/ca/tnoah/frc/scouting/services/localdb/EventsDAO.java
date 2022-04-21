package ca.tnoah.frc.scouting.services.localdb;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import ca.tnoah.frc.scouting.models.Event;

@Dao
public interface EventsDAO {

    @Query("SELECT * FROM events")
    List<Event> getAll();

    @Query("SELECT * FROM events WHERE `key` = :key")
    Event get(String key);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOrUpdate(Event event);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOrUpdate(List<Event> events);

    @Delete
    void delete(Event event);

}
