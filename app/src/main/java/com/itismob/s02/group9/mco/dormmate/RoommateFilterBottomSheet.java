package com.itismob.s02.group9.mco.dormmate;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.slider.RangeSlider;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class RoommateFilterBottomSheet extends BottomSheetDialogFragment {

    public static final String REQUEST_KEY = "roommate_filter";
    public static final String RESULT_FILTER = "filter";
    private static final String ARG_FILTER = "current_filter";

    private static final List<String> GENDERS =
            Arrays.asList("Women", "Men");
    private static final List<String> LOCATIONS =
            Arrays.asList("Katipunan", "Diliman", "Teachers Village", "Maginhawa");
    private static final List<String> LIFESTYLE =
            Arrays.asList("Pet-friendly", "Study-focused", "Early sleeper",
                    "Night owl", "Tidy", "Non-smoker", "Introvert", "Friendly");

    private ChipGroup genderGroup, locationGroup, lifestyleGroup;
    private RangeSlider ageSlider, budgetSlider;
    private TextView ageLabel, budgetLabel;

    public static RoommateFilterBottomSheet newInstance(RoommateFilter current) {
        RoommateFilterBottomSheet sheet = new RoommateFilterBottomSheet();
        Bundle args = new Bundle();
        args.putSerializable(ARG_FILTER, current);
        sheet.setArguments(args);
        return sheet;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.roommate_bottom_sheet_filter, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        genderGroup = view.findViewById(R.id.genderGroup);
        locationGroup = view.findViewById(R.id.locationGroup);
        lifestyleGroup = view.findViewById(R.id.lifestyleGroup);
        ageSlider = view.findViewById(R.id.ageSlider);
        budgetSlider = view.findViewById(R.id.budgetSlider);
        ageLabel = view.findViewById(R.id.ageLabel);
        budgetLabel = view.findViewById(R.id.budgetLabel);

        RoommateFilter current = (RoommateFilter) requireArguments().getSerializable(ARG_FILTER);
        if (current == null) current = new RoommateFilter();

        fillChips(genderGroup, GENDERS, current.genders);
        fillChips(locationGroup, LOCATIONS, current.locations);
        fillChips(lifestyleGroup, LIFESTYLE, current.lifestyle);

        ageSlider.setValues((float) current.minAge, (float) current.maxAge);
        budgetSlider.setValues((float) current.minBudget, (float) current.maxBudget);
        updateLabels();

        ageSlider.addOnChangeListener((s, v, fromUser) -> updateLabels());
        budgetSlider.addOnChangeListener((s, v, fromUser) -> updateLabels());

        view.findViewById(R.id.resetBtn).setOnClickListener(v -> resetControls());
        view.findViewById(R.id.applyBtn).setOnClickListener(v -> applyAndClose());
    }

    private void fillChips(ChipGroup group, List<String> options, Set<String> selected) {
        group.removeAllViews();
        for (String option : options) {
            Chip chip = (Chip) getLayoutInflater().inflate(R.layout.item_filter_chip, group, false);
            chip.setId(View.generateViewId());
            chip.setText(option);
            chip.setChecked(selected.contains(option));
            group.addView(chip);
        }
    }

    private Set<String> checkedTexts(ChipGroup group) {
        Set<String> result = new HashSet<>();
        for (int i = 0; i < group.getChildCount(); i++) {
            Chip chip = (Chip) group.getChildAt(i);
            if (chip.isChecked()) result.add(chip.getText().toString());
        }
        return result;
    }

    private void updateLabels() {
        List<Float> age = ageSlider.getValues();
        List<Float> budget = budgetSlider.getValues();
        ageLabel.setText(String.format(Locale.US, "Age range: %d – %d",
                age.get(0).intValue(), age.get(1).intValue()));
        budgetLabel.setText(String.format(Locale.US, "Budget: ₱%,d – ₱%,d",
                budget.get(0).intValue(), budget.get(1).intValue()));
    }

    private void resetControls() {
        fillChips(genderGroup, GENDERS, new HashSet<>());
        fillChips(locationGroup, LOCATIONS, new HashSet<>());
        fillChips(lifestyleGroup, LIFESTYLE, new HashSet<>());
        ageSlider.setValues((float) RoommateFilter.AGE_MIN, (float) RoommateFilter.AGE_MAX);
        budgetSlider.setValues((float) RoommateFilter.BUDGET_MIN, (float) RoommateFilter.BUDGET_MAX);
        updateLabels();
    }

    private void applyAndClose() {
        RoommateFilter filter = new RoommateFilter();
        filter.genders = checkedTexts(genderGroup);
        filter.locations = checkedTexts(locationGroup);
        filter.lifestyle = checkedTexts(lifestyleGroup);
        filter.minAge = ageSlider.getValues().get(0).intValue();
        filter.maxAge = ageSlider.getValues().get(1).intValue();
        filter.minBudget = budgetSlider.getValues().get(0).intValue();
        filter.maxBudget = budgetSlider.getValues().get(1).intValue();

        Bundle result = new Bundle();
        result.putSerializable(RESULT_FILTER, filter);
        getParentFragmentManager().setFragmentResult(REQUEST_KEY, result);
        dismiss();
    }
}