package ca.tnoah.frc.scouting.services.sync.download;

import android.util.Log;

import java.util.List;

import ca.tnoah.frc.scouting.models.Template;
import retrofit2.Call;

public class DownloadTemplates extends DownloadBase<Template> implements Runnable {

    private static final String TAG = "==DownloadEvents==";
    private static final String TYPE = "Events";

    public DownloadTemplates() {
        super(TAG);
    }

    @Override
    public void run() {
        Log.d(tag, "Download Templates Thread Started...");

        Call<List<Template>> call = api.templates.getTemplates();
        download(call, this::onRetrieve, TYPE);

        Log.d(tag, "Download Templates Thread Ended...");
    }

    public void onRetrieve(List<Template> templates) {
        db.templatesDAO().insertOrUpdate(templates);
    }

}