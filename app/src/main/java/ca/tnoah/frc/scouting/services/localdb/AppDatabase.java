package ca.tnoah.frc.scouting.services.localdb;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import ca.tnoah.frc.scouting.models.dbo.Event;
import ca.tnoah.frc.scouting.models.dbo.Match;
import ca.tnoah.frc.scouting.models.dbo.Note;
import ca.tnoah.frc.scouting.models.dbo.Scout;
import ca.tnoah.frc.scouting.models.dbo.Team;
import ca.tnoah.frc.scouting.models.dbo.Template;

@Database(entities = {Event.class, Match.class, Note.class, Scout.class, Team.class, Template.class}, version = 1)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {

    public abstract EventsDAO eventsDAO();
    public abstract MatchesDAO matchesDAO();
    public abstract NotesDAO notesDAO();
    public abstract ScoutsDAO scoutsDAO();
    public abstract TeamsDAO teamsDAO();
    public abstract TemplatesDAO templatesDAO();

}
