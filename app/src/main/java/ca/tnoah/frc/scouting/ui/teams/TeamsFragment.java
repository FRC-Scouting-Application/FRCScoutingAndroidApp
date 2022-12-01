package ca.tnoah.frc.scouting.ui.teams;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import ca.tnoah.frc.scouting.R;
import ca.tnoah.frc.scouting.services.ApiService;
import ca.tnoah.frc.scouting.services.DatabaseService;
import ca.tnoah.frc.scouting.services.localdb.AppDatabase;
import ca.tnoah.frc.scouting.ui.MainViewModel;
import ca.tnoah.frc.scouting.ui.teams.details.TeamDetailActivity;

public class TeamsFragment extends Fragment {
    private static final String TAG = "==TeamsFragment==";

    private final AppDatabase db = DatabaseService.getInstance().getDB();
    private final ApiService api = ApiService.getInstance();

    private TeamsListAdapter adapter;

    public TeamsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_teams, container, false);

        MainViewModel viewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        viewModel.getSearch().observe(getViewLifecycleOwner(), this::onSearch);

        ListView listView = view.findViewById(R.id.teams_list);
        adapter = new TeamsListAdapter(getActivity(), db.teamsDAO().getAll());

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this::onTeamClick);

        return view;
    }

    private void onTeamClick(AdapterView<?> parent, View view, int position, long id) {
        String teamKey = adapter.getItem(position).id;

        Intent switchToDetails = new Intent(getActivity(), TeamDetailActivity.class);
        switchToDetails.putExtra(TeamDetailActivity.TEAM_KEY, teamKey);
        startActivity(switchToDetails);
    }

    private void onSearch(String search) {
        if (adapter != null)
            adapter.getFilter().filter(search);
    }
}