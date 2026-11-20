package com.itismob.s02.group9.mco.dormmate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.slider.RangeSlider;

public class ReadProfile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_profile);

        showProfile();
        setupButtons();
        setupBottomNav();
    }

    // placeholder data rn; replace with saved data later
    private void showProfile() {
        ((TextView) findViewById(R.id.nameTv)).setText("Maya Santos");
        ((ShapeableImageView) findViewById(R.id.profilePicture)).setImageResource(R.drawable.mayasantospfp);
        ((TextView) findViewById(R.id.profileProgressTv)).setText("Profile 100% complete");
        ((LinearProgressIndicator) findViewById(R.id.profileProgressBar)).setProgress(100);

        ((TextView) findViewById(R.id.genderTv)).setText("Woman");
        ((TextView) findViewById(R.id.ageTv)).setText("20");
        ((TextView) findViewById(R.id.courseTv)).setText("BA Communication");
        ((TextView) findViewById(R.id.yearTv)).setText("2nd year");

        ((TextView) findViewById(R.id.budgetRangeTv)).setText("₱6,000–₱10,000");
        ((RangeSlider) findViewById(R.id.budgetSlider)).setValues(6000f, 10000f);
        ((TextView) findViewById(R.id.areaTv)).setText("Katipunan · UP Diliman · Teacher's Village");

        ((TextView) findViewById(R.id.drinkingTv)).setText("Sometimes");
        ((TextView) findViewById(R.id.smokingTv)).setText("No");
        // placeholder habits; replace with saved data later
        findViewById(R.id.earlySleeperChip).setVisibility(View.GONE);
        findViewById(R.id.veryTidyChip).setVisibility(View.VISIBLE);
        findViewById(R.id.quietWeekdaysChip).setVisibility(View.VISIBLE);

        ((TextView) findViewById(R.id.roommateGenderTv)).setText("Any gender");
        ((TextView) findViewById(R.id.roommateAgeTv)).setText("18–24");
        ((RangeSlider) findViewById(R.id.roommateAgeSlider)).setValues(18f, 24f);

        ((TextView) findViewById(R.id.additionalInfoTv)).setText(
                "I value clear communication, shared chores, and a calm home for studying.");
    }

    private void setupButtons() {
        findViewById(R.id.backButtonIb).setOnClickListener(v -> finish());

        // edit button -> EditProfile
        findViewById(R.id.editButtonIb).setOnClickListener(v ->
                startActivity(new Intent(this, EditProfile.class)));
    }

    private void setupBottomNav() {
        BottomNavigationView nav = findViewById(R.id.bottomNavigation);

        // Highlight the Profile tab. Do this BEFORE attaching the listener,
        // otherwise setting the selected item fires the listener.
        nav.setSelectedItemId(R.id.nav_profile); // use your real menu item id

        nav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_profile) {
                return true; // already here
            }
            // TODO: start the other screens, e.g.
            // if (id == R.id.nav_explore) { startActivity(new Intent(this, ExploreActivity.class)); return true; }
            return false;
        });
    }
}
