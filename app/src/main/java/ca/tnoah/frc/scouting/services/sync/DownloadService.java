package ca.tnoah.frc.scouting.services.sync;

import android.os.AsyncTask;
import android.util.Log;

import java.util.List;

import ca.tnoah.frc.scouting.models.Event;
import ca.tnoah.frc.scouting.models.Match;
import ca.tnoah.frc.scouting.models.Note;
import ca.tnoah.frc.scouting.models.Scout;
import ca.tnoah.frc.scouting.models.Team;
import ca.tnoah.frc.scouting.models.Template;
import ca.tnoah.frc.scouting.services.ApiService;
import ca.tnoah.frc.scouting.services.DatabaseService;
import ca.tnoah.frc.scouting.services.api.ApiEventsService;
import ca.tnoah.frc.scouting.services.localdb.AppDatabase;
import ca.tnoah.frc.scouting.services.localdb.EventsDAO;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DownloadService {
    private static DownloadService instance;
    private static final String TAG = "==DownloadService==";

    private final ApiService api;
    private final AppDatabase db;

    public static DownloadService getInstance() {
        if (instance == null)
            instance = new DownloadService();
        return instance;
    }

    private DownloadService() {
        this.api = ApiService.getInstance();
        this.db = DatabaseService.getInstance().getDB();
    }

    private class DownloadAsync extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {
            String eventKey = strings[0];
            if (eventKey.isEmpty()) {
                Log.d(TAG, "Event Key Missing");
                return null;
            }

            downloadEvents();
            downloadMatches(eventKey);
            downloadNotes(eventKey);
            downloadNotes(eventKey);
            downloadTeams();
            downloadTemplates();

            return null;
        }

        private void downloadEvents() {
            String type = "Events";

            Call<List<Event>> call = api.events.getEvents();
            call.enqueue(new Callback<List<Event>>() {
                @Override
                public void onResponse(Call<List<Event>> call, Response<List<Event>> response) {
                    if (response.isSuccessful() || response.body() == null)
                        fail(type);
                    else if (response.body().size() == 0)
                        empty(type);
                    else
                        db.eventsDAO().insertOrUpdate(response.body());
                }

                @Override
                public void onFailure(Call<List<Event>> call, Throwable t) {
                    fail(type);
                }
            });
        }

        private void downloadMatches(String eventKey) {
            String type = "Matches";

            Call<List<Match>> call = api.matches.getMatches(eventKey);
            call.enqueue(new Callback<List<Match>>() {
                @Override
                public void onResponse(Call<List<Match>> call, Response<List<Match>> response) {
                    if (response.isSuccessful() || response.body() == null)
                        fail(type);
                    else if (response.body().size() == 0)
                        empty(type);
                    else
                        db.matchesDAO().insertOrUpdate(response.body());
                }

                @Override
                public void onFailure(Call<List<Match>> call, Throwable t) {
                    fail(type);
                }
            });
        }

        private void downloadNotes(String eventKey) {
            String type = "Notes";

            Call<List<Note>> call = api.notes.getNotesByEvent(eventKey);
            call.enqueue(new Callback<List<Note>>() {
                @Override
                public void onResponse(Call<List<Note>> call, Response<List<Note>> response) {
                    if (response.isSuccessful() || response.body() == null)
                        fail(type);
                    else if (response.body().size() == 0)
                        empty(type);
                    else
                        db.notesDAO().insertOrUpdate(response.body());
                }

                @Override
                public void onFailure(Call<List<Note>> call, Throwable t) {
                    fail(type);
                }
            });
        }

        private void downloadScouts(String eventKey) {
            String type = "Scouts";

            Call<List<Scout>> call = api.scouts.getScoutsByEvent(eventKey);
            call.enqueue(new Callback<List<Scout>>() {
                @Override
                public void onResponse(Call<List<Scout>> call, Response<List<Scout>> response) {
                    if (response.isSuccessful() || response.body() == null)
                        fail(type);
                    else if (response.body().size() == 0)
                        empty(type);
                    else
                        db.scoutsDAO().insertOrUpdate(response.body());
                }

                @Override
                public void onFailure(Call<List<Scout>> call, Throwable t) {
                    fail(type);
                }
            });
        }

        private void downloadTeams() {
            String type = "Teams";

            Call<List<Team>> call = api.teams.getAllTeams();
            call.enqueue(new Callback<List<Team>>() {
                @Override
                public void onResponse(Call<List<Team>> call, Response<List<Team>> response) {
                    if (response.isSuccessful() || response.body() == null)
                        fail(type);
                    else if (response.body().size() == 0)
                        empty(type);
                    else
                        db.teamsDAO().insertOrUpdate(response.body());
                }

                @Override
                public void onFailure(Call<List<Team>> call, Throwable t) {
                    fail(type);
                }
            });
        }

        private void downloadTemplates() {
            String type = "Templates";

            Call<List<Template>> call = api.templates.getTemplates();
            call.enqueue(new Callback<List<Template>>() {
                @Override
                public void onResponse(Call<List<Template>> call, Response<List<Template>> response) {
                    if (response.isSuccessful() || response.body() == null)
                        fail(type);
                    else if (response.body().size() == 0)
                        empty(type);
                    else
                        db.templatesDAO().insertOrUpdate(response.body());
                }

                @Override
                public void onFailure(Call<List<Template>> call, Throwable t) {
                    fail(type);;
                }
            });
        }

        private void fail(String msg) {
            Log.d(TAG, "Failed to get: " + msg);
        }

        private void empty(String msg) {
            Log.d(TAG, "Empty: " + msg);
        }
    }
}
