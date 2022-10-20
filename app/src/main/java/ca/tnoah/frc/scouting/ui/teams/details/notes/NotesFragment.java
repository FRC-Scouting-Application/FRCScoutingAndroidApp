package ca.tnoah.frc.scouting.ui.teams.details.notes;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import ca.tnoah.frc.scouting.R;
import ca.tnoah.frc.scouting.models.dbo.Team;
import ca.tnoah.frc.scouting.services.DatabaseService;
import ca.tnoah.frc.scouting.services.localdb.AppDatabase;
import ca.tnoah.frc.scouting.ui.form.FormActivity;
import ca.tnoah.frc.scouting.ui.teams.details.TeamViewModel;

public class NotesFragment extends Fragment {
    private static final String TAG = "==NotesFragment==";
    private static final String TEMPLATE_FILE = "templates/template_notes.json";

    private final AppDatabase db = DatabaseService.getInstance().getDB();

    private ListView listView;
    private NotesListAdapter adapter;

    private Team team;

    // TODO: REMOVE WHEN EVENT SELECTION IS IMPLEMENTED
    private static final String TEST_EVENT = "2022on325";

    public NotesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notes, container, false);

        TeamViewModel viewModel = new ViewModelProvider(requireActivity()).get(TeamViewModel.class);
        viewModel.getTeam().observe(getViewLifecycleOwner(), this::loadTeam);

        listView = view.findViewById(R.id.notes_list);
        listView.setOnItemClickListener(this::onNoteClick);

        if (adapter != null)
            adapter.updateList();

        view.findViewById(R.id.notes_add).setOnClickListener(this::addClicked);

        return view;
    }

    private void loadTeam(Team team) {
        if (team == null) {
            Log.d(TAG, "Team null");
            return;
        }

        this.team = team;
        Log.d(TAG, "Load team");

        adapter = new NotesListAdapter(getActivity(), TEST_EVENT, team.id);
        listView.setAdapter(adapter);
    }

    private void onNoteClick(AdapterView<?> parent, View view, int position, long id) {

    }

    private void addClicked(View view) {
        Intent switchToForm = FormActivity.createIntentFile(getActivity(),
                TEMPLATE_FILE, team.id, TEST_EVENT);
        startActivity(switchToForm);
    }

}