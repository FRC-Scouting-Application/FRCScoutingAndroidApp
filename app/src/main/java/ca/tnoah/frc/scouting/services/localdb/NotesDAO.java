package ca.tnoah.frc.scouting.services.localdb;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import ca.tnoah.frc.scouting.models.Note;

@Dao
public interface NotesDAO {

    @Query("SELECT * FROM notes")
    List<Note> getAll();

    @Query("SELECT * FROM notes WHERE `id` = :id")
    Note get(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOrUpdate(Note note);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOrUpdate(List<Note> notes);

    @Delete
    void delete(Note note);

}
