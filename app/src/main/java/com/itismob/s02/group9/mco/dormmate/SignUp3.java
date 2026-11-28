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

public class SignUp3 extends AppCompatActivity {
    public boolean isSelected = false;
    private TextView tvContractWarning;
    private FrameLayout btnLongTerm, btnShortTerm, btnTempStay;
    private ImageView shpSolid1, shpSolid2, shpSolid3;
    private ImageButton btnProceed;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.enableEdgeToEdge(getWindow());
        setContentView(R.layout.activity_sign_up3);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        this.btnLongTerm = findViewById(R.id.btnLongTerm);
        this.btnShortTerm = findViewById(R.id.btnShortTerm);
        this.btnTempStay = findViewById(R.id.btnTempStay);
        this.btnProceed = findViewById(R.id.btnProceed);
        this.tvContractWarning = findViewById(R.id.tvContractWarning);

        shpSolid1 = findViewById(R.id.shpSolid1);
        Drawable originalCircle = shpSolid1.getDrawable();
        shpSolid2 = findViewById(R.id.shpSolid2);
        Drawable originalCircle2 = shpSolid2.getDrawable();
        shpSolid3 = findViewById(R.id.shpSolid3);

        btnLongTerm.setOnClickListener(v -> {
            tvContractWarning.setVisibility(View.GONE);
            isSelected = true;
            shpSolid1.setImageResource(R.drawable.check_circle);
            shpSolid2.setImageResource(R.drawable.circle_outline);
            shpSolid3.setImageResource(R.drawable.circle_outline);
        });
        btnShortTerm.setOnClickListener(v -> {
            tvContractWarning.setVisibility(View.GONE);
            isSelected = true;
            shpSolid1.setImageResource(R.drawable.circle_outline);
            shpSolid2.setImageResource(R.drawable.check_circle);
            shpSolid3.setImageResource(R.drawable.circle_outline);
        });
        btnTempStay.setOnClickListener(v -> {
            tvContractWarning.setVisibility(View.GONE);
            isSelected = true;
            shpSolid1.setImageResource(R.drawable.circle_outline);
            shpSolid2.setImageResource(R.drawable.circle_outline);
            shpSolid3.setImageResource(R.drawable.check_circle);
        });


        btnProceed.setOnClickListener(v -> {
            if(isSelected){
                Intent intent = new Intent(SignUp3.this, SignUpGenderPreference.class);
                startActivity(intent);
            }
            else{
                tvContractWarning.setVisibility(View.VISIBLE);
                return;
            }
        });

    }
}