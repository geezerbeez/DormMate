package com.itismob.s02.group9.mco.dormmate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.card.MaterialCardView;

public class RoommateDiscoveryFragment extends Fragment {

    public RoommateDiscoveryFragment() {
        super(R.layout.fragment_roommate_discovery);
    }

    /**
     * TODO:
     *  - Implement swipeable roommate profile cards (Pass/Like gestures)
     *  - Bookmark functionality
     *  - Navigate to full roommate profile --> add profile details
     *  - Handle empty discovery results
     *  - Roommate filters
     *  - Profile tags
     *
     */


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        MaterialCardView roommateProfile = view.findViewById(R.id.roommateProfile);

        roommateProfile.setOnClickListener(v -> {

            Intent intent = new Intent(
                    requireActivity(),
                    RoommateProfileActivity.class
            );

            startActivity(intent);
        });
    }
}