package ca.tnoah.frc.scouting.services;

import ca.tnoah.frc.scouting.services.api.ApiEventsService;
import ca.tnoah.frc.scouting.services.api.ApiMatchesService;
import ca.tnoah.frc.scouting.services.api.ApiNotesService;
import ca.tnoah.frc.scouting.services.api.ApiScoutsService;
import ca.tnoah.frc.scouting.services.api.ApiTeamsService;
import ca.tnoah.frc.scouting.services.api.ApiTemplateService;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiService {
    private static ApiService instance;

    private static final String BASE_URL = "https://localhost:7089/api/";

    private final Retrofit retrofit;

    public ApiEventsService events;
    public ApiMatchesService matches;
    public ApiNotesService notes;
    public ApiScoutsService scouts;
    public ApiTeamsService teams;
    public ApiTemplateService templates;

    public static ApiService getInstance() {
        if (instance == null)
            instance = new ApiService();
        return instance;
    }

    private ApiService() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        OkHttpClient client = builder.build();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        events = retrofit.create(ApiEventsService.class);
        matches = retrofit.create(ApiMatchesService.class);
        notes = retrofit.create(ApiNotesService.class);
        scouts = retrofit.create(ApiScoutsService.class);
        teams = retrofit.create(ApiTeamsService.class);
        templates = retrofit.create(ApiTemplateService.class);
    }

}
