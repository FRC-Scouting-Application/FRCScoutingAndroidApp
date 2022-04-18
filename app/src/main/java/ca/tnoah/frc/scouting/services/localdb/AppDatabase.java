package ca.tnoah.frc.scouting.services.localdb;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import ca.tnoah.frc.scouting.models.*;

@Database(entities = {Event.class, Match.class, Note.class, Scout.class, Team.class, Template.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    public abstract EventsDAO eventsDAO();
    public abstract MatchesDAO matchesDAO();
    public abstract NotesDAO notesDAO();
    public abstract ScoutsDAO scoutsDAO();
    public abstract TeamsDAO teamsDAO();
    public abstract TemplatesDAO templatesDAO();

}
