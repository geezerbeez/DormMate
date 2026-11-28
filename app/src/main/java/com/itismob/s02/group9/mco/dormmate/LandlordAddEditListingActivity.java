package com.itismob.s02.group9.mco.dormmate;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Add / Edit Dormitory Listing (dorm-owner side).
 * Launch without extras to add; pass EXTRA_DORM_ID to edit an existing listing.
 */
public class LandlordAddEditListingActivity extends AppCompatActivity {

    public static final String EXTRA_DORM_ID = "dorm_id";

    /** Placeholder photos used until the image picker / Firebase Storage is hooked up. */
    private static final int[] SAMPLE_PHOTOS = {
            R.drawable.dorm_photo_1, R.drawable.dorm_photo_2, R.drawable.dorm_photo_3,
            R.drawable.dorm_photo_4, R.drawable.dorm_photo_5, R.drawable.dorm_photo_6,
            R.drawable.dorm_photo_7
    };
    private static final int MAX_PHOTOS = 6;

    private Dorm editing;                       // null when adding
    private final List<Integer> photos = new ArrayList<>();
    private int nextSample = 0;

    private LinearLayout photoStrip;
    private ChipGroup amenityGroup;
    private MaterialAutoCompleteTextView typeActv;

    private TextInputLayout nameTil, typeTil, addressTil, descTil, priceTil, depositTil, advanceTil,
            leaseTil, totalTil, availTil, rulesTil, contactNameTil, phoneTil, emailTil;
    private TextInputEditText nameEtv, addressEtv, descEtv, priceEtv, depositEtv, advanceEtv,
            leaseEtv, totalEtv, availEtv, rulesEtv, contactNameEtv, phoneEtv, emailEtv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_landlord_add_edit_listing);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            Insets ime = insets.getInsets(WindowInsetsCompat.Type.ime());
            v.setPadding(bars.left, bars.top, bars.right, Math.max(bars.bottom, ime.bottom));
            return insets;
        });

        bindViews();

        String id = getIntent().getStringExtra(EXTRA_DORM_ID);
        if (id != null) editing = DormRepository.getInstance().getById(id);

        // Room type dropdown
        typeActv.setSimpleItems(new String[]{Dorm.TYPE_SOLO, Dorm.TYPE_SHARED, Dorm.TYPE_BEDSPACE});

        // Amenity chips
        for (String a : DormRepository.ALL_AMENITIES) {
            Chip chip = (Chip) getLayoutInflater().inflate(R.layout.item_chip_filter, amenityGroup, false);
            chip.setText(a);
            amenityGroup.addView(chip);
        }

        if (editing != null) {
            ((TextView) findViewById(R.id.formTitleTv)).setText("Edit Listing");
            prefill(editing);
        } else {
            typeActv.setText(Dorm.TYPE_SOLO, false);
            photos.add(SAMPLE_PHOTOS[0]);
            nextSample = 1;
        }
        renderPhotos();

        findViewById(R.id.backBtn).setOnClickListener(v -> finish());
        findViewById(R.id.cancelBtn).setOnClickListener(v -> finish());
        findViewById(R.id.saveListingBtn).setOnClickListener(v -> save());
    }

    private void bindViews() {
        photoStrip = findViewById(R.id.photoStrip);
        amenityGroup = findViewById(R.id.amenityGroup);
        typeActv = findViewById(R.id.typeEtv);

        nameTil = findViewById(R.id.nameTil);       nameEtv = findViewById(R.id.nameEtv);
        typeTil = findViewById(R.id.typeTil);
        addressTil = findViewById(R.id.addressTil); addressEtv = findViewById(R.id.addressEtv);
        descTil = findViewById(R.id.descTil);       descEtv = findViewById(R.id.descEtv);
        priceTil = findViewById(R.id.priceTil);     priceEtv = findViewById(R.id.priceEtv);
        depositTil = findViewById(R.id.depositTil); depositEtv = findViewById(R.id.depositEtv);
        advanceTil = findViewById(R.id.advanceTil); advanceEtv = findViewById(R.id.advanceEtv);
        leaseTil = findViewById(R.id.leaseTil);     leaseEtv = findViewById(R.id.leaseEtv);
        totalTil = findViewById(R.id.totalTil);     totalEtv = findViewById(R.id.totalEtv);
        availTil = findViewById(R.id.availTil);     availEtv = findViewById(R.id.availEtv);
        rulesTil = findViewById(R.id.rulesTil);     rulesEtv = findViewById(R.id.rulesEtv);
        contactNameTil = findViewById(R.id.contactNameTil); contactNameEtv = findViewById(R.id.contactNameEtv);
        phoneTil = findViewById(R.id.phoneTil);     phoneEtv = findViewById(R.id.phoneEtv);
        emailTil = findViewById(R.id.emailTil);     emailEtv = findViewById(R.id.emailEtv);
    }

    // ---- prefill (edit mode) -----------------------------------------------------------

    private void prefill(Dorm d) {
        nameEtv.setText(d.getName());
        typeActv.setText(d.getRoomType(), false);
        addressEtv.setText(d.getAddress());
        descEtv.setText(d.getDescription());
        priceEtv.setText(String.valueOf(d.getPrice()));
        depositEtv.setText(String.valueOf(d.getDeposit()));
        advanceEtv.setText(String.valueOf(d.getAdvanceMonths()));
        leaseEtv.setText(d.getLease());
        totalEtv.setText(String.valueOf(d.getTotalSlots()));
        availEtv.setText(String.valueOf(d.getAvailableSlots()));
        contactNameEtv.setText(d.getContactName());
        phoneEtv.setText(d.getContactPhone());
        emailEtv.setText(d.getContactEmail());

        StringBuilder rules = new StringBuilder();
        for (String r : d.getHouseRules()) {
            if (rules.length() > 0) rules.append("\n");
            rules.append(r);
        }
        rulesEtv.setText(rules.toString());

        for (int i = 0; i < amenityGroup.getChildCount(); i++) {
            Chip c = (Chip) amenityGroup.getChildAt(i);
            c.setChecked(d.getAmenities().contains(c.getText().toString()));
        }
        photos.clear();
        photos.addAll(d.getPhotos());
        nextSample = photos.size() % SAMPLE_PHOTOS.length;
    }

    // ---- photos ------------------------------------------------------------------------

    private void renderPhotos() {
        photoStrip.removeAllViews();
        for (int i = 0; i < photos.size(); i++) {
            final int index = i;
            View tile = getLayoutInflater().inflate(R.layout.item_photo_tile, photoStrip, false);
            ((ImageView) tile.findViewById(R.id.tilePhotoIv)).setImageResource(photos.get(i));
            tile.findViewById(R.id.tileRemoveBtn).setOnClickListener(v -> {
                photos.remove(index);
                renderPhotos();
            });
            photoStrip.addView(tile);
        }
        if (photos.size() < MAX_PHOTOS) {
            View add = getLayoutInflater().inflate(R.layout.item_photo_add, photoStrip, false);
            add.setOnClickListener(v -> {
                // TODO: replace with the system photo picker + Firebase Storage upload.
                photos.add(SAMPLE_PHOTOS[nextSample % SAMPLE_PHOTOS.length]);
                nextSample++;
                renderPhotos();
            });
            photoStrip.addView(add);
        }
    }

    // ---- validation + save -------------------------------------------------------------

    private String text(TextInputEditText e) {
        return e.getText() == null ? "" : e.getText().toString().trim();
    }

    /** Returns the number, or -1 when empty / not a number. */
    private int number(TextInputEditText e) {
        String s = text(e);
        if (s.isEmpty()) return -1;
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException ex) {
            return -1;
        }
    }

    private void clearErrors() {
        for (TextInputLayout t : Arrays.asList(nameTil, typeTil, addressTil, descTil, priceTil, depositTil,
                advanceTil, leaseTil, totalTil, availTil, rulesTil, contactNameTil, phoneTil, emailTil)) {
            t.setError(null);
        }
    }

    private boolean validate() {
        clearErrors();
        boolean ok = true;
        View firstBad = null;

        if (text(nameEtv).isEmpty()) { nameTil.setError("Listing name is required."); ok = false; firstBad = nameTil; }
        String type = typeActv.getText() == null ? "" : typeActv.getText().toString();
        if (type.isEmpty()) { typeTil.setError("Choose a room type."); ok = false; if (firstBad == null) firstBad = typeTil; }
        if (text(addressEtv).isEmpty()) { addressTil.setError("Address is required."); ok = false; if (firstBad == null) firstBad = addressTil; }
        if (text(descEtv).isEmpty()) { descTil.setError("Please add a short description."); ok = false; if (firstBad == null) firstBad = descTil; }

        if (number(priceEtv) <= 0) { priceTil.setError("Enter a monthly rate greater than 0 (numbers only)."); ok = false; if (firstBad == null) firstBad = priceTil; }
        if (number(depositEtv) < 0) { depositTil.setError("Enter the deposit (0 if none)."); ok = false; if (firstBad == null) firstBad = depositTil; }
        if (number(advanceEtv) < 0) { advanceTil.setError("Enter advance months (0 if none)."); ok = false; if (firstBad == null) firstBad = advanceTil; }
        if (text(leaseEtv).isEmpty()) { leaseTil.setError("Lease length is required."); ok = false; if (firstBad == null) firstBad = leaseTil; }

        int total = number(totalEtv);
        int avail = number(availEtv);
        if (total <= 0) { totalTil.setError("Total slots must be at least 1."); ok = false; if (firstBad == null) firstBad = totalTil; }
        if (avail < 0) { availTil.setError("Enter available slots (0 if full)."); ok = false; if (firstBad == null) firstBad = availTil; }
        else if (total > 0 && avail > total) { availTil.setError("Cannot be more than total slots."); ok = false; if (firstBad == null) firstBad = availTil; }

        if (text(contactNameEtv).isEmpty()) { contactNameTil.setError("Contact person is required."); ok = false; if (firstBad == null) firstBad = contactNameTil; }
        String phone = text(phoneEtv);
        if (phone.isEmpty()) { phoneTil.setError("Mobile number is required."); ok = false; if (firstBad == null) firstBad = phoneTil; }
        else if (!phone.matches("^09\\d{9}$")) { phoneTil.setError("Use an 11-digit number starting with 09."); ok = false; if (firstBad == null) firstBad = phoneTil; }
        String email = text(emailEtv);
        if (email.isEmpty()) { emailTil.setError("Email is required."); ok = false; if (firstBad == null) firstBad = emailTil; }
        else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) { emailTil.setError("Please enter a valid email address."); ok = false; if (firstBad == null) firstBad = emailTil; }

        if (photos.isEmpty()) {
            Toast.makeText(this, "Add at least one photo.", Toast.LENGTH_SHORT).show();
            ok = false;
        }
        if (firstBad != null) firstBad.requestFocus();
        return ok;
    }

    private void save() {
        if (!validate()) return;

        Dorm d = editing != null ? editing : new Dorm();
        d.setName(text(nameEtv));
        d.setRoomType(typeActv.getText().toString());
        d.setAddress(text(addressEtv));
        d.setDescription(text(descEtv));
        d.setPrice(number(priceEtv));
        d.setDeposit(number(depositEtv));
        d.setAdvanceMonths(number(advanceEtv));
        d.setLease(text(leaseEtv));
        d.setTotalSlots(number(totalEtv));
        d.setAvailableSlots(number(availEtv));
        d.setContact(text(contactNameEtv), text(phoneEtv), text(emailEtv));

        List<String> amenities = new ArrayList<>();
        for (int i = 0; i < amenityGroup.getChildCount(); i++) {
            Chip c = (Chip) amenityGroup.getChildAt(i);
            if (c.isChecked()) amenities.add(c.getText().toString());
        }
        d.setAmenities(amenities);

        List<String> rules = new ArrayList<>();
        for (String line : text(rulesEtv).split("\n")) {
            if (!line.trim().isEmpty()) rules.add(line.trim());
        }
        d.setHouseRules(rules);
        d.setPhotos(new ArrayList<>(photos));

        // Keep status in sync with vacancy (but never un-hide a hidden listing)
        if (d.getStatus() != Dorm.Status.HIDDEN) {
            d.setStatus(d.getAvailableSlots() > 0 ? Dorm.Status.ACTIVE : Dorm.Status.FULL);
        }

        if (editing == null) {
            // Distance / commute come from the Places + Routes APIs later; placeholder for now.
            d.setDistanceKm(1.0);
            d.setWalkTime("-");
            d.setTransitTime("-");
            d.setDriveTime("-");
            DormRepository.getInstance().add(d);
        }

        Toast.makeText(this, editing == null ? "Listing added" : "Listing updated", Toast.LENGTH_SHORT).show();
        finish();
    }
}
