package com.itismob.s02.group9.mco.dormmate;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.progressindicator.LinearProgressIndicator;

public class RoommateProfileActivity extends AppCompatActivity {

    public static final String EXTRA_ROOMMATE_ID = "roommate_id";

    private ImageButton btnBack;
    private Button btnConnect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.roommate_profile);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(bars.left, 0, bars.right, bars.bottom);
            findViewById(R.id.topBar).setPadding(0, bars.top, 0, 0);
            return insets;
        });

        btnBack = findViewById(R.id.btnBack);
        btnConnect = findViewById(R.id.btnConnect);
        btnBack.setOnClickListener(v -> finish());


        String id = getIntent().getStringExtra(EXTRA_ROOMMATE_ID);
        Roommate roommate = DataHelperRoommate.getRoommateById(id);
        if (roommate == null) {
            Toast.makeText(this, "Profile not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        bind(roommate);

        btnConnect.setOnClickListener(v -> {
            // Chat with the person whose profile this is (reuses an existing chat if any).
            Conversation chat = MessageStore.get()
                    .getOrCreateConversation(roommate.id, roommate.name);

            // Put the Messages tab underneath so Back from the chat lands there.
            Intent pages = new Intent(this, Pages.class);
            pages.putExtra(Pages.EXTRA_OPEN_TAB, "messages");
            pages.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(pages);

            Intent chatIntent = new Intent(this, ChatActivity.class);
            chatIntent.putExtra(ChatActivity.EXTRA_CONVERSATION_ID, chat.getId());
            startActivity(chatIntent);
        });
    }

    private void bind(Roommate r) {
        ((ImageView) findViewById(R.id.roommateImage)).setImageResource(r.photoRes);

        setText(R.id.roommateNameAge, r.name + ", " + r.age);
        setText(R.id.roommateCourse, r.course + " • " + r.year);
        setText(R.id.compatibilityPercent, r.compatibility + "%");

        setText(R.id.aboutUser, "About " + r.name);
        setText(R.id.aboutDesc, r.about);

        setText(R.id.roommateBudget, r.budgetShort());
        setText(R.id.roommateArea, r.area);
        setText(R.id.roommateStayDuration, r.stayTerm);

        setText(R.id.roommateLookingFor, r.lookingFor);

        // Lifestyle chips
        ChipGroup tags = findViewById(R.id.roommateTags);
        tags.removeAllViews();
        for (String tag : r.tags) {
            Chip chip = (Chip) getLayoutInflater()
                    .inflate(R.layout.item_profile_tag_chip, tags, false);
            chip.setText(tag);
            tags.addView(chip);
        }

        // Why you match
        setText(R.id.matchLabel, r.fitLabel());
        setProgress(R.id.budgetProgress, R.id.matchBudgetPercentage, r.budgetMatch);
        setProgress(R.id.lifestyleProgress, R.id.matchLifestylePercentage, r.lifestyleMatch);
        setProgress(R.id.homeprefProgress, R.id.matchHomePrefPercentage, r.homeMatch);

        btnConnect.setText("Connect with " + r.name);
    }

    private void setText(int viewId, String text) {
        ((TextView) findViewById(viewId)).setText(text);
    }

    private void setProgress(int barId, int percentTextId, int value) {
        ((LinearProgressIndicator) findViewById(barId)).setProgress(value);
        setText(percentTextId, value + "%");
    }
}
