package com.itismob.s02.group9.mco.dormmate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.TimeZone;

public class SignUp extends AppCompatActivity {

    private Button btnContinue;
    private EditText etFirstName, etLastName, etDay, etYear;
    private TextView tvEmptyFirstName, tvEmptyLastName, tvIncorrectDOB;
    private Spinner spMonth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        this.btnContinue = findViewById(R.id.btnContinue);
        this.etFirstName = findViewById(R.id.etFirstName);
        this.etLastName = findViewById(R.id.etLastName);
        this.tvEmptyFirstName = findViewById(R.id.tvEmptyFirstName);
        this.tvEmptyLastName = findViewById(R.id.tvEmptyLastName);
        this.spMonth = findViewById(R.id.spMonth);
        this.etYear = findViewById(R. id.etYear);
        this.etDay = findViewById(R.id.etDay);
        this.tvIncorrectDOB = findViewById(R.id.tvIncorrectDOB);

        btnContinue.setOnClickListener(v -> {
            String firstName = etFirstName.getText().toString().trim();
            String lastName = etLastName.getText().toString().trim();
            String Year = etYear.getText().toString().trim();
            String Day = etDay.getText().toString().trim();
            int Month = spMonth.getSelectedItemPosition();


            boolean missingFirstName = firstName.isEmpty();
            boolean missingLastName = lastName.isEmpty();
            boolean incompleteDOB = Month <= 0 || Year.isEmpty() || Day.isEmpty();

            tvEmptyFirstName.setVisibility(missingFirstName ? View.VISIBLE : View.GONE);
            tvEmptyLastName.setVisibility(missingLastName ? View.VISIBLE : View.GONE);

            TimeZone manila = TimeZone.getTimeZone("Asia/Manila");
            Calendar cutoff = Calendar.getInstance(manila);
            Calendar birthDate = Calendar.getInstance(manila);
            cutoff.add(Calendar.YEAR, -17);

            try {
                int year = Integer.parseInt(Year);
                int day = Integer.parseInt(Day);
                int monthIndex = Month - 1; // If position 0 is "Select month"

                birthDate.clear();
                birthDate.setLenient(false);
                birthDate.set(year, monthIndex, day);
                birthDate.getTimeInMillis();
            } catch (IllegalArgumentException e) {
                tvIncorrectDOB.setText("Enter a valid date of birth");
                tvIncorrectDOB.setVisibility(View.VISIBLE);
                return;
            }

            if (birthDate.after(cutoff)) {
                tvIncorrectDOB.setText("You must be 17+ to use DormMate");
                tvIncorrectDOB.setVisibility(View.VISIBLE);
                return;
            }
            tvIncorrectDOB.setVisibility(View.GONE);

            if (missingFirstName || missingLastName) {
                return;
            }

            Intent intent = new Intent(SignUp.this, SignUp2.class);
            startActivity(intent);

        });


    }


}