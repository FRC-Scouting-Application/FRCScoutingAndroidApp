package ca.tnoah.frc.scouting.services.sync.download;

import android.util.Log;

import java.util.List;

import ca.tnoah.frc.scouting.models.dbo.Team;
import retrofit2.Call;

public class DownloadTeams extends DownloadBase<Team> implements Runnable {

    public DownloadTeams() {
        super("Teams");
    }

    @Override
    public void run() {
        logThreadStart();

        Call<List<Team>> call = api.teams.getAllTeams();
        download(call);

        logThreadEnd();
    }

    @Override
    public void onRetrieve(List<Team> teams) {
        db.teamsDAO().insertOrUpdate(teams);
    }

}