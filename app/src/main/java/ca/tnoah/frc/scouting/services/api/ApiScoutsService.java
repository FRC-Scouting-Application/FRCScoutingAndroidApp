package ca.tnoah.frc.scouting.services.api;

import java.util.List;

import ca.tnoah.frc.scouting.models.dbo.Scout;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiScoutsService {

    @GET("Scouts/Event/{event_key}")
    Call<List<Scout>> getScoutsByEvent(@Path("event_key") String eventKey);

    @GET("Scouts/Team/{team_key}")
    Call<List<Scout>> getScoutsByTeam(@Path("team_key") String teamKey);

    @GET("Scouts")
    Call<List<Scout>> getScouts();

    @POST("Scouts")
    Call<ResponseBody> addScouts(@Body List<Scout> notes);

}
