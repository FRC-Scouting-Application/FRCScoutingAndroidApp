package ca.tnoah.frc.scouting.ui.teams.details;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import ca.tnoah.frc.scouting.models.Team;

public class TeamViewModel extends ViewModel {

    private MutableLiveData<Team> team = new MutableLiveData<>();
    private MutableLiveData<Boolean> mainPage = new MutableLiveData<>();

    public LiveData<Team> getTeam() {
        if (this.team == null)
            this.team = new MutableLiveData<>();
        return this.team;
    }

    public void setTeam(Team team) {
        this.team.setValue(team);
    }

    public LiveData<Boolean> getMainPage() {
        if (this.mainPage == null)
            this.mainPage = new MutableLiveData<>(true);
        return this.mainPage;
    }

    public void setMainPage(boolean mainPage) {
        this.mainPage.setValue(mainPage);
    }
}
