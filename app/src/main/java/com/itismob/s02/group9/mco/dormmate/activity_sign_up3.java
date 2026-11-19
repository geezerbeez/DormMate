package com.itismob.s02.group9.mco.dormmate;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

public class activity_sign_up3 extends AppCompatActivity {
    public boolean isSelected = false;
    private ImageView selectedCircle;
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

        shpSolid1 = findViewById(R.id.shpSolid1);
        Drawable originalCircle = shpSolid1.getDrawable();
        shpSolid2 = findViewById(R.id.shpSolid2);
        Drawable originalCircle2 = shpSolid2.getDrawable();
        shpSolid3 = findViewById(R.id.shpSolid3);

        btnLongTerm.setOnClickListener(v -> {
            isSelected = true;
            shpSolid1.setImageResource(R.drawable.check_circle);
            shpSolid2.setImageResource(R.drawable.circle_outline);
            shpSolid3.setImageResource(R.drawable.circle_outline);
        });
        btnShortTerm.setOnClickListener(v -> {
            isSelected = true;
            shpSolid1.setImageResource(R.drawable.circle_outline);
            shpSolid2.setImageResource(R.drawable.check_circle);
            shpSolid3.setImageResource(R.drawable.circle_outline);
        });
        btnTempStay.setOnClickListener(v -> {
            isSelected = true;
            shpSolid1.setImageResource(R.drawable.circle_outline);
            shpSolid2.setImageResource(R.drawable.circle_outline);
            shpSolid3.setImageResource(R.drawable.check_circle);
        });

        btnProceed.setOnClickListener(v -> {
            if(isSelected){
                Intent intent = new Intent(activity_sign_up3.this, activity_sign_up4.class);
                startActivity(intent);
            }
        });

    }
}