package com.itismob.s02.group9.mco.dormmate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.ChipGroup;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.util.ArrayList;
import java.util.List;

/** Dorm-owner home: Manage Dormitory Listings (view, edit, hide, mark full, delete, add). */
public class LandlordDashboardActivity extends AppCompatActivity implements LandlordListingAdapter.Actions {

    private LandlordListingAdapter adapter;
    private RecyclerView recycler;
    private View emptyState;
    private ChipGroup statusChipGroup;
    private TextView statListings, statVacant, statInquiries;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_landlord_dashboard);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        recycler = findViewById(R.id.listingRecycler);
        emptyState = findViewById(R.id.emptyState);
        statusChipGroup = findViewById(R.id.statusChipGroup);
        statListings = findViewById(R.id.statListingsTv);
        statVacant = findViewById(R.id.statVacantTv);
        statInquiries = findViewById(R.id.statInquiriesTv);
        ExtendedFloatingActionButton fab = findViewById(R.id.addListingFab);

        adapter = new LandlordListingAdapter(this);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.setAdapter(adapter);

        statusChipGroup.setOnCheckedStateChangeListener((group, ids) -> refresh());

        fab.setOnClickListener(v ->
                startActivity(new Intent(this, LandlordAddEditListingActivity.class)));

        findViewById(R.id.exitBtn).setOnClickListener(v -> finish());
    }

    @Override
    protected void onResume() {
        super.onResume();
        refresh(); // pick up anything added/edited on the form screen
    }

    private void refresh() {
        List<Dorm> all = DormRepository.getInstance().getAll();

        // Summary numbers
        int slots = 0, inquiries = 0;
        for (Dorm d : all) {
            if (d.getStatus() == Dorm.Status.ACTIVE) slots += d.getAvailableSlots();
            inquiries += d.getInquiries();
        }
        statListings.setText(String.valueOf(all.size()));
        statVacant.setText(String.valueOf(slots));
        statInquiries.setText(String.valueOf(inquiries));

        // Status filter
        int checked = statusChipGroup.getCheckedChipId();
        List<Dorm> shown = new ArrayList<>();
        for (Dorm d : all) {
            if (checked == R.id.chipStatusActive && d.getStatus() != Dorm.Status.ACTIVE) continue;
            if (checked == R.id.chipStatusFull && d.getStatus() != Dorm.Status.FULL) continue;
            if (checked == R.id.chipStatusHidden && d.getStatus() != Dorm.Status.HIDDEN) continue;
            shown.add(d);
        }
        adapter.submit(shown);
        emptyState.setVisibility(shown.isEmpty() ? View.VISIBLE : View.GONE);
        recycler.setVisibility(shown.isEmpty() ? View.GONE : View.VISIBLE);
    }

    // ---- row actions -------------------------------------------------------------------

    @Override
    public void onPreview(Dorm dorm) {
        Intent i = new Intent(this, DormDetailsActivity.class);
        i.putExtra(DormDetailsActivity.EXTRA_DORM_ID, dorm.getId());
        startActivity(i);
    }

    @Override
    public void onEdit(Dorm dorm) {
        Intent i = new Intent(this, LandlordAddEditListingActivity.class);
        i.putExtra(LandlordAddEditListingActivity.EXTRA_DORM_ID, dorm.getId());
        startActivity(i);
    }

    @Override
    public void onToggleVisibility(Dorm dorm) {
        if (dorm.getStatus() == Dorm.Status.HIDDEN) {
            dorm.setStatus(dorm.getAvailableSlots() > 0 ? Dorm.Status.ACTIVE : Dorm.Status.FULL);
            Toast.makeText(this, "Listing is now visible to students", Toast.LENGTH_SHORT).show();
        } else {
            dorm.setStatus(Dorm.Status.HIDDEN);
            Toast.makeText(this, "Listing hidden from Explore", Toast.LENGTH_SHORT).show();
        }
        refresh();
    }

    @Override
    public void onToggleFull(Dorm dorm) {
        if (dorm.getStatus() == Dorm.Status.FULL) {
            dorm.setStatus(Dorm.Status.ACTIVE);
            if (dorm.getAvailableSlots() == 0) dorm.setAvailableSlots(1);
        } else {
            dorm.setStatus(Dorm.Status.FULL);
            dorm.setAvailableSlots(0);
        }
        refresh();
    }

    @Override
    public void onDelete(Dorm dorm) {
        new MaterialAlertDialogBuilder(this)
                .setTitle("Delete listing?")
                .setMessage("\"" + dorm.getName() + "\" will be removed permanently.")
                .setPositiveButton("Delete", (d, w) -> {
                    DormRepository.getInstance().delete(dorm);
                    Toast.makeText(this, "Listing deleted", Toast.LENGTH_SHORT).show();
                    refresh();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}
