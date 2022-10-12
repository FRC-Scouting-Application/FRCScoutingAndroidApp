package ca.tnoah.frc.scouting.services.sync.download;

import android.util.Log;

import java.util.List;

import ca.tnoah.frc.scouting.models.Event;
import retrofit2.Call;

public class DownloadEvents extends DownloadBase<Event> implements Runnable  {

    private static final String TAG = "==DownloadEvents==";
    private static final String TYPE = "Events";

    public DownloadEvents() {
        super(TAG);
    }

    @Override
    public void run() {
        Log.d(tag, "Download Events Thread Started...");

        Call<List<Event>> call = api.events.getEvents();
        download(call, this::onRetrieve, TYPE);

        Log.d(tag, "Download Events Thread Ended...");
    }

    public void onRetrieve(List<Event> events) {
        db.eventsDAO().insertOrUpdate(events);
    }

}