package ca.tnoah.frc.scouting.ui.share;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ca.tnoah.frc.scouting.R;
import ca.tnoah.frc.scouting.ui.MainViewModel;

public class ShareFragment extends Fragment {

    public ShareFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_share, container, false);

        return view;
    }
}