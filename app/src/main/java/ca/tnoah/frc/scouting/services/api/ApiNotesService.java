package ca.tnoah.frc.scouting.services.api;

import java.util.List;

import ca.tnoah.frc.scouting.models.dbo.Note;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiNotesService {

    @GET("Notes/Event/{event_key}")
    Call<List<Note>> getNotesByEvent(@Path("event_key") String eventKey);

    @GET("Notes/Team/{team_key}")
    Call<List<Note>> getNotesByTeam(@Path("team_key") String teamKey);

    @POST("Notes")
    Call<ResponseBody> addNotes(@Body List<Note> notes);

}
