package com.itismob.s02.group9.mco.dormmate;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.slider.RangeSlider;

import java.util.List;

/** "Filter dormitories" sheet: price range, room type, amenities. */
public class ExploreFilterBottomSheet extends BottomSheetDialogFragment {

    public interface FilterListener {
        void onFilterApplied(DormFilter filter);
    }

    private static final String ARG_FILTER = "filter";

    private RangeSlider priceSlider;
    private TextView priceRangeTv;
    private ChipGroup roomTypeGroup, amenityGroup;

    public static ExploreFilterBottomSheet newInstance(DormFilter current) {
        ExploreFilterBottomSheet f = new ExploreFilterBottomSheet();
        Bundle b = new Bundle();
        b.putSerializable(ARG_FILTER, current);
        f.setArguments(b);
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.bottom_sheet_filter_dormitory, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View v, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);

        DormFilter current = null;
        if (getArguments() != null) {
            current = (DormFilter) getArguments().getSerializable(ARG_FILTER);
        }
        if (current == null) current = new DormFilter();

        priceSlider = v.findViewById(R.id.priceSlider);
        priceRangeTv = v.findViewById(R.id.priceRangeTv);
        roomTypeGroup = v.findViewById(R.id.roomTypeGroup);
        amenityGroup = v.findViewById(R.id.amenityGroup);
        TextView resetTv = v.findViewById(R.id.filterResetTv);
        Button applyBtn = v.findViewById(R.id.applyFilterBtn);

        // Price slider
        priceSlider.setValues((float) current.minPrice, (float) current.maxPrice);
        updatePriceLabel();
        priceSlider.addOnChangeListener((slider, value, fromUser) -> updatePriceLabel());

        // Room type
        int typeId = R.id.fChipAny;
        if (Dorm.TYPE_SOLO.equals(current.roomType)) typeId = R.id.fChipSolo;
        else if (Dorm.TYPE_SHARED.equals(current.roomType)) typeId = R.id.fChipShared;
        else if (Dorm.TYPE_BEDSPACE.equals(current.roomType)) typeId = R.id.fChipBedspace;
        roomTypeGroup.check(typeId);

        // Amenity chips
        for (String amenity : DormRepository.ALL_AMENITIES) {
            Chip chip = (Chip) getLayoutInflater().inflate(R.layout.item_chip_filter, amenityGroup, false);
            chip.setText(amenity);
            chip.setChecked(current.amenities.contains(amenity));
            amenityGroup.addView(chip);
        }

        resetTv.setOnClickListener(x -> {
            priceSlider.setValues((float) DormFilter.PRICE_MIN, (float) DormFilter.PRICE_MAX);
            roomTypeGroup.check(R.id.fChipAny);
            for (int i = 0; i < amenityGroup.getChildCount(); i++) {
                ((Chip) amenityGroup.getChildAt(i)).setChecked(false);
            }
        });

        applyBtn.setOnClickListener(x -> {
            DormFilter result = new DormFilter();
            List<Float> vals = priceSlider.getValues();
            result.minPrice = Math.round(vals.get(0));
            result.maxPrice = Math.round(vals.get(1));

            int checked = roomTypeGroup.getCheckedChipId();
            if (checked == R.id.fChipSolo) result.roomType = Dorm.TYPE_SOLO;
            else if (checked == R.id.fChipShared) result.roomType = Dorm.TYPE_SHARED;
            else if (checked == R.id.fChipBedspace) result.roomType = Dorm.TYPE_BEDSPACE;

            for (int i = 0; i < amenityGroup.getChildCount(); i++) {
                Chip c = (Chip) amenityGroup.getChildAt(i);
                if (c.isChecked()) result.amenities.add(c.getText().toString());
            }

            if (getParentFragment() instanceof FilterListener) {
                ((FilterListener) getParentFragment()).onFilterApplied(result);
            }
            dismiss();
        });
    }

    private void updatePriceLabel() {
        List<Float> vals = priceSlider.getValues();
        String max = vals.get(1) >= DormFilter.PRICE_MAX ? Dorm.formatPeso(DormFilter.PRICE_MAX) + "+"
                : Dorm.formatPeso(Math.round(vals.get(1)));
        priceRangeTv.setText(Dorm.formatPeso(Math.round(vals.get(0))) + " - " + max);
    }
}
