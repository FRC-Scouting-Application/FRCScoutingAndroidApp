package ca.tnoah.frc.scouting.ui.share.client;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import ca.tnoah.frc.scouting.Perms;
import ca.tnoah.frc.scouting.R;
import ca.tnoah.frc.scouting.ui.share.Bluetooth;
import ca.tnoah.frc.scouting.ui.share.MyBluetoothDevice;

public class BluetoothClient extends Bluetooth {
    private static final String TAG = "==BluetoothClient==";

    private BluetoothClientListAdapter adapter;

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_client);
        initHeader(false);

        findViewById(R.id.btConnectToDevice).setOnClickListener((v) -> {
            Log.d(TAG, "Connected: " + myBluetoothDevicesToString(getConnectedDevices()));
            Log.d(TAG, "Discovered: " + myBluetoothDevicesToString(getDiscoveredDevices()));

            adapter.updateList(getAllDevices());
        });


        ListView listView = (ListView) findViewById(R.id.btConnectDeviceList);
        adapter = new BluetoothClientListAdapter(this);
        listView.setAdapter(adapter);

        discover();
    }

}