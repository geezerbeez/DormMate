package com.itismob.s02.group9.mco.dormmate;

public class Interest {
    private final String name;
    private final String category;

    public Interest(String name, String category) {
        this.name = name;
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public boolean matchesSearch(String query) {
        return name.contains(query) || category.equalsIgnoreCase(query);
    }
}
