package ca.tnoah.frc.scouting.services.sync.download;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import ca.tnoah.frc.scouting.models.Event;
import ca.tnoah.frc.scouting.services.ApiService;
import ca.tnoah.frc.scouting.services.DatabaseService;
import ca.tnoah.frc.scouting.services.localdb.AppDatabase;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class DownloadBase<T> {

    protected final String tag;
    protected final ApiService api;
    protected final AppDatabase db;

    protected DownloadBase(String tag) {
        this.tag = tag;

        this.api = ApiService.getInstance();
        this.db = DatabaseService.getInstance().getDB();
    }

    protected void download(Call<List<T>> call, Consumer<List<T>> onRetrieve, String type) {
        call.enqueue(new Callback<List<T>>() {
            @Override
            public void onResponse(Call<List<T>> call, Response<List<T>> response) {
                if (!response.isSuccessful() || response.body() == null)
                    fail(type);
                else if (response.body().size() == 0)
                    empty(type);
                else
                    onRetrieve.accept(response.body());
            }

            @Override
            public void onFailure(Call<List<T>> call, Throwable t) {
                fail(type + " - " + t.getMessage());
            }
        });
    }

    private void fail(String msg) {
        Log.d(tag, "Failed to get: " + msg);
    }

    private void empty(String msg) {
        Log.d(tag, "Empty: " + msg);
    }

    protected List<String> getAllEventKeys() {
        List<Event> events = db.eventsDAO().getAll();

        List<String> keys = new ArrayList<>();
        for (Event event : events)
            keys.add(event.id);

        return keys;
    }

}