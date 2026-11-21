package com.itismob.s02.group9.mco.dormmate;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class RoommateFilter implements Serializable {

    public static final int AGE_MIN = 17, AGE_MAX = 24;
    public static final int BUDGET_MIN = 3000, BUDGET_MAX = 15000;

    Set<String> genders = new HashSet<>();
    Set<String> locations = new HashSet<>();
    Set<String> lifestyle = new HashSet<>();
    int minAge = AGE_MIN, maxAge = AGE_MAX;
    int minBudget = BUDGET_MIN, maxBudget = BUDGET_MAX;

    boolean isActive() {
        return !genders.isEmpty() || !locations.isEmpty() || !lifestyle.isEmpty()
                || minAge != AGE_MIN || maxAge != AGE_MAX
                || minBudget != BUDGET_MIN || maxBudget != BUDGET_MAX;
    }

    boolean matches(Roommate r) {
        if (r.age < minAge || r.age > maxAge) return false;

        if (r.budgetMax < minBudget || r.budgetMin > maxBudget) return false;

        if (!genders.isEmpty() && !genders.contains(r.gender)) return false;
        if (!locations.isEmpty() && !locations.contains(r.area)) return false;

        return r.tags.containsAll(lifestyle);
    }
}