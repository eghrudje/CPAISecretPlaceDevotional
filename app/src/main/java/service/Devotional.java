package service;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import java.io.IOException;
import java.io.InputStream;
import java.util.Comparator;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Devotional extends RealmObject implements Comparable<Devotional>{

    @PrimaryKey
    private int devotional_id;
    private String date, title, text, author, memoryVerse, memoryVersePassage, fullPassage, fullText, bibleInAYear, monthYearName, yearName;
    private String image;

    public static final String MyPreferences = "MyPref";

    public Devotional() {
    }

    public String getMonthYearName() {
        return monthYearName;
    }

    public void setMonthYearName(String monthYearName) {
        this.monthYearName = monthYearName;
    }

    public String getYearName() {
        return yearName;
    }

    public void setYearName(String yearName) {
        this.yearName = yearName;
    }

    public Devotional(int devotional__id, String date, String title, String text, String author,
                      String memoryVerse, String memoryVersePassage, String fullPassage, String fullText,
                      String bibleInAYear, String image, String monthYearName, String yearName) {
        this.devotional_id = devotional__id;
        this.date = date;
        this.title = title;
        this.text = text;
        this.author = author;
        this.memoryVerse = memoryVerse;
        this.memoryVersePassage = memoryVersePassage;
        this.fullPassage = fullPassage;
        this.fullText = fullText;
        this.bibleInAYear = bibleInAYear;
        this.image = image;
        this.monthYearName = monthYearName;
        this.yearName = yearName;
    }

    public static final String TAG = "Devotional";

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getMemoryVerse() {
        return memoryVerse;
    }

    public void setMemoryVerse(String memoryVerse) {
        this.memoryVerse = memoryVerse;
    }


    public String getBibleInAYear() {
        return bibleInAYear;
    }

    public void setBibleInAYear(String bibleInAYear) {
        this.bibleInAYear = bibleInAYear;
    }

    public String getFullText() {
        return fullText;
    }

    public void setFullText(String fullText) {
        this.fullText = fullText;
    }

    public String getFullPassage() {
        return fullPassage;
    }

    public void setFullPassage(String fullPassage) {
        this.fullPassage = fullPassage;
    }

    public String getMemoryVersePassage() {
        return memoryVersePassage;
    }

    public void setMemoryVersePassage(String memoryVersePassage) { this.memoryVersePassage = memoryVersePassage; }


    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "Devotional{" +
                "devotional_id=" + devotional_id +
                ", date='" + date + '\'' +
                '}';
    }

    //    public static ArrayList<Devotional> getMyList(Context context){
//
//        String monthlyDevotionalsJson = loadJSONFromAsset(context);
//
//        Gson gson = new GsonBuilder().create();
//        MonthDevotional monthDevotionals = gson.fromJson(monthlyDevotionalsJson, MonthDevotional.class);
//
//        return monthDevotionals.getDevotionals();
//    }
    public static String loadJSONFromAsset(Context context){
        String json = null;
        try {
            InputStream is = context.getAssets().open(jsonFileName(context));
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");

        } catch (IOException ex){
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    private static String jsonFileName(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("langSetting", Activity.MODE_PRIVATE);
        String langExt = prefs.getString("My_Lang", "en");
        String fileName = "serverdata_" + langExt + ".json";
        return fileName;
    }

    @Override
    public int compareTo(Devotional o) {
        return this.devotional_id - o.getDevotional_id();
    }

    public static Comparator<Devotional> devotionalComparatorDate = new Comparator<Devotional>() {
        @Override
        public int compare(Devotional o1, Devotional o2) {
            return o1.getDate().compareTo(o2.getDate());
        }
    };



//    public static Devotional getTodaysDevotional(Context context) {
//        Calendar cal = Calendar.getInstance();
//        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
//        String currentDate = dateFormat.format(cal.getTime());
//
//        ArrayList<Devotional> dailyDevotionals = Devotional.getMyList(context);
//        Devotional todaysDevotional = null;
//        for (int i = 0; i < dailyDevotionals.size(); i++){
//            Devotional dailyDevotional = dailyDevotionals.get(i);
//            Log.i(TAG, "Current date: " + currentDate);
//            Log.i(TAG, "Devotional date: " + dailyDevotional.date);
//            if (dailyDevotional.date.contentEquals(currentDate)){
//                todaysDevotional = dailyDevotional;
//                break;
//            }
//        }
//        Log.i(TAG, "Today's Devotional: " + todaysDevotional);
//        return todaysDevotional;
//    }


}
