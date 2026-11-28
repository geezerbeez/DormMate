package com.itismob.s02.group9.mco.dormmate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.slider.RangeSlider;

public class ProfileFragment extends Fragment {

    public ProfileFragment() {
        super(R.layout.fragment_profile);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        showProfile(view);
        view.findViewById(R.id.editButtonIb).setOnClickListener(v ->
                startActivity(new Intent(requireContext(), ProfileEdit.class)));
    }

    // placeholder data rn; replace with saved data later
    private void showProfile(View root) {
        ((TextView) root.findViewById(R.id.nameTv)).setText("Maya Santos");
        ((ShapeableImageView) root.findViewById(R.id.profilePicture))
                .setImageResource(R.drawable.img_roommateprofile);
        ((TextView) root.findViewById(R.id.profileProgressTv)).setText("Profile 100% complete");
        ((LinearProgressIndicator) root.findViewById(R.id.profileProgressBar)).setProgress(100);

        ((TextView) root.findViewById(R.id.genderTv)).setText("Woman");
        ((TextView) root.findViewById(R.id.ageTv)).setText("20");
        ((TextView) root.findViewById(R.id.courseTv)).setText("BA Communication");
        ((TextView) root.findViewById(R.id.yearTv)).setText("2nd year");

        ((TextView) root.findViewById(R.id.budgetRangeTv)).setText("₱6,000–₱10,000");
        ((RangeSlider) root.findViewById(R.id.budgetSlider)).setValues(6000f, 10000f);
        ((TextView) root.findViewById(R.id.areaTv)).setText("Katipunan · UP Diliman · Teacher's Village");

        ((TextView) root.findViewById(R.id.drinkingTv)).setText("Sometimes");
        ((TextView) root.findViewById(R.id.smokingTv)).setText("No");
        root.findViewById(R.id.earlySleeperChip).setVisibility(View.GONE);
        root.findViewById(R.id.veryTidyChip).setVisibility(View.VISIBLE);
        root.findViewById(R.id.quietWeekdaysChip).setVisibility(View.VISIBLE);

        ((TextView) root.findViewById(R.id.roommateGenderTv)).setText("Any gender");
        ((TextView) root.findViewById(R.id.roommateAgeTv)).setText("18–24");
        ((RangeSlider) root.findViewById(R.id.roommateAgeSlider)).setValues(18f, 24f);

        ((TextView) root.findViewById(R.id.additionalInfoTv)).setText(
                "I value clear communication, shared chores, and a calm home for studying.");
    }
}