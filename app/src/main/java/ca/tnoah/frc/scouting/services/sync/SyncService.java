package ca.tnoah.frc.scouting.services.sync;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import ca.tnoah.frc.scouting.Preferences;
import ca.tnoah.frc.scouting.services.sync.download.DownloadEvents;
import ca.tnoah.frc.scouting.services.sync.download.DownloadScouts;
import ca.tnoah.frc.scouting.services.sync.download.DownloadTeams;
import ca.tnoah.frc.scouting.services.sync.download.DownloadTemplates;
import ca.tnoah.frc.scouting.services.sync.upload.UploadScouts;

public class SyncService {
    private static SyncService instance;
    private static final int N_THREADS = Runtime.getRuntime().availableProcessors();
    private static final String TAG = "==Sync Service==";

    private static final String LAST_SYNC = "LAST_SYNC";
    private final SharedPreferences settings;

    private Date lastSync = null;
    private boolean syncing;
    private OnSyncDoneListener onSyncDoneListener;

    private Timer autoSyncTimer;

    public static synchronized SyncService getInstance(Context context) {
        if (instance == null)
            instance = new SyncService(context);
        return instance;
    }

    public static synchronized SyncService getInstance() throws InstantiationError {
        if (instance == null) throw new InstantiationError();
        return instance;
    }

    private SyncService(Context context) {
        syncing = false;

        settings = Preferences.get(context, Preferences.SETTINGS);

        long lastSyncTimestamp = settings.getLong(LAST_SYNC, 0);
        if (lastSyncTimestamp > 0) lastSync = new Date(lastSyncTimestamp);
    }

    public synchronized boolean sync() {
        if (!syncing) {
            new Thread(new Sync()).start();
            syncing = true;
            return true;
        }

        return false;
    }

    private void notifyDone() {
        syncing = false;
        lastSync = new Date();
        Log.d(TAG, "Sync done at " + lastSync);

        long lastSyncTimestamp = lastSync.toInstant().toEpochMilli();
        SharedPreferences.Editor settings = this.settings.edit();
        settings.putLong(LAST_SYNC, lastSyncTimestamp);
        settings.apply();

        if (onSyncDoneListener != null)
            onSyncDoneListener.onSyncDone(lastSync);
    }

    public boolean isSyncing() {
        return this.syncing;
    }

    public Date getLastSync() {
        return this.lastSync;
    }

    public void setAutoSync(long delay) {
        stopAutoSync();

        autoSyncTimer = new Timer();
        AutoSync autoSync = new AutoSync(instance);

        autoSyncTimer.scheduleAtFixedRate(autoSync, 0, delay);
    }

    public void stopAutoSync() {
        if (autoSyncTimer != null)
            autoSyncTimer.cancel();
    }

    public void setOnSyncDoneListener(OnSyncDoneListener onSyncDoneListener) {
        this.onSyncDoneListener = onSyncDoneListener;
    }

    public interface OnSyncDoneListener {
        void onSyncDone(Date timeUpdated);
    }

    public class Sync implements Runnable {

        @Override
        public void run() {
            ExecutorService pool = Executors.newFixedThreadPool(N_THREADS);
            List<Future<?>> futures = new ArrayList<>();

            upload(futures, pool);
            download(futures, pool);

            pool.shutdown();

            for (Future<?> future : futures) {
                try {
                    future.get();
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }
            }

            notifyDone();
        }

        private void upload(List<Future<?>> futures, ExecutorService pool) {
            //pool.submit(new UploadNotes());
            futures.add(pool.submit(new UploadScouts()));
        }

        private void download(List<Future<?>> futures, ExecutorService pool) {
            futures.add(pool.submit(new DownloadEvents()));
            futures.add(pool.submit(new DownloadScouts()));
            futures.add(pool.submit(new DownloadTeams()));
            futures.add(pool.submit(new DownloadTemplates()));
        }
    }

    private class AutoSync extends TimerTask {
        private final SyncService syncService;

        public AutoSync(SyncService syncService) {
            this.syncService = syncService;
        }

        @Override
        public void run() {
            syncService.sync();
        }
    }
}
