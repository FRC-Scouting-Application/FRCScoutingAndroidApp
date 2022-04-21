package ca.tnoah.frc.scouting.services.sync;

public class SyncService {
    private static SyncService instance;

    public static SyncService getInstance() {
        if (instance == null)
            instance = new SyncService();
        return instance;
    }

    private SyncService() {

    }

    public void Sync() {
        DownloadService.getInstance().download();
    }
}
