package ca.tnoah.frc.scouting.ui.events;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import ca.tnoah.frc.scouting.R;
import ca.tnoah.frc.scouting.models.Event;
import ca.tnoah.frc.scouting.models.Team;

public class EventsListAdapter extends ArrayAdapter<Event> {
    private static final String TAG = "==EventsListAdapter==";

    private final Activity context;
    private final List<Event> original;

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd", Locale.CANADA);
    private final SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy", Locale.CANADA);

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

        String name = event.shortName;
        if (name == null || name.isEmpty())
            name = event.name;

        String subtitle = String.format(Locale.CANADA, "%s, %s, %s",
                event.city, event.stateProv, event.country);

        String date = String.format(Locale.CANADA, "%s to %s, %s",
                dateFormat.format(event.startDate), dateFormat.format(event.endDate),
                yearFormat.format(event.endDate));

        ((TextView) rowView.findViewById(R.id.li_title)).setText(name);
        ((TextView) rowView.findViewById(R.id.li_subtitle)).setText(subtitle);

        TextView dateTV = rowView.findViewById(R.id.li_date);
        dateTV.setText(date);
        dateTV.setVisibility(View.VISIBLE);

        return rowView;
    }
}
