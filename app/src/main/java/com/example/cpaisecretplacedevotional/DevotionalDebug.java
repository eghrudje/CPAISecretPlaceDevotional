package com.example.cpaisecretplacedevotional;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class DevotionalDebug extends RealmObject {

    @PrimaryKey
    private int devotional_id;
    private String date;

    public int getDevotional_id() {
        return devotional_id;
    }

    public void setDevotional_id(int devotional_id) {
        this.devotional_id = devotional_id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public DevotionalDebug(int devotional_id, String date) {
        this.devotional_id = devotional_id;
        this.date = date;
    }
    public DevotionalDebug() {
    }

}
