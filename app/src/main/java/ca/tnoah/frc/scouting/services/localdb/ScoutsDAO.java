package ca.tnoah.frc.scouting.services.localdb;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import ca.tnoah.frc.scouting.models.Scout;

@Dao
public interface ScoutsDAO {

    @Query("SELECT * FROM scouts")
    List<Scout> getAll();

    @Query("SELECT * FROM scouts WHERE `id` = :id")
    Scout get(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOrUpdate(Scout scout);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOrUpdate(List<Scout> scouts);

    @Delete
    void delete(Scout scout);

}
