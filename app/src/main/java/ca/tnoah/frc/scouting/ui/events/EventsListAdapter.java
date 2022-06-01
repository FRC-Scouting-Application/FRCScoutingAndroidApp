package ca.tnoah.frc.scouting.ui.events;

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
import ca.tnoah.frc.scouting.models.Event;
import ca.tnoah.frc.scouting.ui.teams.TeamsListAdapter;

public class EventsListAdapter extends ArrayAdapter<Event> {
    private static final String TAG = "==EventsListAdapter==";

    private final Activity context;
    private final List<Event> original;

    private EventFilter filter;

    public EventsListAdapter(Activity context, List<Event> events) {
        super(context, R.layout.list_item, new ArrayList<>(events));

        this.context = context;
        this.original = new ArrayList<>(events);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View view, @NonNull ViewGroup parent) {
        if (view == null)
            LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);

        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.list_item, parent, false);

        Event event = getItem(position);

        ((TextView) rowView.findViewById(R.id.li_title)).setText(event.getName());
        ((TextView) rowView.findViewById(R.id.li_subtitle)).setText(event.getShortLocationString());

        TextView dateTV = rowView.findViewById(R.id.li_date);
        dateTV.setText(event.getDateString());
        dateTV.setVisibility(View.VISIBLE);

        return rowView;
    }

    @Override
    public Filter getFilter() {
        if (filter == null){
            filter = new EventFilter();
        }
        return filter;
    }

    private class EventFilter extends Filter
    {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            String filter = constraint.toString().toLowerCase();
            FilterResults result = new FilterResults();
            if(constraint.toString().length() > 0)
            {
                ArrayList<Event> filteredItems = new ArrayList<>();

                for(int i = 0, l = original.size(); i < l; i++)
                {
                    Event event = original.get(i);
                    if(event.filter(filter))
                        filteredItems.add(event);
                }
                result.count = filteredItems.size();
                result.values = filteredItems;
            }
            else
            {
                result.values = original;
                result.count = original.size();
            }
            return result;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {

            ArrayList<Event> filteredEvents = (ArrayList<Event>)results.values;
            notifyDataSetChanged();
            clear();
            for(int i = 0, l = filteredEvents.size(); i < l; i++)
                add(filteredEvents.get(i));
            notifyDataSetInvalidated();
        }
    }
}
