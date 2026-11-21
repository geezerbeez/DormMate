package com.itismob.s02.group9.mco.dormmate;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/** Card list on the dorm-owner "My Listings" page. */
public class LandlordListingAdapter extends RecyclerView.Adapter<LandlordListingAdapter.VH> {

    public interface Actions {
        void onPreview(Dorm dorm);
        void onEdit(Dorm dorm);
        void onToggleVisibility(Dorm dorm);
        void onToggleFull(Dorm dorm);
        void onDelete(Dorm dorm);
    }

    private final List<Dorm> items = new ArrayList<>();
    private final Actions actions;

    public LandlordListingAdapter(Actions actions) {
        this.actions = actions;
    }

    public void submit(List<Dorm> newItems) {
        items.clear();
        items.addAll(newItems);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VH(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_landlord_listing, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int position) {
        Dorm d = items.get(position);
        h.photo.setImageResource(d.getCoverPhoto());
        h.name.setText(d.getName());
        h.price.setText(d.getPriceLabel() + " / month");
        h.meta.setText(d.getRoomType() + " \u00B7 " + d.getAvailableSlots() + " of "
                + d.getTotalSlots() + " slots open \u00B7 " + d.getInquiries() + " inquiries");

        switch (d.getStatus()) {
            case ACTIVE:
                h.status.setText("Active");
                h.status.setBackgroundResource(R.drawable.bg_status_active);
                h.status.setTextColor(h.itemView.getContext().getColor(R.color.statusGreen));
                break;
            case FULL:
                h.status.setText("Full");
                h.status.setBackgroundResource(R.drawable.bg_status_full);
                h.status.setTextColor(h.itemView.getContext().getColor(R.color.statusOrange));
                break;
            default:
                h.status.setText("Hidden");
                h.status.setBackgroundResource(R.drawable.bg_status_hidden);
                h.status.setTextColor(h.itemView.getContext().getColor(R.color.statusGrey));
                break;
        }

        boolean hidden = d.getStatus() == Dorm.Status.HIDDEN;
        h.visibilityBtn.setText(hidden ? "Show" : "Hide");
        h.visibilityBtn.setIconResource(hidden ? R.drawable.ic_visibility : R.drawable.ic_visibility_off);
        h.fullBtn.setText(d.getStatus() == Dorm.Status.FULL ? "Reopen" : "Mark full");

        h.itemView.setOnClickListener(v -> actions.onPreview(d));
        h.edit.setOnClickListener(v -> actions.onEdit(d));
        h.visibilityBtn.setOnClickListener(v -> actions.onToggleVisibility(d));
        h.fullBtn.setOnClickListener(v -> actions.onToggleFull(d));
        h.delete.setOnClickListener(v -> actions.onDelete(d));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        final ImageView photo;
        final TextView status, name, price, meta;
        final com.google.android.material.button.MaterialButton edit, visibilityBtn, fullBtn;
        final ImageButton delete;

        VH(@NonNull View v) {
            super(v);
            photo = v.findViewById(R.id.listingPhotoIv);
            status = v.findViewById(R.id.listingStatusTv);
            name = v.findViewById(R.id.listingNameTv);
            price = v.findViewById(R.id.listingPriceTv);
            meta = v.findViewById(R.id.listingMetaTv);
            edit = v.findViewById(R.id.editBtn);
            visibilityBtn = v.findViewById(R.id.visibilityBtn);
            fullBtn = v.findViewById(R.id.fullBtn);
            delete = v.findViewById(R.id.deleteBtn);
        }
    }
}
