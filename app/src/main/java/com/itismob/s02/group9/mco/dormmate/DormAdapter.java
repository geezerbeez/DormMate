package com.itismob.s02.group9.mco.dormmate;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/** Card list used on the Explore page. */
public class DormAdapter extends RecyclerView.Adapter<DormAdapter.DormViewHolder> {

    public interface OnDormClickListener {
        void onDormClick(Dorm dorm);
    }

    private final List<Dorm> items = new ArrayList<>();
    private final OnDormClickListener listener;

    public DormAdapter(OnDormClickListener listener) {
        this.listener = listener;
    }

    public void submit(List<Dorm> newItems) {
        items.clear();
        items.addAll(newItems);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DormViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dorm, parent, false);
        return new DormViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull DormViewHolder h, int position) {
        Dorm d = items.get(position);
        h.photo.setImageResource(d.getCoverPhoto());
        h.roomType.setText(d.getRoomType());
        h.name.setText(d.getName());
        h.price.setText(d.getPriceLabel());
        h.address.setText(d.getAddress());
        h.distance.setText(d.getDistanceLabel());
        h.slots.setText(d.getSlotsLabel());
        updateHeart(h.save, d.isSaved());

        h.save.setOnClickListener(v -> {
            d.setSaved(!d.isSaved());
            updateHeart(h.save, d.isSaved());
        });
        h.itemView.setOnClickListener(v -> listener.onDormClick(d));
    }

    private void updateHeart(ImageButton btn, boolean saved) {
        btn.setImageResource(saved ? R.drawable.ic_favorite : R.drawable.ic_favorite_border);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class DormViewHolder extends RecyclerView.ViewHolder {
        final ImageView photo;
        final ImageButton save;
        final TextView roomType, name, price, address, distance, slots;

        DormViewHolder(@NonNull View v) {
            super(v);
            photo = v.findViewById(R.id.dormPhotoIv);
            save = v.findViewById(R.id.saveBtn);
            roomType = v.findViewById(R.id.roomTypeTv);
            name = v.findViewById(R.id.dormNameTv);
            price = v.findViewById(R.id.dormPriceTv);
            address = v.findViewById(R.id.dormAddressTv);
            distance = v.findViewById(R.id.dormDistanceTv);
            slots = v.findViewById(R.id.dormSlotsTv);
        }
    }
}
