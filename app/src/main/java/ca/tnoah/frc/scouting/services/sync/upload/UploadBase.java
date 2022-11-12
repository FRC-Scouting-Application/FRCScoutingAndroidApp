package ca.tnoah.frc.scouting.services.sync.upload;

import android.util.Log;

import ca.tnoah.frc.scouting.services.ApiService;
import ca.tnoah.frc.scouting.services.DatabaseService;
import ca.tnoah.frc.scouting.services.localdb.AppDatabase;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class UploadBase<T> {

    protected final String type;
    protected final String tag;

    protected final ApiService api;
    protected final AppDatabase db;

    protected UploadBase(String type) {
        this.type = type;
        this.tag = "==Upload" + type + "==";

        this.api = ApiService.getInstance();
        this.db = DatabaseService.getInstance().getDB();
    }

    protected void upload(Call<ResponseBody> call) {
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (!response.isSuccessful())
                    fail("response unsuccessful " + response.toString());
                else
                    onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                fail(t.getMessage());
            }
        });
    }

    protected abstract void onSuccess(ResponseBody responseBody);

    private void fail(String msg) {
        Log.d(tag, "Failed to upload: " + type + " - " + msg);
    }

    protected void logThreadStart() {
        Log.d(tag, "Download " + tag + " Thread Started...");
    }

    protected void logThreadEnd() {
        Log.d(tag, "Download " + tag + " Thread Ended...");
    }
}