package com.islamicapp.model;

public class Zikr {
    private int id;
    private String text;
    private String translation;
    private String category;
    private int count;
    private boolean completed;

    public Zikr() {}

    public Zikr(int id, String text, String translation, String category, int count) {
        this.id = id;
        this.text = text;
        this.translation = translation;
        this.category = category;
        this.count = count;
        this.completed = false;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }

    public String getTranslation() { return translation; }
    public void setTranslation(String translation) { this.translation = translation; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public int getCount() { return count; }
    public void setCount(int count) { this.count = count; }

    public boolean isCompleted() { return completed; }
    public void setCompleted(boolean completed) { this.completed = completed; }
}
