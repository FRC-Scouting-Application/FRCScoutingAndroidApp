package ca.tnoah.frc.scouting.api.api;

import java.util.List;

import ca.tnoah.frc.scouting.api.models.Event;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiEventsService {

    @GET("Events")
    Call<List<Event>> getEvents();

    @POST("Events/Custom")
    Call<ResponseBody> addCustomEvents(@Body List<Event> events);

}
