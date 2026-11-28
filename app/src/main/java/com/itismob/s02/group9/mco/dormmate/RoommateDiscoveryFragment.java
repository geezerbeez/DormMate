package com.itismob.s02.group9.mco.dormmate;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.List;

public class RoommateDiscoveryFragment extends Fragment {

    private MaterialCardView roommateProfile;
    private LinearLayout emptyState;
    private ImageButton xBtn, likeBtn, filterBtn;

    private TextView roommateNameAge, roommateCourse, roommateLocation, nearProfiles;
    private ImageView roommateImage;
    private Chip roommateCompatibility;
    private ChipGroup roommateTags;

    private List<Roommate> allRoommates;   // master list
    private List<Roommate> roommates;      // currently shown (filtered)
    private RoommateFilter filter = new RoommateFilter();
    private int currentIndex = 0;

    private float downX;
    private float downY;
    private boolean isDragging = false;
    private boolean isAnimating = false;
    private int touchSlop;

    private static final float SWIPE_THRESHOLD = 120f;
    private static final float SWIPE_ROTATION = 12f;

    public RoommateDiscoveryFragment() {
        super(R.layout.fragment_roommate_discovery);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        touchSlop = ViewConfiguration.get(requireContext()).getScaledTouchSlop();

        roommateProfile = view.findViewById(R.id.roommateProfile);
        emptyState = view.findViewById(R.id.emptyState);
        xBtn = view.findViewById(R.id.xBtn);
        likeBtn = view.findViewById(R.id.likeBtn);
        filterBtn = view.findViewById(R.id.filterBtn);

        roommateImage = view.findViewById(R.id.roommateImage);
        roommateNameAge = view.findViewById(R.id.roommateNameAge);
        roommateCourse = view.findViewById(R.id.roommateCourse);
        roommateLocation = view.findViewById(R.id.roommateLocation);
        nearProfiles = view.findViewById(R.id.nearProfiles);
        roommateCompatibility = view.findViewById(R.id.roommateCompatibility);
        roommateTags = view.findViewById(R.id.roommateTags);

        // Sample student users
        allRoommates = RoommateDataHelper.getRoommates();
        roommates = new ArrayList<>(allRoommates);

        displayCurrentRoommate();

        xBtn.setOnClickListener(v -> passRoommate());
        likeBtn.setOnClickListener(v -> likeRoommate());

        // Filters
        filterBtn.setOnClickListener(v ->
                RoommateFilterBottomSheet.newInstance(filter).show(getChildFragmentManager(), "filter"));

        getChildFragmentManager().setFragmentResultListener(
                RoommateFilterBottomSheet.REQUEST_KEY, getViewLifecycleOwner(), (key, bundle) -> {
                    RoommateFilter result =
                            (RoommateFilter) bundle.getSerializable(RoommateFilterBottomSheet.RESULT_FILTER);
                    if (result != null) {
                        filter = result;
                        applyFilter();
                    }
                });

        roommateProfile.setOnTouchListener((v, event) -> {
            if (isAnimating) return true;

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    downX = event.getRawX();
                    downY = event.getRawY();
                    isDragging = false;
                    return true;

                case MotionEvent.ACTION_MOVE: {
                    float dx = event.getRawX() - downX;
                    float dy = event.getRawY() - downY;

                    if (!isDragging && Math.abs(dx) > touchSlop && Math.abs(dx) > Math.abs(dy)) {
                        isDragging = true;
                    }
                    if (isDragging) {
                        roommateProfile.setTranslationX(dx);
                        roommateProfile.setRotation(dx / roommateProfile.getWidth() * SWIPE_ROTATION);
                        roommateProfile.setAlpha(
                                1f - Math.min(Math.abs(dx) / (roommateProfile.getWidth() * 1.5f), 0.4f));
                    }
                    return true;
                }

                case MotionEvent.ACTION_UP: {
                    float dx = event.getRawX() - downX;

                    if (isDragging) {
                        if (dx > SWIPE_THRESHOLD) {
                            likeRoommate();
                        } else if (dx < -SWIPE_THRESHOLD) {
                            passRoommate();
                        } else {
                            resetCardPosition();
                        }
                    } else {
                        openRoommateProfile();
                    }
                    return true;
                }

                case MotionEvent.ACTION_CANCEL:
                    resetCardPosition();
                    return true;
            }
            return true;
        });

        RoommateMatchManager.resetLikes();

        // Simulate user_002 already liking user_001
        RoommateMatchManager.simulateLike("user_002", "user_001");
    }

    private void applyFilter() {
        if (isAnimating) return;

        roommates = new ArrayList<>();
        for (Roommate r : allRoommates) {
            if (filter.matches(r)) roommates.add(r);
        }
        currentIndex = 0;

        int color = filter.isActive() ? R.color.orange : R.color.darkGrey;
        filterBtn.setImageTintList(ColorStateList.valueOf(
                ContextCompat.getColor(requireContext(), color)));

        displayCurrentRoommate();
    }

    private void displayCurrentRoommate() {
        if (currentIndex >= roommates.size()) {
            showEmptyState();
            return;
        }

        Roommate roommate = roommates.get(currentIndex);

        roommateImage.setImageResource(roommate.photoRes);
        roommateNameAge.setText(roommate.name + ", " + roommate.age);
        roommateCourse.setText(roommate.course + " • " + roommate.year);
        roommateLocation.setText("Budget " + roommate.budgetLabel());
        roommateCompatibility.setText(roommate.compatibilityLabel());

        roommateTags.removeAllViews();
        for (String tag : roommate.tags) {
            Chip chip = (Chip) getLayoutInflater()
                    .inflate(R.layout.item_tag_chip, roommateTags, false);
            chip.setText(tag);
            roommateTags.addView(chip);
        }

        nearProfiles.setText((roommates.size() - currentIndex) + " compatible profiles for you");

        // Reset card position
        roommateProfile.setTranslationX(0);
        roommateProfile.setTranslationY(0);
        roommateProfile.setRotation(0);
        roommateProfile.setAlpha(1f);
        roommateProfile.setVisibility(View.VISIBLE);

        emptyState.setVisibility(View.GONE);
        xBtn.setVisibility(View.VISIBLE);
        likeBtn.setVisibility(View.VISIBLE);
        xBtn.setEnabled(true);
        likeBtn.setEnabled(true);
        xBtn.setAlpha(1f);
        likeBtn.setAlpha(1f);
    }

    private void passRoommate() {
        animateSwipe(-1);
    }

    private void likeRoommate() {
        if (currentIndex >= roommates.size() || isAnimating) return;

        Roommate roommate = roommates.get(currentIndex);

        String currentUserId = "user_001"; // Replace with logged-in user ID
        String targetUserId = roommate.id;

        boolean isMatch = RoommateMatchManager.likeUser(
                currentUserId,
                targetUserId
        );

        if (isMatch) {
            showMatchDialog(roommate);
        }

        animateSwipe(1);
    }

    private void animateSwipe(int direction) {
        if (isAnimating || currentIndex >= roommates.size()) return;
        isAnimating = true;

        roommateProfile.animate()
                .translationX(direction * roommateProfile.getWidth() * 1.5f)
                .rotation(direction * 20f)
                .alpha(0f)
                .setDuration(300)
                .withEndAction(() -> {
                    if (!isAdded()) return;
                    currentIndex++;
                    displayCurrentRoommate();
                    isAnimating = false;
                })
                .start();
    }

    private void resetCardPosition() {
        roommateProfile.animate()
                .translationX(0)
                .translationY(0)
                .rotation(0)
                .alpha(1f)
                .setDuration(200)
                .start();
    }

    private void openRoommateProfile() {
        if (currentIndex >= roommates.size()) return;

        Intent intent = new Intent(requireActivity(), RoommateProfileActivity.class);
        intent.putExtra(RoommateProfileActivity.EXTRA_ROOMMATE_ID, roommates.get(currentIndex).id);
        startActivity(intent);
    }

    private void showMatchDialog(Roommate roommate) {

        View dialogView = getLayoutInflater()
                .inflate(R.layout.activity_roommate_dialog_match, null);

        ImageView myImage = dialogView.findViewById(R.id.myMatchImage);

        ImageView otherImage = dialogView.findViewById(R.id.otherMatchImage);

        TextView matchMessage = dialogView.findViewById(R.id.matchMessage);

        MaterialButton continueBtn = dialogView.findViewById(R.id.continueMatchBtn);

        MaterialButton messageBtn = dialogView.findViewById(R.id.messageMatchBtn);

        otherImage.setImageResource(roommate.photoRes);

        matchMessage.setText(
                "You and " + roommate.name + " liked each other!"
        );

        androidx.appcompat.app.AlertDialog dialog =
                new androidx.appcompat.app.AlertDialog.Builder(requireContext())
                        .setView(dialogView)
                        .create();

        continueBtn.setOnClickListener(v -> dialog.dismiss());

        // Same chat as "Connect with ..." on their profile (one chat per person).
        messageBtn.setOnClickListener(v -> {
            dialog.dismiss();
            ChatLauncher.openChatWith(requireActivity(), roommate);
        });

        dialog.show();

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(
                    android.R.color.transparent
            );

            dialog.getWindow().setLayout(
                    android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                    android.view.ViewGroup.LayoutParams.WRAP_CONTENT
            );
        }
    }

    private void showEmptyState() {
        roommateProfile.setVisibility(View.GONE);
        emptyState.setVisibility(View.VISIBLE);

        xBtn.setVisibility(View.VISIBLE);
        likeBtn.setVisibility(View.VISIBLE);
        xBtn.setEnabled(false);
        likeBtn.setEnabled(false);
        xBtn.setAlpha(0.4f);
        likeBtn.setAlpha(0.4f);

        nearProfiles.setText(roommates.isEmpty()
                ? "No roommates match your filters"
                : "No more compatible profiles");
    }
}