package ca.tnoah.frc.scouting.ui.teams.details.notes;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ca.tnoah.frc.scouting.R;

public class NoteEdit extends Fragment {
    private static final String TAG = "==NoteEditFragment==";

    public NoteEdit() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_note_edit, container, false);
        return view;
    }
}