package ca.tnoah.frc.scouting.ui.share.client;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import ca.tnoah.frc.scouting.R;
import ca.tnoah.frc.scouting.ui.share.BluetoothActivityBase;
import me.aflak.bluetooth.interfaces.DiscoveryCallback;

public class BluetoothClient extends BluetoothActivityBase {
    private static final String TAG = "==BluetoothClient==";
    private static final boolean DEBUG = true;

    private BluetoothClientListAdapter adapter;
    private List<BluetoothDevice> devices;

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_client);
        initHeader(false);

        findViewById(R.id.btConnectToDevice).setOnClickListener((v) -> {
            adapter.updateList(bluetooth.getPairedDevices());
        });


        ListView listView = (ListView) findViewById(R.id.btConnectDeviceList);
        adapter = new BluetoothClientListAdapter(this);

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this::onItemClick);

        findViewById(R.id.sendMsg).setOnClickListener((v) -> {
            bluetooth.send("Hello World!".getBytes(StandardCharsets.UTF_8));
        });

        bluetooth.setDiscoveryCallback(discoveryCallback);
    }

    @Override
    protected void onStart() {
        super.onStart();
        bluetooth.startScanning();
    }

    private void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        BluetoothDevice device = adapter.getItem(position);

        if (device == null) {
            Toast toast = Toast.makeText(this, "Failed to get device!", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }

        if (bluetooth.getPairedDevices().contains(device))
            bluetooth.connectToDevice(device);
        else
            bluetooth.pair(device);

        //bluetooth.connectToDevice(device);
        //bluetooth.connectToDeviceWithPortTrick(device);
    }

    @SuppressLint("MissingPermission")
    private DiscoveryCallback discoveryCallback = new DiscoveryCallback() {
        @Override
        public void onDiscoveryStarted() {
            debug("Discovery Started");
            devices = new ArrayList<>();
            devices.addAll(bluetooth.getPairedDevices());
        }

        @Override
        public void onDiscoveryFinished() {
            debug("Discovery Finished");
        }

        @Override
        public void onDeviceFound(BluetoothDevice device) {
            debug("Device Found: " + device.getName());
            devices.add(device);
            adapter.updateList(devices);
        }

        @Override
        public void onDevicePaired(BluetoothDevice device) {
            debug("Device Paired: " + device.getName());
            bluetooth.connectToDevice(device);
        }

        @Override
        public void onDeviceUnpaired(BluetoothDevice device) {
            debug("Device Unpaired: " + device.getName());
        }

        @Override
        public void onError(int errorCode) {
            debug("On Error: " + errorCode);
        }
    };

    private void debug(String msg) {
        if (DEBUG) Log.d(TAG, msg);
    }
}