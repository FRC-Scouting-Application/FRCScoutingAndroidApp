package ca.tnoah.frc.scouting.ui.share;

import android.bluetooth.BluetoothDevice;
import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresPermission;
import androidx.annotation.StringRes;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import ca.tnoah.frc.scouting.R;

public class MyBluetoothDevice implements Comparable<MyBluetoothDevice> {
    private final BluetoothDevice device;
    private final String deviceName;

    private Status status = Status.NOT_CONNECTED;

    private enum Status {
        NOT_CONNECTED ,CONNECTING, SYNCING, FINISHED, FAILED
    }

    @RequiresPermission(value = "android.permission.BLUETOOTH_CONNECT")
    public MyBluetoothDevice(BluetoothDevice device) {
        this.device = device;

        String deviceName = device.getName();
        this.deviceName = deviceName != null ? deviceName : "Hidden";
    }

    public MyBluetoothDevice(String deviceName) {
        this.device = null;
        this.deviceName = deviceName != null ? deviceName : "Hidden";
    }

    @Nullable
    public BluetoothDevice getDevice() {
        return this.device;
    }

    public String getDeviceName() {
        return this.deviceName;
    }

    public Status getStatus() {
        return this.status;
    }

    @StringRes
    public int getStatusStringRes() {
        return getStatusStringRes(this.status);
    }

    @StringRes
    public static int getStatusStringRes(Status status) {
        switch (status) {
            case CONNECTING:
                return R.string.bt_sync_connecting;
            case SYNCING:
                return R.string.bt_sync_syncing;
            case FINISHED:
                return R.string.bt_sync_finished;
            case FAILED:
                return R.string.bt_sync_fail;
            case NOT_CONNECTED:
            default:
                return R.string.bt_not_connected;
        }
    }

    public static String getStatusString(@NotNull Context context, Status status) {
        return context.getString(getStatusStringRes(status));
    }

    @Override
    public String toString() {
        return "MyBluetoothDevice{" +
                "device=" + device +
                ", deviceName='" + deviceName + '\'' +
                ", status=" + status +
                '}';
    }

    @Override
    public int compareTo(MyBluetoothDevice o) {
        if (deviceName == null || o.deviceName == null) return 0;
        return deviceName.compareTo(o.deviceName);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MyBluetoothDevice device1 = (MyBluetoothDevice) o;
        return Objects.equals(device, device1.device) && Objects.equals(deviceName, device1.deviceName) && status == device1.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(device, deviceName, status);
    }
}