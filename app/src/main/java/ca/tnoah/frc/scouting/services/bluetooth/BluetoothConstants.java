package ca.tnoah.frc.scouting.services.bluetooth;

import java.util.UUID;

public interface BluetoothConstants {
    // Message types sent from the BluetoothChatService Handler
    int MESSAGE_STATE_CHANGE = 1;
    int MESSAGE_READ = 2;
    int MESSAGE_WRITE = 3;
    int MESSAGE_DEVICE_NAME = 4;
    int MESSAGE_TOAST = 5;

    UUID APP_UUID = UUID.fromString("8d1fc184-c743-4adc-9d06-ca6ec9b4533f");
    String APP_NAME = "ca.tnoah.frc.scouting";

    // Constants that indicate the current connection state
    int STATE_NONE = 0;       // we're doing nothing
    int STATE_LISTEN = 1;     // now listening for incoming connections
    int STATE_CONNECTING = 2; // now initiating an outgoing connection
    int STATE_CONNECTED = 3;  // now connected to a remote device

    String DEVICE_NAME = "device_name";
    String TOAST = "toast";
}