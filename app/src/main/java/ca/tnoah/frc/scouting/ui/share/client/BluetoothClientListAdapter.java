package ca.tnoah.frc.scouting.ui.share.client;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import ca.tnoah.frc.scouting.R;
import ca.tnoah.frc.scouting.ui.share.MyBluetoothDevice;

public class BluetoothClientListAdapter extends ArrayAdapter<BluetoothDevice>{

    private final Activity context;

    public BluetoothClientListAdapter(Activity context, List<BluetoothDevice> devices) {
        super(context, R.layout.connect_list_item, devices);
        this.context = context;
    }

    public BluetoothClientListAdapter(Activity context) {
        this(context, new ArrayList<>());
    }

    @SuppressLint("MissingPermission")
    @NonNull
    @Override
    public View getView(int position, @Nullable View view, @NonNull ViewGroup parent) {
        if (view == null)
            LayoutInflater.from(getContext()).inflate(R.layout.connect_list_item, parent, false);

        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.connect_list_item, parent, false);

        BluetoothDevice device = getItem(position);

        ((TextView) rowView.findViewById(R.id.btListDeviceName)).setText(device.getName());

        return rowView;
    }

    public void updateList(List<BluetoothDevice> devices) {
        notifyDataSetChanged();
        clear();
        addAll(devices);
        notifyDataSetInvalidated();
    }

    public void updateList(Set<BluetoothDevice> devices) {
        updateList(new ArrayList<>(devices));
    }
}