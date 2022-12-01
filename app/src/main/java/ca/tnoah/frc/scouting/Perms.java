package ca.tnoah.frc.scouting;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@RequiresApi(api = Build.VERSION_CODES.S)
public class Perms {

    public static final Perm INTERNET = new Perm(Manifest.permission.INTERNET, 100);
    public static final Perm BLUETOOTH = new Perm(Manifest.permission.BLUETOOTH, 101);
    public static final Perm BLUETOOTH_ADMIN = new Perm(Manifest.permission.BLUETOOTH_ADMIN, 102);
    public static final Perm ACCESS_COARSE_LOCATION = new Perm(Manifest.permission.ACCESS_COARSE_LOCATION, 106);

    public static final Perm BLUETOOTH_SCAN = new Perm(Manifest.permission.BLUETOOTH_SCAN, 103);
    public static final Perm BLUETOOTH_ADVERTISE = new Perm(Manifest.permission.BLUETOOTH_ADVERTISE, 104);
    public static final Perm BLUETOOTH_CONNECT = new Perm(Manifest.permission.BLUETOOTH_CONNECT, 105);

    public static final Perm[] STARTUP_PERMS = {
            INTERNET, BLUETOOTH, BLUETOOTH_ADMIN, ACCESS_COARSE_LOCATION,
            BLUETOOTH_SCAN, BLUETOOTH_ADVERTISE, BLUETOOTH_CONNECT
    };

    public static boolean checkPerm(Activity activity, Perm perm) {
        return checkPerm(activity, perm.permission, perm.requestCode);
    }

    public static boolean checkPerm(Activity activity, @NotNull String permission, @IntRange(from = 0L) int requestCode) {
        if (ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(activity, new String[] { permission }, requestCode);
        }

        return ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED;
    }

    public static void requestAllStartup(Activity activity) {
        List<String> perms = new ArrayList<>();

        for (Perm perm : STARTUP_PERMS)
            perms.add(perm.permission);

        List<String> permsToRequest = new ArrayList<>();
        for (String perm : perms)
            if (ContextCompat.checkSelfPermission(activity, perm) == PackageManager.PERMISSION_DENIED)
                permsToRequest.add(perm);

        if (permsToRequest.isEmpty())
            return;

        String[] permsRequesting = new String[permsToRequest.size()];
        for (int i = 0; i < permsRequesting.length; i++)
            permsRequesting[i] = permsToRequest.get(i);

        ActivityCompat.requestPermissions(activity, permsRequesting, 99);
    }

    public static class Perm {
        @NotNull
        private final String permission;

        @IntRange(from = 0L)
        private final int requestCode;

        public Perm(@NonNull String permission, @IntRange(from = 0L) int requestCode) {
            this.permission = permission;
            this.requestCode = requestCode;
        }
    }

}