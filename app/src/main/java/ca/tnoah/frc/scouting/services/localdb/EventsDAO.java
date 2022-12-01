package ca.tnoah.frc.scouting.services.localdb;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import ca.tnoah.frc.scouting.models.dbo.Event;

@Dao
public interface EventsDAO {

    @Query("SELECT * FROM events ORDER BY `startDate` ASC")
    List<Event> getAll();

    @Query("SELECT * FROM events WHERE `id` = :id")
    Event get(String id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOrUpdate(Event event);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOrUpdate(List<Event> events);

    @Delete
    void delete(Event event);

}
