package com.itismob.s02.group9.mco.dormmate;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

public class activity_sign_up_genderPreference extends AppCompatActivity {

    public boolean isSelected = false;
    private FrameLayout btnMale, btnFemale, btnNoPref;
    private ImageView shpSolid1, shpSolid2, shpSolid3;
    private ImageButton btnProceedPref;
    private TextView tvGenderWarning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.enableEdgeToEdge(getWindow());
        setContentView(R.layout.activity_sign_up_gender_preference);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        this.btnMale = findViewById(R.id.btnMale);
        this.btnFemale = findViewById(R.id.btnFemale);
        this.btnNoPref = findViewById(R.id.btnNoPref);
        this.btnProceedPref = findViewById(R.id.btnProceedPref);
        this.tvGenderWarning = findViewById(R.id.tvGenderWarning);

        shpSolid1 = findViewById(R.id.shpSolid1);
        Drawable originalCircle = shpSolid1.getDrawable();
        shpSolid2 = findViewById(R.id.shpSolid2);
        Drawable originalCircle2 = shpSolid2.getDrawable();
        shpSolid3 = findViewById(R.id.shpSolid3);

        btnMale.setOnClickListener(v -> {
            tvGenderWarning.setVisibility(View.GONE);
            isSelected = true;
            shpSolid1.setImageResource(R.drawable.check_circle);
            shpSolid2.setImageResource(R.drawable.circle_outline);
            shpSolid3.setImageResource(R.drawable.circle_outline);
        });
        btnFemale.setOnClickListener(v -> {
            tvGenderWarning.setVisibility(View.GONE);
            isSelected = true;
            shpSolid1.setImageResource(R.drawable.circle_outline);
            shpSolid2.setImageResource(R.drawable.check_circle);
            shpSolid3.setImageResource(R.drawable.circle_outline);
        });
        btnNoPref.setOnClickListener(v -> {
            tvGenderWarning.setVisibility(View.GONE);
            isSelected = true;
            shpSolid1.setImageResource(R.drawable.circle_outline);
            shpSolid2.setImageResource(R.drawable.circle_outline);
            shpSolid3.setImageResource(R.drawable.check_circle);
        });

        btnProceedPref.setOnClickListener(v -> {
            if(isSelected){
                Intent intent = new Intent(activity_sign_up_genderPreference.this, activity_sign_up4.class);
                startActivity(intent);
            }
            else{
                tvGenderWarning.setVisibility(View.VISIBLE);
                return;
            }
        });
    }
}