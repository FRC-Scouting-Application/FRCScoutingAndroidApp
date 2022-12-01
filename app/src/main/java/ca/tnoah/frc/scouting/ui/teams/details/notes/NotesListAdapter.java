package ca.tnoah.frc.scouting.ui.teams.details.notes;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import ca.tnoah.frc.scouting.R;
import ca.tnoah.frc.scouting.models.dbo.Note;
import ca.tnoah.frc.scouting.services.DatabaseService;
import ca.tnoah.frc.scouting.services.localdb.AppDatabase;

public class NotesListAdapter extends ArrayAdapter<Note> {
    private static final String TAG = "==NotesListAdapter==";

    private final Activity context;

    private final String eventKey;
    private final String teamKey;

    public NotesListAdapter(Activity context, String eventKey, String teamKey) {
        super(context, R.layout.list_item, getNotesFromDB(eventKey, teamKey));

        this.context = context;
        this.eventKey = eventKey;
        this.teamKey = teamKey;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View view, @NonNull ViewGroup parent) {
        if (view == null)
            LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);

        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.list_item, parent, false);

        Note note = getItem(position);

        String noteBy = String.format(Locale.CANADA, "%s %s",
                context.getString(R.string.note_by), note.scoutName);

        ((TextView) rowView.findViewById(R.id.li_title)).setText(noteBy);
        ((TextView) rowView.findViewById(R.id.li_subtitle)).setText("");

        return rowView;
    }

    private static List<Note> getNotesFromDB(String eventKey, String teamKey) {
        AppDatabase db = DatabaseService.getInstance().getDB();
        List<Note> notes = db.notesDAO().getAll(eventKey, teamKey);
        if (notes == null)
            notes = new ArrayList<>();
        return notes;
    }

    public void updateList() {
        List<Note> notes = getNotesFromDB(eventKey, teamKey);

        clear();
        addAll(notes);
        notifyDataSetChanged();

        Log.d(TAG, "List updated!");
    }
}
