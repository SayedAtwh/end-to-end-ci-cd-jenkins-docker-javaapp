package com.islamicapp.model;

public class Surah {
    private int number;
    private String name;
    private String englishName;
    private int numberOfAyahs;
    private String revelationType;
    private String[] ayahs; // Arabic text

    // Constructors, getters, setters
    public Surah() {}

    public Surah(int number, String name, String englishName, int numberOfAyahs, String revelationType, String[] ayahs) {
        this.number = number;
        this.name = name;
        this.englishName = englishName;
        this.numberOfAyahs = numberOfAyahs;
        this.revelationType = revelationType;
        this.ayahs = ayahs;
    }

    // Getters and setters
    public int getNumber() { return number; }
    public void setNumber(int number) { this.number = number; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEnglishName() { return englishName; }
    public void setEnglishName(String englishName) { this.englishName = englishName; }

    public int getNumberOfAyahs() { return numberOfAyahs; }
    public void setNumberOfAyahs(int numberOfAyahs) { this.numberOfAyahs = numberOfAyahs; }

    public String getRevelationType() { return revelationType; }
    public void setRevelationType(String revelationType) { this.revelationType = revelationType; }

    public String[] getAyahs() { return ayahs; }
    public void setAyahs(String[] ayahs) { this.ayahs = ayahs; }
}