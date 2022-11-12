package ca.tnoah.frc.scouting.ui.teams.details.scout;

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

import ca.tnoah.frc.scouting.R;
import ca.tnoah.frc.scouting.models.dbo.Scout;
import ca.tnoah.frc.scouting.services.DatabaseService;
import ca.tnoah.frc.scouting.services.localdb.AppDatabase;

public class ScoutListAdapter extends ArrayAdapter<Scout> {
    private static final String TAG = "==ScoutListAdapter==";

    private final Activity context;
    private final String eventKey;
    private final String teamKey;
    private final String type;

    public ScoutListAdapter(Activity context, String eventKey, String teamKey, String type) {
        super(context, R.layout.list_item, getScoutsFromDB(eventKey, teamKey, type));

        this.context = context;
        this.eventKey = eventKey;
        this.teamKey = teamKey;
        this.type = type;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View view, @NonNull ViewGroup parent) {
        if (view == null)
            LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);

        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.list_item, parent, false);

        Scout scout = getItem(position);
        String title;
        String subtitle = "Date"; // TODO: FIX DATES


        switch (type) {
            case "pit":
                title = "Pit Scout by " + scout.getScoutName();
                break;
            case "match":
                title = "Match " + scout.getMatchNumber() + " by " + scout.getScoutName();
                break;
            case "notes":
                title = "Note by " + scout.getScoutName();
                break;
            default:
                title = type + " by " + scout.getScoutName();
                break;
        }


        ((TextView) rowView.findViewById(R.id.li_title)).setText(title);
        ((TextView) rowView.findViewById(R.id.li_subtitle)).setText(subtitle);

        return rowView;
    }

    private static List<Scout> getScoutsFromDB(String eventKey, String teamKey, String type) {
        AppDatabase db = DatabaseService.getInstance().getDB();
        List<Scout> scouts = db.scoutsDAO().getAll(type, eventKey, teamKey);

        if (scouts == null)
            scouts = new ArrayList<>();


        return scouts;
    }

    public void updateList() {
        List<Scout> scouts = getScoutsFromDB(eventKey, teamKey, type);

        clear();
        addAll(scouts);
        notifyDataSetChanged();

        Log.d(TAG, "List updated!");
    }
}