package ca.tnoah.frc.scouting.ui.teams.details.team;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Locale;

import ca.tnoah.frc.scouting.R;
import ca.tnoah.frc.scouting.models.Team;
import ca.tnoah.frc.scouting.ui.teams.details.TeamDetailActivity;
import ca.tnoah.frc.scouting.ui.teams.details.TeamViewModel;

public class TeamFragment extends Fragment {
    private static final String TAG = "==TeamFragment==";

    public TeamFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_team, container, false);

        TeamViewModel viewModel = new ViewModelProvider(requireActivity()).get(TeamViewModel.class);
        viewModel.setMainPage(true);
        viewModel.getTeam().observe(this, this::onTeamChanged);

        return view;
    }

    public void onTeamChanged(Team team) {
        View view = getView();
        if (view == null) {
            Log.d(TAG, "View not found!");
            return;
        } else if (team == null) {
            Log.d(TAG, "Team null!");
            return;
        }

        String location = String.format(Locale.CANADA, "%s %s",
                getString(R.string.team_location), team.getShortLocationString());

        String aka = String.format(Locale.CANADA, "%s %s",
                getString(R.string.team_aka), team.name);

        String rookieYear = String.format(Locale.CANADA, "%s %s",
                getString(R.string.team_rookie_year), team.rookieYear);

        ((TextView) view.findViewById(R.id.team_location)).setText(location);
        ((TextView) view.findViewById(R.id.team_aka)).setText(aka);
        ((TextView) view.findViewById(R.id.team_rookie_year)).setText(rookieYear);
        ((TextView) view.findViewById(R.id.team_website)).setText(team.website);

    }
}