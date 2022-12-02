package ca.tnoah.frc.scouting.ui.share;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresPermission;
import androidx.appcompat.app.AppCompatActivity;

import java.nio.charset.StandardCharsets;
import java.util.Set;

import ca.tnoah.frc.scouting.Perms;
import ca.tnoah.frc.scouting.R;
import ca.tnoah.frc.scouting.services.bluetooth.BluetoothConstants;
import me.aflak.bluetooth.Bluetooth;
import me.aflak.bluetooth.interfaces.BluetoothCallback;
import me.aflak.bluetooth.interfaces.DeviceCallback;

public class BluetoothActivityBase extends AppCompatActivity {
    private static final String TAG = "==BluetoothActivityBase==";
    private static final boolean DEBUG = true;

    // Intent
    public static final String INTENT_PASSPHRASE = "intent_passphrase";
    private String passphrase;

    // Bluetooth
    protected Bluetooth bluetooth;


    //region Overridden Activity Methods

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initIntent();
        initBluetooth();

        bluetooth = new Bluetooth(this, BluetoothConstants.APP_UUID);
        bluetooth.setCallbackOnUI(this);
        bluetooth.setBluetoothCallback(bluetoothCallback);
        bluetooth.setDeviceCallback(deviceCallback);
    }

    @Override
    protected void onStart() {
        super.onStart();
        bluetooth.onStart();
        if (bluetooth.isEnabled()) {
            // doStuffWhenBluetoothOn() ...
        } else {
            bluetooth.showEnableDialog(this);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        bluetooth.onStop();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        bluetooth.onActivityResult(requestCode, resultCode);
    }

    //endregion Overridden Activity Methods

    private BluetoothCallback bluetoothCallback = new BluetoothCallback() {
        @Override
        public void onBluetoothTurningOn() {
            debug("Bluetooth Turning On");
        }

        @Override
        public void onBluetoothOn() {
            debug("Bluetooth On");
        }

        @Override
        public void onBluetoothTurningOff() {
            debug("Bluetooth Turning Off");
        }

        @Override
        public void onBluetoothOff() {
            debug("Bluetooth Off");
        }

        @Override
        public void onUserDeniedActivation() {
            debug("User Denied Activation");
        }
    };

    @SuppressLint("MissingPermission")
    private DeviceCallback deviceCallback = new DeviceCallback() {

        @Override
        public void onDeviceConnected(BluetoothDevice device) {
            debug("Device Connected: " + device.getName());
        }

        @Override
        public void onDeviceDisconnected(BluetoothDevice device, String message) {
            debug("Device Disconnected: " + device.getName() + " - " + message);
        }

        @Override
        public void onMessage(byte[] message) {
            debug(new String(message, StandardCharsets.UTF_8));
        }

        @Override
        public void onError(int errorCode) {
            debug("Callback Error: " + errorCode);
        }

        @Override
        public void onConnectError(BluetoothDevice device, String message) {
            debug("Connect Error: " + device.getName() + " - " + message);
        }
    };

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

        //deviceNameTV.setText(getString(R.string.bt_device_name_field, getDeviceName()));
        passphraseTV.setText(getString(R.string.bt_passphrase_field, passphrase));
    }

    private void initBluetooth() {

    }

    //endregion Init Methods

    private void debug(String message) {
        if (DEBUG) Log.d(TAG, message);
    }
}