package com.itismob.s02.group9.mco.dormmate;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.chip.ChipGroup;

import java.util.List;

/** Explore tab: search + filter + list/map of dormitory listings. */
public class ExploreFragment extends Fragment implements FilterBottomSheet.FilterListener {

    private DormFilter filter = new DormFilter();
    private DormAdapter adapter;

    private RecyclerView recycler;
    private View mapContainer, emptyState;
    private TextView resultCountTv, filterBadgeTv;
    private ChipGroup typeChipGroup;
    private boolean syncingChips = false;

    public ExploreFragment() {
        super(R.layout.fragment_explore);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recycler = view.findViewById(R.id.dormRecycler);
        mapContainer = view.findViewById(R.id.mapContainer);
        emptyState = view.findViewById(R.id.emptyState);
        resultCountTv = view.findViewById(R.id.resultCountTv);
        filterBadgeTv = view.findViewById(R.id.filterBadgeTv);
        typeChipGroup = view.findViewById(R.id.typeChipGroup);
        EditText searchEtv = view.findViewById(R.id.searchEtv);
        ImageButton filterBtn = view.findViewById(R.id.filterBtn);
        MaterialButtonToggleGroup viewToggle = view.findViewById(R.id.viewToggle);
        Button clearBtn = view.findViewById(R.id.clearFiltersBtn);

        adapter = new DormAdapter(dorm -> {
            Intent i = new Intent(requireContext(), DormDetailsActivity.class);
            i.putExtra(DormDetailsActivity.EXTRA_DORM_ID, dorm.getId());
            startActivity(i);
        });
        recycler.setLayoutManager(new LinearLayoutManager(requireContext()));
        recycler.setAdapter(adapter);

        // Search as you type
        searchEtv.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int st, int c, int a) { }
            @Override public void onTextChanged(CharSequence s, int st, int b, int c) { }
            @Override public void afterTextChanged(Editable s) {
                filter.query = s.toString();
                refresh();
            }
        });

        // Quick room type chips
        viewToggle.check(R.id.listViewBtn);
        typeChipGroup.setOnCheckedStateChangeListener((group, checkedIds) -> {
            if (syncingChips) return;
            int id = checkedIds.isEmpty() ? R.id.chipAll : checkedIds.get(0);
            if (id == R.id.chipSolo) filter.roomType = Dorm.TYPE_SOLO;
            else if (id == R.id.chipShared) filter.roomType = Dorm.TYPE_SHARED;
            else if (id == R.id.chipBedspace) filter.roomType = Dorm.TYPE_BEDSPACE;
            else filter.roomType = null;
            refresh();
        });

        // Filter button -> bottom sheet
        filterBtn.setOnClickListener(v ->
                FilterBottomSheet.newInstance(filter).show(getChildFragmentManager(), "filter"));

        // List / Map toggle (map is a placeholder until Google Maps is added)
        viewToggle.addOnButtonCheckedListener((group, checkedId, isChecked) -> {
            if (!isChecked) return;
            boolean showMap = checkedId == R.id.mapViewBtn;
            mapContainer.setVisibility(showMap ? View.VISIBLE : View.GONE);
            refresh();
        });

        clearBtn.setOnClickListener(v -> {
            String q = filter.query;
            filter = new DormFilter();
            filter.query = q;
            syncChips();
            refresh();
        });

        refresh();
    }

    @Override
    public void onResume() {
        super.onResume();
        // Pick up changes made elsewhere (saved hearts, landlord edits)
        if (adapter != null) refresh();
    }

    @Override
    public void onFilterApplied(DormFilter newFilter) {
        newFilter.query = filter.query; // keep the current search text
        filter = newFilter;
        syncChips();
        refresh();
    }

    private void syncChips() {
        syncingChips = true;
        int id = R.id.chipAll;
        if (Dorm.TYPE_SOLO.equals(filter.roomType)) id = R.id.chipSolo;
        else if (Dorm.TYPE_SHARED.equals(filter.roomType)) id = R.id.chipShared;
        else if (Dorm.TYPE_BEDSPACE.equals(filter.roomType)) id = R.id.chipBedspace;
        typeChipGroup.check(id);
        syncingChips = false;
    }

    private void refresh() {
        List<Dorm> results = DormRepository.getInstance().search(filter);
        adapter.submit(results);

        resultCountTv.setText(results.size() + (results.size() == 1 ? " listing" : " listings"));

        int count = filter.activeCount();
        filterBadgeTv.setVisibility(count > 0 ? View.VISIBLE : View.GONE);
        filterBadgeTv.setText(String.valueOf(count));

        boolean mapShown = mapContainer.getVisibility() == View.VISIBLE;
        boolean empty = results.isEmpty();
        emptyState.setVisibility(empty && !mapShown ? View.VISIBLE : View.GONE);
        recycler.setVisibility(empty || mapShown ? View.GONE : View.VISIBLE);
    }
}
