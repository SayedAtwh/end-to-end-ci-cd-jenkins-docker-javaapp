package com.islamicapp.model;

public class Azkar {
    private String category; // e.g., "Morning", "Evening"
    private String[] azkarList;

    public Azkar() {}

    public Azkar(String category, String[] azkarList) {
        this.category = category;
        this.azkarList = azkarList;
    }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String[] getAzkarList() { return azkarList; }
    public void setAzkarList(String[] azkarList) { this.azkarList = azkarList; }
}