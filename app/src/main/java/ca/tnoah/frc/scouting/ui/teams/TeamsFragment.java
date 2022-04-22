package ca.tnoah.frc.scouting.ui.teams;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import ca.tnoah.frc.scouting.R;
import ca.tnoah.frc.scouting.services.ApiService;
import ca.tnoah.frc.scouting.services.DatabaseService;
import ca.tnoah.frc.scouting.services.localdb.AppDatabase;

public class TeamsFragment extends Fragment {
    private static final String TAG = "==TeamsFragment==";

    private final AppDatabase db = DatabaseService.getInstance().getDB();

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

        ListView listView = view.findViewById(R.id.teams_list);
        TeamsListAdapter adapter = new TeamsListAdapter(getActivity(), db.teamsDAO().getAll());

        listView.setAdapter(adapter);

        return view;
    }

}