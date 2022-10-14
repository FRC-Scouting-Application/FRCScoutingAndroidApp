package ca.tnoah.frc.scouting.services.sync.download;

import android.util.Log;

import java.util.List;

import ca.tnoah.frc.scouting.models.dbo.Event;
import retrofit2.Call;

public class DownloadEvents extends DownloadBase<Event> implements Runnable  {

    public DownloadEvents() {
        super("Events");
    }

    @Override
    public void run() {
        Log.d(tag, "Download Events Thread Started...");

        Call<List<Event>> call = api.events.getEvents();
        download(call);

        Log.d(tag, "Download Events Thread Ended...");
    }

    @Override
    public void onRetrieve(List<Event> events) {
        db.eventsDAO().insertOrUpdate(events);
    }

}