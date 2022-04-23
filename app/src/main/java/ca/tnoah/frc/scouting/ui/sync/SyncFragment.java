package ca.tnoah.frc.scouting.ui.sync;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import ca.tnoah.frc.scouting.R;
import ca.tnoah.frc.scouting.services.sync.SyncService;
import ca.tnoah.frc.scouting.ui.MainViewModel;

public class SyncFragment extends Fragment {

    public SyncFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sync, container, false);

        MainViewModel viewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        viewModel.setPage("sync");

        view.findViewById(R.id.sync_now).setOnClickListener(this::onSyncNowClicked);

        return view;
    }

    private void onSyncNowClicked(View view) {
        SyncService.getInstance().Sync();
    }
}