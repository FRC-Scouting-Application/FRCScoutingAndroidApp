package ca.tnoah.frc.scouting.ui;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.SearchView;

import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import ca.tnoah.frc.scouting.R;
import ca.tnoah.frc.scouting.databinding.ActivityMainBinding;
import ca.tnoah.frc.scouting.models.template.TemplateData;
import ca.tnoah.frc.scouting.models.template.TemplateSerializer;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener, NavController.OnDestinationChangedListener {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;

    private MainViewModel viewModel;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initNav();
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
    }

    private void initNav() {
        setSupportActionBar(binding.appBarMain.toolbar);
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_teams, R.id.nav_events, R.id.nav_share, R.id.nav_export, R.id.nav_sync, R.id.nav_templates, R.id.nav_settings)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        navController.addOnDestinationChangedListener(this);
    }

    @Override
    public void onDestinationChanged(@NonNull NavController navController, @NonNull NavDestination navDestination, @Nullable Bundle bundle) {
        if (searchView == null) return;

        String dest = navDestination.getDisplayName().split(":id/nav_")[1];

        switch (dest) {
            case "teams":
            case "events":
                searchView.setVisibility(View.VISIBLE);
                break;
            default:
                searchView.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(this);

        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        viewModel.setSearch(s);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        viewModel.setSearch(s);
        return false;
    }

    public void resetSearch() {
        if (searchView == null) return;

        searchView.setQuery("", false);
        searchView.setIconified(true);
    }
}