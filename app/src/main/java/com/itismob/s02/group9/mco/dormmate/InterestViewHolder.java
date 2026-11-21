package com.itismob.s02.group9.mco.dormmate;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class InterestViewHolder extends RecyclerView.ViewHolder {
    private final TextView chip;

    public InterestViewHolder(@NonNull View itemView) {
        super(itemView);
        this.chip = itemView.findViewById(R.id.btnInterest2);
    }
    public void bindData(Interest interest, boolean selected) {
        chip.setText(interest.getName());

        if (selected) {
            chip.setCompoundDrawablesRelativeWithIntrinsicBounds(
                    0, 0, R.drawable.ic_interest_close_circle, 0);
            chip.setContentDescription("Remove " + interest.getName());
        } else {
            chip.setCompoundDrawablesRelativeWithIntrinsicBounds(
                    0, 0, R.drawable.ic_interest_add, 0);
            chip.setContentDescription("Add " + interest.getName());
        }
    }

    public void setChipOnClickListener(View.OnClickListener listener) {
        chip.setOnClickListener(listener);
    }
}
