package ca.tnoah.frc.scouting.services.sync;

import java.util.Date;

import ca.tnoah.frc.scouting.services.sync.download.DownloadEvents;
import ca.tnoah.frc.scouting.services.sync.download.DownloadTeams;
import ca.tnoah.frc.scouting.services.sync.download.DownloadTemplates;
import ca.tnoah.frc.scouting.services.sync.upload.UploadNotes;

public class SyncService {
    private static SyncService instance;

    // DOWNLOAD
    private final DownloadEvents events = new DownloadEvents();
    private final DownloadTeams teams = new DownloadTeams();
    private final DownloadTemplates templates = new DownloadTemplates();

    // UPLOAD
    private final UploadNotes notes = new UploadNotes();

    private Date timeSinceLastUpdate = null;

    public static SyncService getInstance() {
        if (instance == null)
            instance = new SyncService();
        return instance;
    }

    private SyncService() {

    }

    public void Sync() {
        upload();

        //download();

        timeSinceLastUpdate = new Date();
    }

    private void upload() {
        Thread notesThread = new Thread(notes);

        notesThread.start();
    }

    private void download() {
        downloadKeylessItems();
    }

    private void downloadKeylessItems() {
        Thread eventsThread = new Thread(events);
        Thread teamsThread = new Thread(teams);
        Thread templatesThread = new Thread(templates);

        eventsThread.start();
        teamsThread.start();
        templatesThread.start();
    }
}
