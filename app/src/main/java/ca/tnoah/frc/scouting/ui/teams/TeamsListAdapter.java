package ca.tnoah.frc.scouting.ui.teams;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import ca.tnoah.frc.scouting.R;
import ca.tnoah.frc.scouting.models.dbo.Team;

public class TeamsListAdapter extends ArrayAdapter<Team> {
    private static final String TAG = "==TeamsListAdapter==";

    private final Activity context;
    private final List<Team> original;

    private TeamFilter filter;

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

    @Override
    public Filter getFilter() {
        if (filter == null){
            filter  = new TeamFilter();
        }
        return filter;
    }

    private class TeamFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            String filter = constraint.toString().toLowerCase();
            FilterResults result = new FilterResults();
            if (constraint.toString().length() > 0) {
                ArrayList<Team> filteredItems = new ArrayList<>();

                for (int i = 0, l = original.size(); i < l; i++) {
                    Team team = original.get(i);
                    if (team.filter(filter))
                        filteredItems.add(team);
                }
                result.count = filteredItems.size();
                result.values = filteredItems;
            } else {
                result.values = original;
                result.count = original.size();
            }
            return result;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {

            ArrayList<Team> filteredTeams = (ArrayList<Team>) results.values;
            notifyDataSetChanged();
            clear();
            for (int i = 0, l = filteredTeams.size(); i < l; i++)
                add(filteredTeams.get(i));
            notifyDataSetInvalidated();
        }
    }
}
