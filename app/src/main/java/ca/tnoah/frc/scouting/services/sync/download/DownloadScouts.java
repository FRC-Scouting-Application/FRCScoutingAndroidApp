package ca.tnoah.frc.scouting.services.sync.download;

import java.util.List;

import ca.tnoah.frc.scouting.models.dbo.Scout;
import retrofit2.Call;

public class DownloadScouts extends DownloadBase<Scout> implements Runnable {

    public DownloadScouts() {
        super("Scouts");
    }

    @Override
    public void run() {
        logThreadStart();

        Call<List<Scout>> call = api.scouts.getScouts();
        download(call);

        logThreadEnd();
    }

    @Override
    protected void onRetrieve(List<Scout> items) {
        db.scoutsDAO().insertOrUpdate(items);
    }
}