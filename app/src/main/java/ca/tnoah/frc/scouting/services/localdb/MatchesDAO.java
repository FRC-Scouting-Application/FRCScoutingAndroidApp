package ca.tnoah.frc.scouting.services.localdb;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import ca.tnoah.frc.scouting.models.dbo.Match;

@Dao
public interface MatchesDAO {

    @Query("SELECT * FROM matches ORDER BY `matchNumber` ASC")
    List<Match> getAll();

    @Query("SELECT * FROM matches WHERE `id` = :id")
    Match get(String id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOrUpdate(Match match);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOrUpdate(List<Match> matches);

    @Delete
    void delete(Match match);

}
