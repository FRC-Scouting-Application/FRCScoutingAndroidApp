package ca.tnoah.frc.scouting.services.sync.download;

import android.util.Log;

import java.util.List;

import ca.tnoah.frc.scouting.models.Team;
import retrofit2.Call;

public class DownloadTeams extends DownloadBase<Team> implements Runnable {

    private static final String TAG = "==DownloadEvents==";
    private static final String TYPE = "Events";

    public DownloadTeams() {
        super(TAG);
    }

    @Override
    public void run() {
        Log.d(tag, "Download Teams Thread Started...");

        Call<List<Team>> call = api.teams.getAllTeams();
        download(call, this::onRetrieve, TYPE);

        Log.d(tag, "Download Teams Thread Ended...");
    }

    public void onRetrieve(List<Team> teams) {
        db.teamsDAO().insertOrUpdate(teams);
    }

}