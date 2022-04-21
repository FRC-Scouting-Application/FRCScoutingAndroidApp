package ca.tnoah.frc.scouting.services.api;

import java.util.List;

import ca.tnoah.frc.scouting.models.Team;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiTeamsService {

    @GET("Teams")
    Call<List<Team>> getAllTeams();

    @GET("Teams/{event_key}")
    Call<List<Team>> getTeamsByEvent(@Path("event_key") String eventKey);

    @GET("Teams/{team_key}/Media")
    Call<byte[]> getTeamMedia(@Path("team_key") String teamKey);

    @POST("Teams/Custom")
    Call<ResponseBody> addCustomTeams(@Body List<Team> teams);

}
