package ca.tnoah.frc.scouting.ui.teams.details.scout;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ca.tnoah.frc.scouting.R;
import ca.tnoah.frc.scouting.ui.teams.details.TeamDetailActivity;
import ca.tnoah.frc.scouting.ui.teams.details.TeamViewModel;

public class PitScoutFragment extends Fragment {

    public PitScoutFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pit_scout, container, false);

        TeamViewModel viewModel = new ViewModelProvider(requireActivity()).get(TeamViewModel.class);
        viewModel.setMainPage(false);

        return view;
    }
}