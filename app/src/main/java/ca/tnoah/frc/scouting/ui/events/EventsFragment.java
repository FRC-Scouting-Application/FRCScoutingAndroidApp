package ca.tnoah.frc.scouting.ui.events;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import ca.tnoah.frc.scouting.R;
import ca.tnoah.frc.scouting.services.ApiService;
import ca.tnoah.frc.scouting.services.DatabaseService;
import ca.tnoah.frc.scouting.services.localdb.AppDatabase;
import ca.tnoah.frc.scouting.ui.teams.TeamsListAdapter;

public class EventsFragment extends Fragment {
    private static final String TAG = "==EventsFragment==";

    private final AppDatabase db = DatabaseService.getInstance().getDB();
    private final ApiService api = ApiService.getInstance();

    private EventsListAdapter adapter;

    public EventsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_events, container, false);

        ListView listView = view.findViewById(R.id.events_list);
        adapter = new EventsListAdapter(getActivity(), db.eventsDAO().getAll());

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this::onEventClick);

        return view;
    }

    private void onEventClick(AdapterView<?> parent, View view, int position, long id) {
        String eventKey = adapter.getItem(position).key;

        Intent switchToDetails = new Intent(getActivity(), EventDetailActivity.class);
        switchToDetails.putExtra(EventDetailActivity.EVENT_KEY, eventKey);
        startActivity(switchToDetails);
    }
}