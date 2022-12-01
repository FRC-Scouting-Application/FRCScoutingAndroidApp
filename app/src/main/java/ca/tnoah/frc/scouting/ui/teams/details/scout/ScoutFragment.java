package ca.tnoah.frc.scouting.ui.teams.details.scout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import ca.tnoah.frc.scouting.R;
import ca.tnoah.frc.scouting.models.dbo.Scout;
import ca.tnoah.frc.scouting.models.dbo.Team;
import ca.tnoah.frc.scouting.models.dbo.Template;
import ca.tnoah.frc.scouting.services.DatabaseService;
import ca.tnoah.frc.scouting.services.localdb.AppDatabase;
import ca.tnoah.frc.scouting.ui.form.FormActivity;
import ca.tnoah.frc.scouting.ui.teams.details.TeamViewModel;

public abstract class ScoutFragment extends Fragment {
    private static final String TAG = "==ScoutFragment==";

    // TODO: REMOVE WHEN EVENT SELECTION IS IMPLEMENTED
    private static final String TEST_EVENT = "2022on325";
    private final AppDatabase db = DatabaseService.getInstance().getDB();

    private Team team;
    private ListView listView;
    private ScoutListAdapter adapter;

    private final String type;

    public ScoutFragment() {
        this("notes");
    }

    public ScoutFragment(String type) {
        this.type = type;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scout, container, false);

        TeamViewModel viewModel = new ViewModelProvider(requireActivity()).get(TeamViewModel.class);
        viewModel.getTeam().observe(getViewLifecycleOwner(), this::loadTeam);

        listView = view.findViewById(R.id.scoutList);
        listView.setOnItemClickListener(this::onItemClicked);

        if (adapter != null)
            adapter.updateList();

        view.findViewById(R.id.scoutAdd).setOnClickListener(this::onAddClicked);

        return view;
    }

    private void loadTeam(Team team) {
        if (team == null) {
            Log.d(TAG, "Team null");
            return;
        }

        this.team = team;
        Log.d(TAG, "Load team");

        adapter = new ScoutListAdapter(getActivity(), TEST_EVENT, team.id, type);
        listView.setAdapter(adapter);
    }

    private void onItemClicked(AdapterView<?> parent, View view, int position, long id) {
        Scout scout = (Scout) parent.getItemAtPosition(position);

        if (scout == null) {
            Toast toast = Toast.makeText(getContext(), R.string.failed_to_open_scout, Toast.LENGTH_SHORT);
            toast.show();
            return;
        }

        Intent switchToForm = FormActivity.editFromExisting(getContext(), scout);
        startActivity(switchToForm);
    }

    private void onAddClicked(View view) {
        Template template = db.templatesDAO().getDefault(type);

        if (template == null) {
            Toast toast = Toast.makeText(getContext(), "Template Not Found!", Toast.LENGTH_LONG);
            toast.show();
            return;
        }

        Intent switchToFrom = FormActivity.editFromNew(getActivity(), team.id, TEST_EVENT, template);
        startActivity(switchToFrom);
    }
}