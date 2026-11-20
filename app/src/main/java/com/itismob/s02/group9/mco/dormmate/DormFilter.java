package com.itismob.s02.group9.mco.dormmate;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/** The options chosen in the Filter bottom sheet / quick chips on the Explore page. */
public class DormFilter implements Serializable {

    public static final int PRICE_MIN = 0;
    public static final int PRICE_MAX = 30000;

    public String query = "";
    public int minPrice = PRICE_MIN;
    public int maxPrice = PRICE_MAX;
    /** null = any room type */
    public String roomType = null;
    public Set<String> amenities = new HashSet<>();

    public boolean matches(Dorm d) {
        if (d.getStatus() != Dorm.Status.ACTIVE) return false;

        if (query != null && !query.trim().isEmpty()) {
            String q = query.trim().toLowerCase();
            String hay = (d.getName() + " " + d.getAddress()).toLowerCase();
            if (!hay.contains(q)) return false;
        }
        if (d.getPrice() < minPrice || d.getPrice() > maxPrice) return false;
        if (roomType != null && !roomType.equals(d.getRoomType())) return false;
        return d.getAmenities().containsAll(amenities);
    }

    /** Number of active filters (used for the badge on the Filter button). */
    public int activeCount() {
        int n = 0;
        if (minPrice > PRICE_MIN || maxPrice < PRICE_MAX) n++;
        if (roomType != null) n++;
        n += amenities.size();
        return n;
    }
}
