package ca.tnoah.frc.scouting.services.sync;

import java.util.Date;

import ca.tnoah.frc.scouting.services.ApiService;
import ca.tnoah.frc.scouting.services.DatabaseService;
import ca.tnoah.frc.scouting.services.localdb.AppDatabase;
import ca.tnoah.frc.scouting.services.sync.download.DownloadEvents;
import ca.tnoah.frc.scouting.services.sync.download.DownloadTeams;
import ca.tnoah.frc.scouting.services.sync.download.DownloadTemplates;

public class DownloadService {
    private static DownloadService instance;

    private final DownloadEvents events = new DownloadEvents();
    private final DownloadTeams teams = new DownloadTeams();
    private final DownloadTemplates templates = new DownloadTemplates();

    private Date timeSinceLastUpdate = null;

    protected static synchronized DownloadService getInstance() {
        if (instance == null)
            instance = new DownloadService();
        return instance;
    }

    private DownloadService() {

    }

    protected void download() {
        downloadKeylessItems();
    }

    protected void downloadKeylessItems() {
        Thread eventsThread = new Thread(events);
        Thread teamsThread = new Thread(teams);
        Thread templatesThread = new Thread(templates);

        eventsThread.start();
        teamsThread.start();
        templatesThread.start();

        timeSinceLastUpdate = new Date();
    }


    /*private void downloadMatches(String eventKey) {
        String type = "Matches";

        Call<List<Match>> call = api.matches.getMatches(eventKey);
        call.enqueue(new Callback<List<Match>>() {
            @Override
            public void onResponse(Call<List<Match>> call, Response<List<Match>> response) {
                if (!response.isSuccessful() || response.body() == null)
                    fail(type);
                else if (response.body().size() == 0)
                    empty(type);
                else
                    db.matchesDAO().insertOrUpdate(response.body());
            }

            @Override
            public void onFailure(Call<List<Match>> call, Throwable t) {
                fail(type + " - " + t.getMessage());
            }
        });
    }

    private void downloadNotes(String eventKey) {
        String type = "Notes";

        Call<List<Note>> call = api.notes.getNotesByEvent(eventKey);
        call.enqueue(new Callback<List<Note>>() {
            @Override
            public void onResponse(Call<List<Note>> call, Response<List<Note>> response) {
                if (!response.isSuccessful() || response.body() == null)
                    fail(type);
                else if (response.body().size() == 0)
                    empty(type);
                else
                    db.notesDAO().insertOrUpdate(response.body());
            }

            @Override
            public void onFailure(Call<List<Note>> call, Throwable t) {
                fail(type + " - " + t.getMessage());
            }
        });
    }

    private void downloadScouts(String eventKey) {
        String type = "Scouts";

        Call<List<Scout>> call = api.scouts.getScoutsByEvent(eventKey);
        call.enqueue(new Callback<List<Scout>>() {
            @Override
            public void onResponse(Call<List<Scout>> call, Response<List<Scout>> response) {
                if (!response.isSuccessful() || response.body() == null)
                    fail(type);
                else if (response.body().size() == 0)
                    empty(type);
                else
                    db.scoutsDAO().insertOrUpdate(response.body());
            }

            @Override
            public void onFailure(Call<List<Scout>> call, Throwable t) {
                fail(type + " - " + t.getMessage());
            }
        });
    }*/
}
