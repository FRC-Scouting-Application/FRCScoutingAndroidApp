package ca.tnoah.frc.scouting.ui.teams.details.notes;

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
import ca.tnoah.frc.scouting.ui.teams.details.TeamViewModel;

public class NotesFragment extends Fragment {
    private static final String TAG = "==NotesFragment==";
    private final AppDatabase db = DatabaseService.getInstance().getDB();

    private ListView listView;
    private NotesListAdapter adapter;

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

        Log.d(TAG, "Load team");

        adapter = new NotesListAdapter(getActivity(), "2022on325", team.id);
        listView.setAdapter(adapter);
    }

    private void onNoteClick(AdapterView<?> parent, View view, int position, long id) {

    }

    private void addClicked(View view) {
        Log.d(TAG, "Add Clicked");
    }

}