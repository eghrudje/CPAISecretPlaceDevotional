package service;

import java.util.ArrayList;

public class MonthDevotional{

    private String monthDevotional_id;
    private String monthName;
    private String monthImage;

    public MonthDevotional() {

    }

    public MonthDevotional(String monthName, String monthImage, String monthDevotional_id) {
        this.monthName = monthName;
        this.monthImage = monthImage;
        this.monthDevotional_id = monthDevotional_id;
    }

    public String getMonthImage() {
        return monthImage;
    }
    public void setMonthImage(String monthImage) {
        this.monthImage = monthImage;
    }

    public String getMonthDevotional_id() {
        return monthDevotional_id;
    }
    public void setMonthDevotional_id(String monthDevotional_id) {
        this.monthDevotional_id = monthDevotional_id;
    }
    public String getMonthName() {
        return monthName;
    }
    public void setMonthName(String monthName) {
        this.monthName = monthName;
    }

    //    public ArrayList<Devotional>getDevotionals() {
//        ArrayList arrayList = new ArrayList();
//        for (Devotional devotional: devotionals){
//            arrayList.add(devotional);
//        }
//        return arrayList;
//    }
//    public void setDevotionals(ArrayList<Devotional> arrayListDevotionals){
//        RealmList realmList = new RealmList();
//        for (Devotional devotional: arrayListDevotionals){
//            realmList.add(devotional);
//        }
//        this.devotionals = realmList;
//    }
    @Override
    public String toString(){
        return "MonthDevotional{" + "monthDevotional_id='"+ monthDevotional_id + ", " + "monthName='"+ monthName  + ", " + "monthImage='" + monthImage + '\''+ '}';
    }
}
