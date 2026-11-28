package com.itismob.s02.group9.mco.dormmate;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// Sample data for roommate pages
public class RoommateDataHelper {

    private static List<Roommate> roommates;

    private RoommateDataHelper() {}

    public static List<Roommate> getRoommates() {
        return new ArrayList<>(load());
    }

    @Nullable
    public static Roommate getRoommateById(String id) {
        if (id == null) return null;
        for (Roommate r : load()) {
            if (r.id.equals(id)) return r;
        }
        return null;
    }

    private static List<Roommate> load() {
        if (roommates == null) {
            roommates = new ArrayList<>(Arrays.asList(
                    new Roommate("user_002", "Abigail", 21, "Women",
                            "BS Information Technology", "3rd year",
                            "Katipunan", 6000, 9000, "Short-Term",
                            92, 96, 90, 88,
                            "Weekend cafe explorer and respectful roommate. I like a calm home during weekdays and shared dinners when schedules align.",
                            "Women or non-binary roommates • 18-24 • Non-smoker • Okay with occasional guests • Values shared chores",
                            Arrays.asList("Pet-friendly", "Study-focused", "Early sleeper", "Tidy", "Non-smoker"),
                            R.drawable.img_roommateprofile),

                    new Roommate("user_003", "Haley", 20, "Women",
                            "BS Computer Science", "2nd year",
                            "Diliman", 5000, 8000, "Long-Term",
                            89, 90, 87, 85,
                            "Introvert who loves quiet nights, plants, and the occasional movie marathon.",
                            "Women roommates • 18-23 • Non-smoker • Prefers a quiet space",
                            Arrays.asList("Introvert", "Tidy", "Night owl", "Non-smoker"),
                            R.drawable.img_roommateprofile2),

                    new Roommate("user_004", "Sam", 22, "Men",
                            "BS Architecture", "4th year",
                            "Maginhawa", 7000, 10000, "Long-Term",
                            85, 82, 88, 80,
                            "Always up for a coffee run. I keep shared spaces organized and I'm easygoing about schedules.",
                            "Any gender • 20-26 • Okay with pets • Splits chores evenly",
                            Arrays.asList("Friendly", "Tidy", "Pet-friendly"),
                            R.drawable.img_roommateprofile3)
            ));
        }
        return roommates;
    }
}