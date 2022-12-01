package ca.tnoah.frc.scouting.ui.share.server;

import android.os.Bundle;

import ca.tnoah.frc.scouting.R;
import ca.tnoah.frc.scouting.ui.share.Bluetooth;

public class BluetoothServer extends Bluetooth {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_server);
        initHeader(true);

        makeDiscoverable();
    }
}