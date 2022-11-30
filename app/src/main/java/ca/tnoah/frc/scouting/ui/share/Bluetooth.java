package ca.tnoah.frc.scouting.ui.share;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresPermission;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import ca.tnoah.frc.scouting.Perms;
import ca.tnoah.frc.scouting.R;

public class Bluetooth extends AppCompatActivity {
    private static final String TAG = "==BluetoothActivity==";

    public static final UUID APP_UUID = UUID.fromString("8d1fc184-c743-4adc-9d06-ca6ec9b4533f");
    public static final String APP_NAME = "ca.tnoah.frc.scouting";

    // Intent
    public static final String INTENT_PASSPHRASE = "intent_passphrase";
    private String passphrase;

    // Bluetooth
    private BluetoothManager btManager;
    private BluetoothAdapter btAdapter;
    private BroadcastReceiver btReceiver;

    // Activity Result Launchers
    private ActivityResultLauncher<Intent> enableBTActivityResult;
    private ActivityResultLauncher<Intent> makeDiscoverableActivityResult;

    // This Activity
    Activity activity = this;

    // Devices
    private final Set<MyBluetoothDevice> discoveredDevices = new HashSet<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createActivityResultLaunchers();
        initIntent();
        initBluetooth();
    }

    @SuppressLint("MissingPermission")
    protected void discover() {
        if (activity == null) return;
        if (btAdapter == null) return;
        if (btReceiver == null) return;

        if (!Perms.checkPerm(this, Perms.BLUETOOTH_SCAN)) {
            Log.d(TAG, "Missing Perm: BLUETOOTH_SCAN");
            return;
        }

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (!isGpsEnabled)
            Perms.checkPerm(this, Perms.ACCESS_COARSE_LOCATION);

        boolean a = btAdapter.startDiscovery();
        Log.d(TAG, "Start Discovery: " + a);
        Log.d(TAG, "Is Discovering: " + btAdapter.isDiscovering());
    }

    protected void makeDiscoverable() {
        if (btAdapter == null) return;
        if (btReceiver == null) return;
        if (makeDiscoverableActivityResult == null) return;

        Intent discoverable = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverable.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
        discoverable.putExtra(INTENT_PASSPHRASE, passphrase);
        makeDiscoverableActivityResult.launch(discoverable);
    }

    @SuppressLint("MissingPermission")
    protected Set<MyBluetoothDevice> getConnectedDevices() {
        if (!Perms.checkPerm(this, Perms.BLUETOOTH_CONNECT))
            return new HashSet<>();

        Set<BluetoothDevice> devices = btAdapter.getBondedDevices();
        Set<MyBluetoothDevice> myDevices = new HashSet<>();

        for (BluetoothDevice device : devices)
            myDevices.add(new MyBluetoothDevice(device));

        return myDevices;
    }

    protected Set<MyBluetoothDevice> getDiscoveredDevices() {
        return this.discoveredDevices;
    }

    protected Set<MyBluetoothDevice> getAllDevices() {
        Set<MyBluetoothDevice> devices = new HashSet<>();
        devices.addAll(getConnectedDevices());
        devices.addAll(getDiscoveredDevices());
        return devices;
    }

    @SuppressLint("MissingPermission")
    public String bluetoothDevicesToString(Set<BluetoothDevice> devices) {
        if (devices == null || devices.size() == 0) return "";

        if (!Perms.checkPerm(this, Perms.BLUETOOTH_CONNECT))
            return "missing perms";

        StringBuilder out = new StringBuilder();
        for (BluetoothDevice device : devices) {
            out.append(bluetoothDeviceToString(device)).append("\n");
        }

        return out.toString();
    }

    public String myBluetoothDevicesToString(Set<MyBluetoothDevice> devices) {
        if (devices == null || devices.size() == 0) return "";

        if (!Perms.checkPerm(this, Perms.BLUETOOTH_CONNECT))
            return "missing perms";

        StringBuilder out = new StringBuilder();
        for (MyBluetoothDevice device : devices) {
            out.append(device.toString()).append("\n");
        }

        return out.toString();
    }

    @RequiresPermission(value = "android.permission.BLUETOOTH_CONNECT")
    public String bluetoothDeviceToString(BluetoothDevice device) {
        if (device == null) return "device null";

        return device.getName() + "|" + device.getAddress();
    }

    private void initIntent() {
        Intent intent = getIntent();

        passphrase = intent.getStringExtra(INTENT_PASSPHRASE);

        if (passphrase == null) {
            Toast toast = Toast.makeText(this, R.string.bt_invalid_passphrase, Toast.LENGTH_SHORT);
            toast.show();

            finish();
        }
    }

    protected void initHeader(boolean server) {
        TextView titleTV = findViewById(R.id.btTitleTV);
        TextView subtitleTV = findViewById(R.id.btSubtitleTV);
        TextView deviceNameTV = findViewById(R.id.btDeviceNameFieldTV);
        TextView passphraseTV = findViewById(R.id.btPassphraseFieldTV);

        if (server) {
            titleTV.setText(R.string.bt_server);
            subtitleTV.setText(R.string.bt_server_subtitle);
        } else {
            titleTV.setText(R.string.bt_client);
            subtitleTV.setText(R.string.bt_client_subtitle);
        }

        deviceNameTV.setText(getString(R.string.bt_device_name_field, getDeviceName()));
        passphraseTV.setText(getString(R.string.bt_passphrase_field, passphrase));
    }

    @SuppressLint("MissingPermission")
    private String getDeviceName() {
        if (!Perms.checkPerm(this, Perms.BLUETOOTH_CONNECT))
            return "unknown";

        return btAdapter.getName();
    }

    private void initBluetooth() {
        btManager = getSystemService(BluetoothManager.class);
        btAdapter = btManager.getAdapter();

        if (btAdapter == null) {
            Toast toast = Toast.makeText(this, "Bluetooth Unavaliable on this device!", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }

        if (!btAdapter.isEnabled()) {
            Intent enableBT = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            enableBTActivityResult.launch(enableBT);
        }

        btReceiver = new BroadcastReceiver() {
            @SuppressLint("MissingPermission")
            @Override
            public void onReceive(Context context, Intent intent) {
                String pass = intent.getStringExtra(INTENT_PASSPHRASE);
                Log.d(TAG, "Test: " + pass);

                String action = intent.getAction();
                if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    if (!Perms.checkPerm(activity, Perms.BLUETOOTH_CONNECT)) {
                        Log.d(TAG, "Missing Perm: BLUETOOTH_CONNECT");
                        return;
                    }
                    discoveredDevices.add(new MyBluetoothDevice(device));
                    Log.d(TAG, device.getName() + "|" + device.getAddress());
                }
            }
        };

        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(btReceiver, filter);
    }

    private void createActivityResultLaunchers() {
        enableBTActivityResult = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Log.d(TAG, "BT ENABLE OK");
                    }
                });

        makeDiscoverableActivityResult = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Log.d(TAG, "BT Make discoverable OK");
                    }
                });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (btReceiver != null)
            unregisterReceiver(btReceiver);
    }

    private class AcceptThread extends Thread {
        private static final String TAG = "==AcceptThread==";

        private final BluetoothServerSocket serverSocket;

        @SuppressLint("MissingPermission")
        public AcceptThread() {
            BluetoothServerSocket tmp = null;

            if (Perms.checkPerm(activity, Perms.BLUETOOTH_CONNECT)) {
                try {
                    tmp = btAdapter.listenUsingInsecureRfcommWithServiceRecord(Bluetooth.APP_NAME, Bluetooth.APP_UUID);
                } catch (IOException e) {
                    Log.e(TAG, "Socket's listen() method failed", e);
                }
            } else {
                Log.e(TAG, "Missing Perm BLUETOOTH_CONNECT");
            }

            serverSocket = tmp;
        }

        @Override
        public void run() {
            BluetoothSocket socket = null;

            while (true) {
                try {
                    socket = serverSocket.accept();
                } catch (IOException e) {
                    Log.e(TAG, "Socket's accept() method failed", e);
                    break;
                }

                if (socket != null) {
                    // TODO: Manage Connected Socket
                    cancel();
                    break;
                }
            }
        }

        public void cancel() {
            try {
                serverSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "Could not close the connect socket", e);
            }
        }
    }

    private class ConnectThread extends Thread {
        private static final String TAG = "==ConnectThread==";

        private final BluetoothSocket socket;
        private final BluetoothDevice device;

        @SuppressLint("MissingPermission")
        public ConnectThread(BluetoothDevice device) {
            BluetoothSocket tmp = null;
            this.device = device;

            if (Perms.checkPerm(activity, Perms.BLUETOOTH_CONNECT)) {
                try {
                    tmp = device.createRfcommSocketToServiceRecord(Bluetooth.APP_UUID);
                } catch (IOException e) {
                    Log.e(TAG, "Socket's create() method failed", e);
                }
            } else {
                Log.e(TAG, "Missing Perm BLUETOOTH_CONNECT");
            }

            socket = tmp;
        }

        @SuppressLint("MissingPermission")
        @Override
        public void run() {
            if (Perms.checkPerm(activity, Perms.BLUETOOTH_CONNECT)) {
                Log.d(TAG, "Missing Perm: BLUETOOTH_CONNECT");
                return;
            }

            btAdapter.cancelDiscovery();

            try {
                socket.connect();
            } catch (IOException connectException) {
                // Unable to connect; close the socket and return.
                try {
                    socket.close();
                } catch (IOException closeException) {
                    Log.e(TAG, "Could not close the client socket", closeException);
                }
                return;
            }

            // TODO: Manage Connected Socket
        }
    }
}