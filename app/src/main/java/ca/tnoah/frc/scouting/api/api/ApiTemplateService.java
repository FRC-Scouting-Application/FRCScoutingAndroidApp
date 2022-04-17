package ca.tnoah.frc.scouting.api.api;

import java.util.List;

import ca.tnoah.frc.scouting.api.models.Template;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiTemplateService {

    @GET("Template")
    Call<List<Template>> getTemplates();

    @POST("Template")
    Call<ResponseBody> addTemplates(@Body List<Template> templates);
}
