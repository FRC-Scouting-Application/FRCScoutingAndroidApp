package ca.tnoah.frc.scouting.services.localdb;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import ca.tnoah.frc.scouting.models.dbo.Scout;

@Dao
public interface ScoutsDAO {

    @Query("SELECT * FROM scouts ORDER BY `scoutName` ASC")
    List<Scout> getAll();

    @Query("SELECT * FROM scouts " +
            "JOIN templates ON scouts.templateId = templates.id " +
            "AND scouts.templateVersion = templates.version " +
            "WHERE templates.type = :type AND scouts.eventKey = :eventKey " +
            "AND scouts.teamKey = :teamKey " +
            "ORDER BY `scoutName` ASC")
    List<Scout> getAll(String type, String eventKey, String teamKey);

    @Query("SELECT * FROM scouts WHERE `id` = :id")
    Scout get(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOrUpdate(Scout scout);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOrUpdate(List<Scout> scouts);

    @Delete
    void delete(Scout scout);

}
