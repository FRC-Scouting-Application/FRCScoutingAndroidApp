package ca.tnoah.frc.scouting.ui.share;

import static ca.tnoah.frc.scouting.services.bluetooth.BluetoothConstants.*;

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
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresPermission;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import ca.tnoah.frc.scouting.Perms;
import ca.tnoah.frc.scouting.R;
import ca.tnoah.frc.scouting.helpers.SerializeEncode;
import ca.tnoah.frc.scouting.services.bluetooth.BluetoothService;
import ca.tnoah.frc.scouting.services.bluetooth.DataPackage;

public class Bluetooth extends AppCompatActivity {
    private static final String TAG = "==BluetoothActivity==";
    private static final boolean DEBUG = true;

    // Intent
    public static final String INTENT_PASSPHRASE = "intent_passphrase";
    private String passphrase;

    private BluetoothAdapter btAdapter;
    private BroadcastReceiver btReceiver;

    // This Activity
    Activity activity = this;

    // Devices
    private final Set<MyBluetoothDevice> discoveredDevices = new HashSet<>();

    // Bluetooth Service
    private BluetoothService mBluetoothService;

    //region Final Methods

    private final ActivityResultLauncher<Intent> enableBTActivityResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Log.d(TAG, "BT ENABLE OK");
                }
            });

    private final ActivityResultLauncher<Intent> makeDiscoverableActivityResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Log.d(TAG, "BT Make discoverable OK");
                }
            });

    private final Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_STATE_CHANGE:
                    if(DEBUG) Log.i(TAG, "MESSAGE_STATE_CHANGE: " + msg.arg1);
                    switch (msg.arg1) {
                        case STATE_CONNECTED:
                            Log.d(TAG, "Connected");
                            break;
                        case STATE_CONNECTING:
                            Log.d(TAG, "Connecting");
                            break;
                        case STATE_LISTEN:
                        case STATE_NONE:
                            Log.d(TAG, "Listen or None");
                            break;
                    }
                    break;
                case MESSAGE_WRITE:
                    Log.d(TAG, "Message Write");
                    break;
                case MESSAGE_READ:
                    Log.d(TAG, "Message Read");
                    break;
                case MESSAGE_DEVICE_NAME:
                    Log.d(TAG, "Message Device Name");
                    break;
                case MESSAGE_TOAST:
                    Toast.makeText(getApplicationContext(), msg.getData().getString(TOAST),
                            Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    //endregion Final Methods

    //region Overridden Activity Methods

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initIntent();
        initBluetooth();

        mBluetoothService = new BluetoothService(this, btAdapter, mHandler);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mBluetoothService != null) {
            if (mBluetoothService.getState() == STATE_NONE) {
                mBluetoothService.start();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (btReceiver != null) unregisterReceiver(btReceiver);
        if (mBluetoothService != null) mBluetoothService.stop();
    }

    //endregion Overridden Activity Methods

    //region Init Methods

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

    private void initBluetooth() {
        // Bluetooth
        BluetoothManager btManager = getSystemService(BluetoothManager.class);
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

                    if (device != null && device.getBondState() != BluetoothDevice.BOND_BONDED) {
                        discoveredDevices.add(new MyBluetoothDevice(device));
                        Log.d(TAG, device.getName() + "|" + device.getAddress());
                    }
                }
            }
        };

        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(btReceiver, filter);
    }

    //endregion Init Methods

    //region Data Transfer

    protected void receiveData(byte[] data) {
        try {
            DataPackage dataPackage = DataPackage.unpackData(data);
            Log.d(TAG, "Events Received" + dataPackage.events.size());
        } catch (SerializeEncode.UnpackException | IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to unpack data", Toast.LENGTH_SHORT).show();
        }
    }

    protected void sendCurrentData() {
        DataPackage dataPackage = DataPackage.createFromDB();
        sendData(dataPackage);
    }

    protected void sendData(DataPackage dataPackage) {
        try {
            byte[] data = dataPackage.packageDataBytes();
            sendData(data);
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Unable to send data!", Toast.LENGTH_SHORT).show();
        }
    }

    protected void sendData(byte[] data) {
        if (mBluetoothService.getState() != STATE_CONNECTED) {
            Toast.makeText(this, "Not Connected!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (data.length > 0) {
            int subArraySize = 1024;

            byte[] bytes = String.valueOf(data.length).getBytes(StandardCharsets.UTF_8);
            mBluetoothService.write(bytes);

            /*for (int i = 0; i < data.length; i+= subArraySize) {
                byte[] temp = Arrays.copyOfRange(data, i, Math.min(data.length, i+subArraySize));
                mBluetoothService.write(temp);
            }*/
        }
    }

    //endregion Data Transfer

    //region Discovery

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

        if(!btAdapter.isEnabled()){
            System.out.println(btAdapter.enable());
        }
        if(btAdapter.isDiscovering()){
            System.out.println(btAdapter.cancelDiscovery());
        }
        if(btAdapter.getScanMode()!=BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE){
            System.out.println(BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE);
        }

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

    //endregion Discovery

    //region Connection

    protected void connect(BluetoothDevice device) {
        mBluetoothService.connect(device);
    }

    //endregion Connection

    //region Get Devices

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

    //endregion Get Devices

    //region ToString Helpers

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

    @SuppressLint("MissingPermission")
    private String getDeviceName() {
        if (!Perms.checkPerm(this, Perms.BLUETOOTH_CONNECT))
            return "unknown";

        return btAdapter.getName();
    }

    //endregion ToString Helpers
}