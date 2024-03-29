package ca.tnoah.frc.scouting.ui.teams.details;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import ca.tnoah.frc.scouting.R;
import ca.tnoah.frc.scouting.helpers.DownloadImage;
import ca.tnoah.frc.scouting.models.dbo.Team;
import ca.tnoah.frc.scouting.services.DatabaseService;
import ca.tnoah.frc.scouting.services.localdb.AppDatabase;
import ca.tnoah.frc.scouting.databinding.ActivityTeamDetailBinding;

public class TeamDetailActivity extends AppCompatActivity implements NavController.OnDestinationChangedListener  {
    private static final String TAG = "==TeamDetailActivity==";

    private final AppDatabase db = DatabaseService.getInstance().getDB();

    public static final String TEAM_KEY = "team_key";
    private ActivityTeamDetailBinding binding;

    private ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityTeamDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initNav();
        TeamViewModel viewModel = new ViewModelProvider(this).get(TeamViewModel.class);
        Intent intent = getIntent();
        String teamKey = intent.getStringExtra(TEAM_KEY);

        image = findViewById(R.id.team_image);

        Team team = db.teamsDAO().get(teamKey);
        if (team != null) {
            setTitle(team.teamNumber + " - " + team.nickname);

            DownloadImage downloadImage = new DownloadImage("https://i.imgur.com/q4SYoxBh.jpg");
            downloadImage.getBitmap().observe(this, bitmap -> {
                if (bitmap != null)
                    image.setImageBitmap(bitmap);
            });

            viewModel.setTeam(team);
        }
    }

    @Override
    public void onDestinationChanged(@NonNull NavController navController, @NonNull NavDestination navDestination, @Nullable Bundle bundle) {
        if (image == null) return;

        String dest = navDestination.getDisplayName().split(":id/nav_")[1];

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 0
        );
        params.weight = (dest.equals("team")) ? 1.5f : 4.0f;
        findViewById(R.id.nav_host_fragment_activity_team_detail).setLayoutParams(params);
    }

    private void initNav() {
        setSupportActionBar(binding.appBarTeamDetail.toolbar);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_team, R.id.nav_pit, R.id.nav_match, R.id.nav_notes)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_team_detail);
        setTitle("");
        NavigationUI.setupWithNavController(binding.navView, navController);

        navController.addOnDestinationChangedListener(this);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}