package ca.tnoah.frc.scouting.services.sync.download;

import android.util.Log;

import java.util.List;

import ca.tnoah.frc.scouting.models.dbo.Template;
import retrofit2.Call;

public class DownloadTemplates extends DownloadBase<Template> implements Runnable {

    public DownloadTemplates() {
        super("Templates");
    }

    @Override
    public void run() {
        Log.d(tag, "Download Templates Thread Started...");

        Call<List<Template>> call = api.templates.getTemplates();
        download(call);

        Log.d(tag, "Download Templates Thread Ended...");
    }

    @Override
    public void onRetrieve(List<Template> templates) {
        db.templatesDAO().insertOrUpdate(templates);
    }

}