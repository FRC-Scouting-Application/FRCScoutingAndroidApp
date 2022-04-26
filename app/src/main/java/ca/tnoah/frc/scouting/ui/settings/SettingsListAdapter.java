package ca.tnoah.frc.scouting.ui.settings;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;

import java.util.List;

import ca.tnoah.frc.scouting.R;
import ca.tnoah.frc.scouting.models.Event;
import ca.tnoah.frc.scouting.models.Setting;

public class SettingsListAdapter extends ArrayAdapter<Setting> {

    private final Activity context;

    public SettingsListAdapter(Activity context, List<Setting> settings) {
        super(context, R.layout.settings_list_item, settings);

        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View view, @NonNull ViewGroup parent) {
        if (view == null)
            LayoutInflater.from(getContext()).inflate(R.layout.settings_list_item, parent, false);

        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.settings_list_item, parent, false);

        Setting setting = getItem(position);

        ImageView image = rowView.findViewById(R.id.settings_img);
        TextView title = rowView.findViewById(R.id.settings_item);
        TextView value = rowView.findViewById(R.id.settings_value);
        TextView desc = rowView.findViewById(R.id.settings_desc);
        SwitchCompat sw = rowView.findViewById(R.id.settings_switch);

        if (setting.image != -1)
            image.setImageDrawable(context.getDrawable(setting.image));
        else
            image.setImageResource(View.INVISIBLE);

        title.setText(setting.title);

        if (setting.values != null)
            value.setText(setting.values[setting.valuePos]);
        else
            value.setVisibility(View.INVISIBLE);

        if (setting.description != null)
            desc.setText(setting.description);
        else
            desc.setVisibility(View.INVISIBLE);

        switch (setting.type) {
            case TITTLE:
                title.setTextColor(context.getColor(R.color.purple_500));
                break;
            case TOGGLE:
                sw.setVisibility(View.VISIBLE);
                break;
        }


        return rowView;
    }

}
