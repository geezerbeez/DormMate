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
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;

import java.util.ArrayList;
import java.util.Arrays;
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

        RecyclerView rvInterests = findViewById(R.id.rvInterests);

        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(this);
        layoutManager.setFlexWrap(FlexWrap.WRAP);
        rvInterests.setLayoutManager(layoutManager);

        Interest[] allInterests = InterestDataHelper.getInterests();
        ArrayList<Interest> data = new ArrayList<>(Arrays.asList(allInterests));

        InterestAdapter adapter = new InterestAdapter(data);
        adapter.setOnSelectionChanged(() -> tvInterestError.setVisibility(View.GONE));
        rvInterests.setAdapter(adapter);

        etInterestSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        btnClearSearch.setOnClickListener(v -> etInterestSearch.setText(""));

        btnContinue.setOnClickListener(v -> {
            int selectedCount = adapter.getSelectedCount();

            if (selectedCount == 5) {
                tvInterestError.setVisibility(View.GONE);

                Intent intent = new Intent(activity_sign_up4.this, activity_sign_up5.class);
                startActivity(intent);
            } else if (selectedCount < 5) {
                tvInterestError.setText("Please select " + (5 - selectedCount) + " more.");
                tvInterestError.setVisibility(View.VISIBLE);
            } else {
                tvInterestError.setText("Please remove " + (selectedCount - 5) + " to continue.");
                tvInterestError.setVisibility(View.VISIBLE);
            }
        });

        btnSkip.setOnClickListener(v -> {
            Intent intent = new Intent(activity_sign_up4.this, activity_sign_up5.class);
            startActivity(intent);
        });



    }
}