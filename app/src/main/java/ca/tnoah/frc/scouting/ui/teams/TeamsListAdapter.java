package ca.tnoah.frc.scouting.ui.teams;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import ca.tnoah.frc.scouting.R;
import ca.tnoah.frc.scouting.helpers.BitmapHelper;
import ca.tnoah.frc.scouting.models.Team;

public class TeamsListAdapter extends ArrayAdapter<Team> {
    private static final String TAG = "==TeamsListAdapter==";

    private final Activity context;
    private final List<Team> original;

    public TeamsListAdapter(Activity context, List<Team> teams) {
        super(context, R.layout.list_item, new ArrayList<>(teams));

        this.context = context;
        this.original = new ArrayList<>(teams);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View view, @NonNull ViewGroup parent) {
        if (view == null)
            LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);

        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.list_item, parent, false);

        Team team = getItem(position);

        ((TextView) rowView.findViewById(R.id.li_title)).setText(String.valueOf(team.teamNumber));
        ((TextView) rowView.findViewById(R.id.li_subtitle)).setText(team.nickname);

        return rowView;
    }
}
