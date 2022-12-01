package ca.tnoah.frc.scouting.ui.share.client;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.nio.charset.StandardCharsets;
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
        listView.setOnItemClickListener(this::onItemClick);

        findViewById(R.id.sendMsg).setOnClickListener((v) -> {
            sendData("Hello World!".getBytes(StandardCharsets.UTF_8));
        });

        discover();
    }

    private void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        BluetoothDevice device = adapter.getItem(position).getDevice();

        if (device == null) {
            Toast toast = Toast.makeText(this, "Failed to get device!", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }

        connect(device);
    }
}