package ca.tnoah.frc.scouting.ui.events;

import android.app.Activity;
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
import ca.tnoah.frc.scouting.models.Event;

public class EventsListAdapter extends ArrayAdapter<Event> {
    private static final String TAG = "==EventsListAdapter==";

    private final Activity context;
    private final List<Event> original;

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
}
