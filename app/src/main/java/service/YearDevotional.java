package service;

import androidx.annotation.NonNull;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import service.Devotional;
import service.MonthDevotional;

public class YearDevotional{

    private int yearDevotional_id;
    private String yearName;
    private String yearImage;

    public YearDevotional(){
    }

    public String getYearImage() {
        return yearImage;
    }

    public void setYearImage(String yearImage) {
        this.yearImage = yearImage;
    }

    private ArrayList<MonthDevotional> monthDevotionals;

    public YearDevotional(String yearName) {
        this.yearName = yearName;
    }

    public int getYearDevotional_id() {
        return yearDevotional_id;
    }

    public void setYearDevotional_id(int monthDevotional_id) {
        this.yearDevotional_id = monthDevotional_id;
    }

    public String getYearName() {
        return yearName;
    }

    public void setYearName(String yearName) {
        this.yearName = yearName;
    }

    public ArrayList<MonthDevotional> getMonthDevotionals() {
        return monthDevotionals;
    }

    public void setMonthDevotionals(ArrayList<MonthDevotional> monthDevotionals) {
        this.monthDevotionals = monthDevotionals;
    }

    @Override
    public String toString() {
        return "YearDevotional{" +  '\'' + ", yearName=" + yearName + '}';
    }
}
