package com.itismob.s02.group9.mco.dormmate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

public class activity_sign_up5 extends AppCompatActivity {
    private ImageButton btnContinue;
    private TextView btnSkip, tvLifestyleError,
            tvInterestError, btnDrinkingCh1, btnDrinkingCh2, btnDrinkingCh3, btnDrinkingCh4,
            btnSmokingCh1, btnSmokingCh2, btnSmokingCh3, btnSmokingCh4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.enableEdgeToEdge(getWindow());
        setContentView(R.layout.activity_sign_up5);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        this.btnContinue = findViewById(R.id.btnContinue);
        this.btnSkip = findViewById(R.id.btnSkip);
        this.tvLifestyleError = findViewById(R.id.tvLifestyleError);

        this.btnDrinkingCh1 = findViewById(R.id.btnDrinkingCh1);
        this.btnDrinkingCh2 = findViewById(R.id.btnDrinkingCh2);
        this.btnDrinkingCh3 = findViewById(R.id.btnDrinkingCh3);
        this.btnDrinkingCh4 = findViewById(R.id.btnDrinkingCh4);

        this.btnSmokingCh1 = findViewById(R.id.btnSmokingCh1);
        this.btnSmokingCh2 = findViewById(R.id.btnSmokingCh2);
        this.btnSmokingCh3 = findViewById(R.id.btnDSmokingCh3);
        this.btnSmokingCh4 = findViewById(R.id.btnSmokingCh4);

        TextView[][] groups = {
                {btnDrinkingCh1, btnDrinkingCh2, btnDrinkingCh3, btnDrinkingCh4},
                {btnSmokingCh1, btnSmokingCh2, btnSmokingCh3, btnSmokingCh4}
        };

        for (TextView[] group : groups) {
            for (TextView choice : group) {
                choice.setOnClickListener(v -> {
                    boolean selectChoice = !choice.isSelected();

                    for (TextView option : group) {
                        boolean selected = option == choice && selectChoice;
                        option.setSelected(selected);

                        int icon = selected
                                ? R.drawable.ic_interest_close_circle
                                : R.drawable.ic_interest_add;
                        option.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, icon, 0);
                    }

                    if (selectChoice) {
                        tvLifestyleError.setVisibility(View.GONE);
                    }
                });
            }
        }

        btnContinue.setOnClickListener(v -> {
            boolean missingAnswer = true;

            for (TextView[] group : groups) {
                for (TextView choice : group) {
                    if (choice.isSelected()) {
                        missingAnswer = false;
                        break;
                    }
                }

                if (!missingAnswer) {
                    break;
                }
            }

            tvLifestyleError.setVisibility(missingAnswer ? View.VISIBLE : View.GONE);

            if (missingAnswer) {
                return;
            }

            Intent intent = new Intent(activity_sign_up5.this, activity_sign_up6.class);
            startActivity(intent);
        });

        btnSkip.setOnClickListener(v -> {
            Intent intent = new Intent(activity_sign_up5.this, activity_sign_up6.class);
            startActivity(intent);
        });
    }
}