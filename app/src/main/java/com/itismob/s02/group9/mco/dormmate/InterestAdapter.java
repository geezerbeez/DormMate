package com.itismob.s02.group9.mco.dormmate;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class InterestAdapter extends RecyclerView.Adapter<InterestViewHolder> {

    private final ArrayList<Interest> data;
    private final ArrayList<Interest> visibleInterests;
    private final ArrayList<Interest> selectedInterests = new ArrayList<>();

    private String query = "";
    private Runnable onSelectionChanged;

    public InterestAdapter(ArrayList<Interest> data) {
        this.data = data;
        this.visibleInterests = new ArrayList<>(data);
    }

    public void setOnSelectionChanged(Runnable listener) {
        this.onSelectionChanged = listener;
    }


    @NonNull
    @Override
    public InterestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.activity_interest_rv, parent, false);
        InterestViewHolder holder = new InterestViewHolder(itemView);

        holder.setChipOnClickListener(view -> {
            int position = holder.getBindingAdapterPosition();

            if (position == RecyclerView.NO_POSITION) {
                return;
            }

            Interest interest = visibleInterests.get(position);

            if (selectedInterests.contains(interest)) {
                selectedInterests.remove(interest);
            } else {
                selectedInterests.add(interest);
            }

            filter(query);

            if (onSelectionChanged != null) {
                onSelectionChanged.run();
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull InterestViewHolder holder, int position) {
        Interest interest = visibleInterests.get(position);
        holder.bindData(interest, selectedInterests.contains(interest));
    }

    @Override
    public int getItemCount() {
        return visibleInterests.size();
    }

    public int getSelectedCount() {
        return selectedInterests.size();
    }

    public void filter(String searchText) {
        query = searchText.trim();
        visibleInterests.clear();

        for (Interest interest : data) {
            if (selectedInterests.contains(interest) || interest.matchesSearch(query)) {
                visibleInterests.add(interest);
            }
        }

        notifyDataSetChanged();
    }
}
