package ca.tnoah.frc.scouting.services.sync.upload;

import java.util.List;

import ca.tnoah.frc.scouting.models.dbo.Scout;
import okhttp3.ResponseBody;
import retrofit2.Call;

public class UploadScouts extends UploadBase<Scout> implements Runnable {

    public UploadScouts() {
        super("Scouts");
    }

    @Override
    public void run() {
        logThreadStart();

        List<Scout> scouts = db.scoutsDAO().getAll();
        Call<ResponseBody> call = api.scouts.addScouts(scouts);
        upload(call);

        logThreadEnd();
    }

    @Override
    protected void onSuccess(ResponseBody responseBody) {

    }
}