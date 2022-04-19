package ca.tnoah.frc.scouting.services.localdb;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import ca.tnoah.frc.scouting.models.Team;

@Dao
public interface TeamsDAO {

    @Query("SELECT * FROM teams")
    List<Team> getAll();

    @Query("SELECT * FROM teams WHERE `key` = :key")
    Team get(String key);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOrUpdate(Team team);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOrUpdate(List<Team> teams);

    @Delete
    void delete(Team team);

}