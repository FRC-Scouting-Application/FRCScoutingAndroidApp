package ca.tnoah.frc.scouting.services.sync.download;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import ca.tnoah.frc.scouting.models.dbo.Event;
import ca.tnoah.frc.scouting.services.ApiService;
import ca.tnoah.frc.scouting.services.DatabaseService;
import ca.tnoah.frc.scouting.services.localdb.AppDatabase;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class DownloadBase<T> {

    protected final String type;
    protected final String tag;

    protected final ApiService api;
    protected final AppDatabase db;

    protected DownloadBase(String type) {
        this.type = type;
        this.tag = "==Download" + type + "==";

        this.api = ApiService.getInstance();
        this.db = DatabaseService.getInstance().getDB();
    }

    protected void download(Call<List<T>> call) {
        call.enqueue(new Callback<List<T>>() {
            @Override
            public void onResponse(Call<List<T>> call, Response<List<T>> response) {
                if (!response.isSuccessful() || response.body() == null)
                    fail("response unsuccessful or body null");
                else if (response.body().size() == 0)
                    empty();
                else
                    onRetrieve(response.body());
            }

            @Override
            public void onFailure(Call<List<T>> call, Throwable t) {
                fail(t.getMessage());
            }
        });
    }

    protected abstract void onRetrieve(List<T> items);

    private void fail(String msg) {
        Log.d(tag, "Failed to get: " + type + " - " + msg);
    }

    private void empty() {
        Log.d(tag, "Empty: " + type);
    }

    protected void logThreadStart() {
        Log.d(tag, "Download " + tag + " Thread Started...");
    }

    protected void logThreadEnd() {
        Log.d(tag, "Download " + tag + " Thread Ended...");
    }

    protected List<String> getAllEventKeys() {
        List<Event> events = db.eventsDAO().getAll();

        List<String> keys = new ArrayList<>();
        for (Event event : events)
            keys.add(event.id);

        return keys;
    }

}