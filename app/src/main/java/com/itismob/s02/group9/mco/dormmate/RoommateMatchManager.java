package com.itismob.s02.group9.mco.dormmate;

import java.util.HashSet;
import java.util.Set;

public class RoommateMatchManager {

    private static final Set<String> likes = new HashSet<>();

    public static boolean likeUser(String currentUserId, String targetUserId) {

        // Check if target user already liked current user
        boolean isMatch = likes.contains(
                targetUserId + "_" + currentUserId
        );

        // Save current user's like
        likes.add(currentUserId + "_" + targetUserId);

        return isMatch;
    }

    // For testing: simulate another user liking you
    public static void simulateLike(String fromUser, String toUser) {
        likes.add(fromUser + "_" + toUser);
    }

    // Reset test data
    public static void resetLikes() {
        likes.clear();
    }
}