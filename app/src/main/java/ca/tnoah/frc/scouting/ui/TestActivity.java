package ca.tnoah.frc.scouting.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import ca.tnoah.frc.scouting.R;
import ca.tnoah.frc.scouting.ui.form.FormFragment;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        FormFragment formFragment = new FormFragment();
        transaction.replace(R.id.testFrame, formFragment);
        transaction.commit();
    }
}