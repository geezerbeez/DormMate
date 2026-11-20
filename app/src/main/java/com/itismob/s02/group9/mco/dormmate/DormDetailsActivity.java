package com.itismob.s02.group9.mco.dormmate;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.List;

/** View Dormitory Details: photos, rates, amenities, rules, commute, nearby spots, contact. */
public class DormDetailsActivity extends AppCompatActivity {

    public static final String EXTRA_DORM_ID = "dorm_id";

    private Dorm dorm;
    private ImageButton heartBtn;
    private Button saveBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dorm_details);

        // Only pad the sides/bottom so the photo can reach the top of the screen.
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(bars.left, 0, bars.right, bars.bottom);
            findViewById(R.id.backBtn).setTranslationY(bars.top);
            ((View) findViewById(R.id.shareBtn).getParent()).setTranslationY(bars.top);
            return insets;
        });

        String id = getIntent().getStringExtra(EXTRA_DORM_ID);
        dorm = id == null ? null : DormRepository.getInstance().getById(id);
        if (dorm == null) {
            Toast.makeText(this, "This listing is no longer available.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        heartBtn = findViewById(R.id.heartBtn);
        saveBtn = findViewById(R.id.saveBtn);

        bindGallery();
        bindDetails();
        bindActions();
    }

    // ---- gallery -----------------------------------------------------------------------

    private void bindGallery() {
        RecyclerView gallery = findViewById(R.id.galleryRecycler);
        TextView counter = findViewById(R.id.photoCounterTv);
        List<Integer> photos = dorm.getPhotos();

        gallery.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        gallery.setAdapter(new GalleryAdapter(photos));
        PagerSnapHelper snap = new PagerSnapHelper();
        snap.attachToRecyclerView(gallery);

        counter.setText("1 / " + Math.max(1, photos.size()));
        gallery.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView rv, int dx, int dy) {
                View snapView = snap.findSnapView(rv.getLayoutManager());
                if (snapView != null) {
                    int pos = rv.getChildAdapterPosition(snapView);
                    counter.setText((pos + 1) + " / " + photos.size());
                }
            }
        });
    }

    private static class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.VH> {
        private final List<Integer> photos;

        GalleryAdapter(List<Integer> photos) { this.photos = photos; }

        @NonNull
        @Override
        public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            ImageView iv = (ImageView) LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_gallery_photo, parent, false);
            return new VH(iv);
        }

        @Override
        public void onBindViewHolder(@NonNull VH h, int position) {
            ((ImageView) h.itemView).setImageResource(photos.get(position));
        }

        @Override
        public int getItemCount() { return photos.size(); }

        static class VH extends RecyclerView.ViewHolder {
            VH(@NonNull View v) { super(v); }
        }
    }

    // ---- details -----------------------------------------------------------------------

    private void bindDetails() {
        ((TextView) findViewById(R.id.detailNameTv)).setText(dorm.getName());
        ((TextView) findViewById(R.id.detailPriceTv)).setText(dorm.getPriceLabel());
        ((TextView) findViewById(R.id.detailAddressTv)).setText(dorm.getAddress());

        ((TextView) findViewById(R.id.factRoommatesTv)).setText(
                dorm.getRoommates() + (dorm.getRoommates() == 1 ? " Roommate" : " Roommates"));
        ((TextView) findViewById(R.id.factBedsTv)).setText(
                dorm.getBeds() + (dorm.getBeds() == 1 ? " Bed" : " Beds"));
        ((TextView) findViewById(R.id.factBathsTv)).setText(
                dorm.getBaths() + (dorm.getBaths() == 1 ? " Bath" : " Baths"));

        TextView vacancy = findViewById(R.id.vacancyTv);
        if (dorm.getAvailableSlots() > 0) {
            vacancy.setText("Vacancy: " + dorm.getAvailableSlots() + " of " + dorm.getTotalSlots() + " available");
            vacancy.setBackgroundResource(R.drawable.bg_status_active);
            vacancy.setTextColor(getColor(R.color.statusGreen));
        } else {
            vacancy.setText("Fully occupied");
            vacancy.setBackgroundResource(R.drawable.bg_status_full);
            vacancy.setTextColor(getColor(R.color.statusOrange));
        }

        ((TextView) findViewById(R.id.depositTv)).setText(Dorm.formatPeso(dorm.getDeposit()));
        int adv = dorm.getAdvanceMonths();
        ((TextView) findViewById(R.id.advanceTv)).setText(adv + (adv == 1 ? " month" : " months"));
        ((TextView) findViewById(R.id.leaseTv)).setText(dorm.getLease());
        ((TextView) findViewById(R.id.descriptionTv)).setText(dorm.getDescription());

        // Amenities
        ChipGroup amenities = findViewById(R.id.amenitiesGroup);
        for (String a : dorm.getAmenities()) {
            Chip chip = (Chip) getLayoutInflater().inflate(R.layout.item_chip_info, amenities, false);
            chip.setText(a);
            amenities.addView(chip);
        }

        // House rules as bullet lines
        StringBuilder rules = new StringBuilder();
        for (String r : dorm.getHouseRules()) {
            if (rules.length() > 0) rules.append("\n");
            rules.append("\u2022  ").append(r);
        }
        TextView rulesTv = findViewById(R.id.rulesTv);
        rulesTv.setText(rules.length() == 0 ? "No house rules listed." : rules.toString());

        // Commute
        ((TextView) findViewById(R.id.walkTv)).setText(dorm.getWalkTime());
        ((TextView) findViewById(R.id.transitTv)).setText(dorm.getTransitTime());
        ((TextView) findViewById(R.id.driveTv)).setText(dorm.getDriveTime());

        // Nearby spots
        LinearLayout spots = findViewById(R.id.spotsContainer);
        for (Dorm.Spot s : dorm.getNearbySpots()) {
            View row = getLayoutInflater().inflate(R.layout.item_spot, spots, false);
            ((TextView) row.findViewById(R.id.spotNameTv)).setText(s.name);
            ((TextView) row.findViewById(R.id.spotDistanceTv)).setText(s.distance);
            spots.addView(row);
        }

        // Contact
        ((TextView) findViewById(R.id.contactNameTv)).setText(dorm.getContactName());
        ((TextView) findViewById(R.id.contactPhoneTv)).setText(dorm.getContactPhone());
        ((TextView) findViewById(R.id.contactEmailTv)).setText(dorm.getContactEmail());
    }

    // ---- buttons -----------------------------------------------------------------------

    private void bindActions() {
        findViewById(R.id.backBtn).setOnClickListener(v -> finish());

        updateSaved();
        View.OnClickListener toggleSave = v -> {
            dorm.setSaved(!dorm.isSaved());
            updateSaved();
            Toast.makeText(this, dorm.isSaved() ? "Saved to your list" : "Removed from saved",
                    Toast.LENGTH_SHORT).show();
        };
        heartBtn.setOnClickListener(toggleSave);
        saveBtn.setOnClickListener(toggleSave);

        findViewById(R.id.shareBtn).setOnClickListener(v -> {
            Intent share = new Intent(Intent.ACTION_SEND);
            share.setType("text/plain");
            share.putExtra(Intent.EXTRA_TEXT, dorm.getName() + " - " + dorm.getPriceLabel()
                    + "/month\n" + dorm.getAddress() + "\nFound on DormMate");
            startActivity(Intent.createChooser(share, "Share listing"));
        });

        findViewById(R.id.contactBtn).setOnClickListener(v -> showContactDialog());
    }

    private void updateSaved() {
        heartBtn.setImageResource(dorm.isSaved() ? R.drawable.ic_favorite : R.drawable.ic_favorite_border);
        saveBtn.setText(dorm.isSaved() ? "Saved" : "Save");
    }

    private void showContactDialog() {
        String msg = dorm.getContactName() + "\n" + dorm.getContactPhone() + "\n" + dorm.getContactEmail();
        new MaterialAlertDialogBuilder(this)
                .setTitle("Contact Landlord")
                .setMessage(msg)
                .setPositiveButton("Call", (d, w) -> {
                    Intent dial = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + dorm.getContactPhone()));
                    try {
                        startActivity(dial);
                    } catch (Exception e) {
                        Toast.makeText(this, "No dialer app found", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNeutralButton("Email", (d, w) -> {
                    Intent mail = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + dorm.getContactEmail()));
                    mail.putExtra(Intent.EXTRA_SUBJECT, "Inquiry about " + dorm.getName());
                    try {
                        startActivity(mail);
                    } catch (Exception e) {
                        Toast.makeText(this, "No email app found", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Close", null)
                .show();
    }
}
