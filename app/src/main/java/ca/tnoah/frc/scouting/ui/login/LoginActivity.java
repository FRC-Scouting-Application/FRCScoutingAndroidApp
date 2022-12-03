package ca.tnoah.frc.scouting.ui.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import ca.tnoah.frc.scouting.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;

    public static final String INTENT_MODE = "Intent_Mode";
    private String appMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        appMode = intent.getStringExtra(INTENT_MODE);

        if (appMode == null) {
            goBack();
            return;
        }

        if (appMode.equals(WelcomeActivity.AppMode.WITH_TEAM)) {
            binding.loginServerAddress.setVisibility(View.INVISIBLE);
            binding.loginServerAddressLabel.setVisibility(View.INVISIBLE);
        }
    }

    private void onContinue(View view) {

    }

    private void goBack() {
        Intent intent = new Intent(this, WelcomeActivity.class);
        startActivity(intent);
    }
}