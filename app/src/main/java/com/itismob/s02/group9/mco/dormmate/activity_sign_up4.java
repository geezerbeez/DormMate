package com.itismob.s02.group9.mco.dormmate;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Locale;


public class activity_sign_up4 extends AppCompatActivity {
    private ImageButton btnContinue, btnClearSearch;
    private EditText etInterestSearch;
    private TextView btnSkip, tvInterestError,
            btnInterest1, btnInterest2, btnInterest3,
            btnInterest4, btnInterest5, btnInterest6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.enableEdgeToEdge(getWindow());
        setContentView(R.layout.activity_sign_up4);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        this.btnContinue = findViewById(R.id.btnContinue);
        this.btnClearSearch = findViewById(R.id.btnClearSearch);
        this.etInterestSearch = findViewById(R.id.etInterestSearch);
        this.btnSkip = findViewById(R.id.btnSkip);
        this.tvInterestError = findViewById(R.id.tvInterestError);
        this.btnInterest1 = findViewById(R.id.btnInterest1);
        this.btnInterest2 = findViewById(R.id.btnInterest2);
        this.btnInterest3 = findViewById(R.id.btnInterest3);
        this.btnInterest4 = findViewById(R.id.btnInterest4);
        this.btnInterest5 = findViewById(R.id.btnInterest5);
        this.btnInterest6 = findViewById(R.id.btnInterest6);

        TextView[] interests = {
                btnInterest1, btnInterest2, btnInterest3,
                btnInterest4, btnInterest5, btnInterest6
        };

        for (TextView interest : interests) {
            interest.setCompoundDrawablesRelativeWithIntrinsicBounds(
                    0, 0, R.drawable.ic_interest_add, 0);
            interest.setContentDescription("Add " + interest.getText());

            interest.setOnClickListener(v -> {
                boolean selected = !interest.isSelected();
                interest.setSelected(selected);

                int icon = selected
                        ? R.drawable.ic_interest_close_circle
                        : R.drawable.ic_interest_add;
                interest.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, icon, 0);
                interest.setContentDescription(
                        (selected ? "Remove " : "Add ") + interest.getText());

                if (selected) {
                    tvInterestError.setVisibility(View.GONE);
                }

                String query = etInterestSearch.getText().toString()
                        .trim().toLowerCase(Locale.ROOT);
                String label = interest.getText().toString().toLowerCase(Locale.ROOT);
                boolean visible = selected || label.contains(query);
                interest.setVisibility(visible ? View.VISIBLE : View.GONE);
            });
        }

        etInterestSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String query = s.toString().trim().toLowerCase(Locale.ROOT);

                for (TextView interest : interests) {
                    String label = interest.getText().toString().toLowerCase(Locale.ROOT);
                    boolean visible = interest.isSelected() || label.contains(query);
                    interest.setVisibility(visible ? View.VISIBLE : View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        btnClearSearch.setOnClickListener(v -> etInterestSearch.setText(""));

        btnContinue.setOnClickListener(v -> {
            boolean missingInterest = true;

            for (TextView interest : interests) {
                if (interest.isSelected()) {
                    missingInterest = false;
                    break;
                }
            }

            tvInterestError.setVisibility(missingInterest ? View.VISIBLE : View.GONE);

            if (missingInterest) {
                return;
            }

            Intent intent = new Intent(activity_sign_up4.this, activity_sign_up5.class);
            startActivity(intent);
        });

        btnSkip.setOnClickListener(v -> {
            Intent intent = new Intent(activity_sign_up4.this, activity_sign_up5.class);
            startActivity(intent);
        });



    }
}