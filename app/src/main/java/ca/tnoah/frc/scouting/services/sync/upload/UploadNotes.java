package ca.tnoah.frc.scouting.services.sync.upload;

import android.util.Log;

import java.util.List;

import ca.tnoah.frc.scouting.models.dbo.Note;
import okhttp3.ResponseBody;
import retrofit2.Call;

public class UploadNotes extends UploadBase<Note> implements Runnable{

    public UploadNotes() {
        super("Notes");
    }

    @Override
    public void run() {
        logThreadStart();

        List<Note> notes = db.notesDAO().getAll();
        Call<ResponseBody> call = api.notes.addNotes(notes);
        upload(call);

        logThreadEnd();
    }

    @Override
    protected void onSuccess(ResponseBody responseBody) {

    }
}