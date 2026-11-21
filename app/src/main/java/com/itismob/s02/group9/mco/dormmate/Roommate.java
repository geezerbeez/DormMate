package com.itismob.s02.group9.mco.dormmate;

import java.util.List;
import java.util.Locale;

public class Roommate {
    String id;
    String name;
    int age;
    String gender;
    String course;
    String year;
    String area;
    int budgetMin;
    int budgetMax;
    String stayTerm;
    int compatibility;
    int budgetMatch;
    int lifestyleMatch;
    int homeMatch;
    String about;
    String lookingFor;
    List<String> tags;
    int photoRes;

    Roommate(String id, String name, int age, String gender, String course, String year,
             String area, int budgetMin, int budgetMax, String stayTerm,
             int compatibility, int budgetMatch, int lifestyleMatch, int homeMatch,
             String about, String lookingFor, List<String> tags, int photoRes) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.course = course;
        this.year = year;
        this.area = area;
        this.budgetMin = budgetMin;
        this.budgetMax = budgetMax;
        this.stayTerm = stayTerm;
        this.compatibility = compatibility;
        this.budgetMatch = budgetMatch;
        this.lifestyleMatch = lifestyleMatch;
        this.homeMatch = homeMatch;
        this.about = about;
        this.lookingFor = lookingFor;
        this.tags = tags;
        this.photoRes = photoRes;
    }


    String budgetLabel() {
        return String.format(Locale.US, "₱%,d-₱%,d/month", budgetMin, budgetMax);
    }


    String budgetShort() {
        return "₱" + shortK(budgetMin) + "-₱" + shortK(budgetMax);
    }

    private static String shortK(int value) {
        return value % 1000 == 0
                ? (value / 1000) + "k"
                : String.format(Locale.US, "%.1fk", value / 1000f);
    }


    String compatibilityLabel() {
        return compatibility + "% compatible";
    }

    String fitLabel() {
        if (compatibility >= 90) return "Excellent fit";
        if (compatibility >= 75) return "Great fit";
        return "Good fit";
    }
}