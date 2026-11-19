package com.itismob.s02.group9.mco.dormmate;

import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.chip.ChipGroup;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.slider.RangeSlider;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class EditProfile extends AppCompatActivity {

    private static final int MIN_AGE = 18;
    private static final int MAX_AGE = 99;
    private static final int MAX_INFO_LENGTH = 300;

    // Roommate age-range slider limits (must match the slider in the layout)
    private static final float ROOMMATE_AGE_FROM = 18f;
    private static final float ROOMMATE_AGE_TO = 60f;

    private static final String[] GENDERS = {"Woman", "Man", "Non-binary", "Prefer not to say"};
    private static final String[] COURSES = {"BA Communication", "BS Computer Science",
            "BS Business Administration", "BS Psychology"};
    private static final String[] YEAR_LEVELS = {"1st year", "2nd year", "3rd year", "4th year", "Graduate"};
    private static final String[] AREAS = {
            "Katipunan · UP Diliman · Teacher's Village",
            "Taft · Malate · Vito Cruz",
            "España · Sampaloc · Morayta",
            "BGC · Guadalupe · Poblacion"};
    private static final String[] HABIT_OPTIONS = {"Yes", "Sometimes", "Trying to quit", "No"};

    private TextInputLayout genderLayout, ageLayout, courseLayout, yearLayout, areaLayout,
            drinkingLayout, smokingLayout, additionalInfoLayout;
    private MaterialAutoCompleteTextView genderDropdown, courseDropdown, yearDropdown,
            areaDropdown, drinkingDropdown, smokingDropdown;
    private TextInputEditText ageEt, additionalInfoEt;
    private RangeSlider budgetSlider, roommateAgeSlider;
    private TextView budgetRangeTv, roommateAgeTv;
    private ChipGroup roommateGenderChipGroup;

    private ImageView profilePicture, profilePictureEdit;
    private TextView profileProgressTv;
    private LinearProgressIndicator profileProgressBar;
    private Uri selectedPhotoUri; // photo picked in this session (null = none yet)

    // Opens the system photo picker (no storage permission needed)
    private final ActivityResultLauncher<PickVisualMediaRequest> photoPicker =
            registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
                if (uri != null) {
                    selectedPhotoUri = uri;
                    profilePicture.setImageURI(uri);
                    updateProgress();
                }
            });

    private final NumberFormat moneyFormat = NumberFormat.getNumberInstance(Locale.US);
    private View firstInvalid; // first field that failed validation (to focus it)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        bindViews();
        setupDropdown(genderLayout, genderDropdown, GENDERS);
        setupDropdown(courseLayout, courseDropdown, COURSES);
        setupDropdown(yearLayout, yearDropdown, YEAR_LEVELS);
        setupDropdown(areaLayout, areaDropdown, AREAS);
        setupDropdown(drinkingLayout, drinkingDropdown, HABIT_OPTIONS);
        setupDropdown(smokingLayout, smokingDropdown, HABIT_OPTIONS);
        setupBudgetSlider();
        setupRoommateAgeSlider();
        clearErrorOnEdit(ageLayout, ageEt);
        clearErrorOnEdit(additionalInfoLayout, additionalInfoEt);
        setupProfilePhoto();
        updateProgress();

        findViewById(R.id.backButtonIb).setOnClickListener(v -> finish());
        findViewById(R.id.saveButton).setOnClickListener(v -> onSavePressed());
    }

    private void bindViews() {
        genderLayout = findViewById(R.id.genderLayout);
        ageLayout = findViewById(R.id.ageLayout);
        courseLayout = findViewById(R.id.courseLayout);
        yearLayout = findViewById(R.id.yearLayout);
        areaLayout = findViewById(R.id.areaLayout);
        drinkingLayout = findViewById(R.id.drinkingLayout);
        smokingLayout = findViewById(R.id.smokingLayout);
        additionalInfoLayout = findViewById(R.id.additionalInfoLayout);

        genderDropdown = findViewById(R.id.genderDropdown);
        courseDropdown = findViewById(R.id.courseDropdown);
        yearDropdown = findViewById(R.id.yearDropdown);
        areaDropdown = findViewById(R.id.areaDropdown);
        drinkingDropdown = findViewById(R.id.drinkingDropdown);
        smokingDropdown = findViewById(R.id.smokingDropdown);

        ageEt = findViewById(R.id.ageEt);
        additionalInfoEt = findViewById(R.id.additionalInfoEt);

        budgetSlider = findViewById(R.id.budgetSlider);
        budgetRangeTv = findViewById(R.id.budgetRangeTv);
        roommateAgeSlider = findViewById(R.id.roommateAgeSlider);
        roommateAgeTv = findViewById(R.id.roommateAgeTv);

        roommateGenderChipGroup = findViewById(R.id.roommateGenderChipGroup);

        profilePicture = findViewById(R.id.profilePicture);
        profilePictureEdit = findViewById(R.id.profilePictureEdit);
        profileProgressTv = findViewById(R.id.profileProgressTv);
        profileProgressBar = findViewById(R.id.profileProgressBar);
    }

    // Setup
    private void setupDropdown(TextInputLayout layout, MaterialAutoCompleteTextView dropdown,
                               String[] options) {
        dropdown.setAdapter(new ArrayAdapter<>(
                this, android.R.layout.simple_list_item_1, options));
        clearErrorOnEdit(layout, dropdown);
    }

    private void setupBudgetSlider() {
        budgetSlider.setValues(6000f, 10000f); // otherwise both thumbs sit at the far left
        updateBudgetLabel();
        budgetSlider.addOnChangeListener((slider, value, fromUser) -> updateBudgetLabel());
    }

    private void updateBudgetLabel() {
        List<Float> v = budgetSlider.getValues();
        budgetRangeTv.setText("₱" + moneyFormat.format(Math.round(v.get(0)))
                + "–₱" + moneyFormat.format(Math.round(v.get(1))));
    }

    private void setupRoommateAgeSlider() {
        roommateAgeSlider.setValues(18f, 24f);
        updateRoommateAgeLabel();
        roommateAgeSlider.addOnChangeListener((slider, value, fromUser) -> updateRoommateAgeLabel());
    }

    private void updateRoommateAgeLabel() {
        List<Float> v = roommateAgeSlider.getValues();
        roommateAgeTv.setText(Math.round(v.get(0)) + "–" + Math.round(v.get(1)));
    }

    private void clearErrorOnEdit(TextInputLayout layout, TextView field) {
        field.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int st, int c, int a) {}
            @Override public void onTextChanged(CharSequence s, int st, int b, int c) {}
            @Override public void afterTextChanged(Editable s) {
                layout.setError(null);
                updateProgress();
            }
        });
    }

    private void setupProfilePhoto() {
        // Placeholders until real profile data is loaded
        profilePicture.setImageResource(R.drawable.catpfp);
        ((TextView) findViewById(R.id.nameTv)).setText("Maya Santos");

        View.OnClickListener pickPhoto = v -> photoPicker.launch(
                new PickVisualMediaRequest.Builder()
                        .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                        .build());
        profilePictureEdit.setOnClickListener(pickPhoto); // camera badge
        profilePicture.setOnClickListener(pickPhoto);     // tapping the photo also works
    }

    // profile completeness = filled-in items / total items (8 fields + the photo).
    private void updateProgress() {
        TextView[] fields = {genderDropdown, ageEt, courseDropdown, yearDropdown,
                areaDropdown, drinkingDropdown, smokingDropdown, additionalInfoEt};

        int filled = 0;
        for (TextView f : fields) {
            if (!f.getText().toString().trim().isEmpty()) filled++;
        }
        if (selectedPhotoUri != null) filled++;

        int percent = Math.round(filled * 100f / (fields.length + 1));
        profileProgressBar.setProgressCompat(percent, true);
        profileProgressTv.setText("Profile " + percent + "% complete");
    }

    // Validation
    private void onSavePressed() {
        if (!validateForm()) return;

        // Values to save:
        //   drinking / smoking:   drinkingDropdown / smokingDropdown text
        //   roommate gender:      roommateGenderChipGroup.getCheckedChipId() -> chipMan / chipWoman / chipAnyGender
        //   roommate age range:   roommateAgeSlider.getValues()
        // TODO: save the profile for mco3
        Toast.makeText(this, "Profile updated!", Toast.LENGTH_SHORT).show();
        finish();
    }

    private boolean validateForm() {
        firstInvalid = null;
        boolean valid = true;

        // Dropdowns: just make sure something was picked
        valid &= requireSelection(genderLayout, genderDropdown, "Please select your gender");
        valid &= validateAge();
        valid &= requireSelection(courseLayout, courseDropdown, "Please select your course");
        valid &= requireSelection(yearLayout, yearDropdown, "Select year level");
        valid &= requireSelection(areaLayout, areaDropdown, "Please select a preferred area");
        valid &= requireSelection(drinkingLayout, drinkingDropdown, "Select one");
        valid &= requireSelection(smokingLayout, smokingDropdown, "Select one");

        // Roommate gender: one chip must be selected
        if (roommateGenderChipGroup.getCheckedChipId() == View.NO_ID) {
            Toast.makeText(this, "Select a roommate gender preference", Toast.LENGTH_SHORT).show();
            if (firstInvalid == null) firstInvalid = roommateGenderChipGroup;
            valid = false;
        }

        // Roommate age range: guard against values outside the allowed span
        List<Float> ages = roommateAgeSlider.getValues();
        if (ages.get(0) < ROOMMATE_AGE_FROM || ages.get(1) > ROOMMATE_AGE_TO
                || ages.get(0) > ages.get(1)) {
            Toast.makeText(this, "Choose a valid roommate age range", Toast.LENGTH_SHORT).show();
            if (firstInvalid == null) firstInvalid = roommateAgeSlider;
            valid = false;
        }

        // Additional information (optional, length cap only)
        String info = additionalInfoEt.getText().toString().trim();
        if (info.length() > MAX_INFO_LENGTH) {
            valid &= fail(additionalInfoLayout, additionalInfoEt,
                    "Keep this under " + MAX_INFO_LENGTH + " characters (currently " + info.length() + ")");
        }

        if (firstInvalid != null) firstInvalid.requestFocus();
        return valid;
    }

    private boolean validateAge() {
        String age = ageEt.getText().toString().trim();

        if (age.isEmpty()) {
            return fail(ageLayout, ageEt, "Age is required");
        }
        if (!age.matches("\\d{1,3}")) {
            return fail(ageLayout, ageEt, "Age must be a whole number");
        }
        int value = Integer.parseInt(age);
        if (value < MIN_AGE || value > MAX_AGE) {
            return fail(ageLayout, ageEt, "Age must be between " + MIN_AGE + " and " + MAX_AGE);
        }
        return true;
    }

    private boolean requireSelection(TextInputLayout layout, TextView field, String message) {
        if (field.getText().toString().trim().isEmpty()) {
            return fail(layout, field, message);
        }
        return true;
    }

    // Shows the error on the field, remembers the first bad field, returns false.
    private boolean fail(TextInputLayout layout, View field, String message) {
        layout.setError(message);
        if (firstInvalid == null) firstInvalid = field;
        return false;
    }
}