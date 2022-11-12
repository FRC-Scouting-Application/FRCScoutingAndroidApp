package ca.tnoah.frc.scouting.ui.sync;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import ca.tnoah.frc.scouting.R;
import ca.tnoah.frc.scouting.helpers.DateHelper;
import ca.tnoah.frc.scouting.services.sync.SyncService;

public class SyncFragment extends Fragment {

    private static final DateFormat format = DateFormat.getDateInstance(DateFormat.DEFAULT, Locale.CANADA);
    private final SyncService sync = SyncService.getInstance();
    private TextView lastSync;

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
        view.findViewById(R.id.sync_now).setOnClickListener(this::onSyncNowClicked);

        lastSync = view.findViewById(R.id.lastSync);
        Date lastSyncTime = sync.getLastSync();
        if (lastSyncTime == null)
            lastSync.setText(R.string.never_synced);
        else
            setSyncText(lastSyncTime);

        //sync.setOnSyncDoneListener(this::onSyncDone);

        return view;
    }

    private void onSyncNowClicked(View view) {
        if (!sync.isSyncing())
            sync.sync();
    }

    private void onSyncDone(Date timeUpdated) {
        setSyncText(timeUpdated);
    }

    private void setSyncText(Date date) {
        String fmtDateTime = DateHelper.simpleDate(date);
        lastSync.setText(getString(R.string.last_sync, fmtDateTime));
    }
}