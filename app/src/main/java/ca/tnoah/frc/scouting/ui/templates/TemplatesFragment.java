package ca.tnoah.frc.scouting.ui.templates;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import ca.tnoah.frc.scouting.R;
import ca.tnoah.frc.scouting.models.dbo.Template;
import ca.tnoah.frc.scouting.services.DatabaseService;
import ca.tnoah.frc.scouting.services.localdb.AppDatabase;
import ca.tnoah.frc.scouting.ui.MainViewModel;
import ca.tnoah.frc.scouting.ui.form.FormActivity;

public class TemplatesFragment extends Fragment {

    private final AppDatabase db = DatabaseService.getInstance().getDB();

    public TemplatesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_templates, container, false);
        MainViewModel viewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);

        view.findViewById(R.id.templateTestNotes).setOnClickListener(this::onClickNotes);
        view.findViewById(R.id.templateTestPit).setOnClickListener(this::onClickPit);

        return view;
    }

    private void onClickNotes(View view) {
        Template notes = db.templatesDAO().get("notes_example", 1);
        openTemplate(notes);
    }

    private void onClickPit(View view) {
        Template pit = db.templatesDAO().get("pit_example", 1);
        openTemplate(pit);
    }

    private void openTemplate(Template template) {
        Intent intent = FormActivity.viewOnly(getActivity(), template);
        startActivity(intent);
    }
}