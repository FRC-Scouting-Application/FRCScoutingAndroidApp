package ca.tnoah.frc.scouting.services.api;

import java.util.List;

import ca.tnoah.frc.scouting.models.dbo.Match;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiMatchesService {

    @GET("Matches/{event_key}")
    Call<List<Match>> getMatches(@Path("event_key") String eventKey);

    @POST("Matches/Custom")
    Call<ResponseBody> addCustomMatches(@Body List<Match> matches);

}
