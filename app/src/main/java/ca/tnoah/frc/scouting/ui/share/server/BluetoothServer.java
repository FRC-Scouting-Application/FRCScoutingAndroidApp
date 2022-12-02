package ca.tnoah.frc.scouting.ui.share.server;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import ca.tnoah.frc.scouting.R;
import ca.tnoah.frc.scouting.ui.share.BluetoothActivityBase;

public class BluetoothServer extends BluetoothActivityBase {
    private static final String TAG = "==BluetoothServer==";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_server);
        initHeader(true);

        makeDiscoverable();
    }

    private void makeDiscoverable() {
        Intent discoverable = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverable.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
        makeDiscoverableActivityResult.launch(discoverable);
    }

    private final ActivityResultLauncher<Intent> makeDiscoverableActivityResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Log.d(TAG, "BT Make discoverable OK");
                }
            });
}