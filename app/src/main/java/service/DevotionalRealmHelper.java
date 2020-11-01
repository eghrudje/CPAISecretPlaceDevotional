package service;

import android.content.Context;
import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import io.realm.exceptions.RealmException;

public class DevotionalRealmHelper {
    Realm realm;

    RealmResults<Devotional> realmDevotional;
    Boolean saved = null;
    public static final String TAG = "DevotionalRealmHelper";

    private static DevotionalRealmHelper instance;


    private DevotionalRealmHelper(Context context) {
        Realm.init(context);
        realm = Realm.getDefaultInstance();
    }

    public static DevotionalRealmHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DevotionalRealmHelper(context);
        }
        return instance;
    }

    public void savedDevotional(final Devotional devotionalRealm) {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                try {
                    Number maxId = realm.where(Devotional.class).max("devotional_id");

                    int newKey = (maxId == null) ? 1 : maxId.intValue() + 1;

                    devotionalRealm.setDevotional_id(newKey);

                    realm.copyToRealm(devotionalRealm);

                } catch (RealmException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    public void saveDevotionalNote(final DevotionalNote devotionalNoteRealm) {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                try {
                    Number maxId = realm.where(DevotionalNote.class).max("devotionalNote_id");

                    int newKey = (maxId == null) ? 1 : maxId.intValue() + 1;

                    devotionalNoteRealm.setDevotionalNote_id(newKey);

                    realm.copyToRealm(devotionalNoteRealm);

                } catch (RealmException e) {
                    e.printStackTrace();
                }
            }
        });

    }
    public void updateDevotionalNote(final DevotionalNote devotionalNoteRealmUpdate) {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                try {
//                    int id = devotionalNoteRealmUpdate.getDevotionalNote_id();
//                   DevotionalNote devotionalNote = realm.where(DevotionalNote.class).equalTo("devotionalNote_id", id).findFirst();
                    realm.copyToRealmOrUpdate(devotionalNoteRealmUpdate);

                } catch (RealmException e) {
                    e.printStackTrace();
                }
            }
        });

    }
    public DevotionalNote fetchDevotionalNoteFromDatabase (String date) {
        DevotionalNote devotionalNote = realm.where(DevotionalNote.class).equalTo("devDate", date).findFirst();

        return  devotionalNote;
    }
//    public void retrieveDevotionalFromDatabase() {
//        realmDevotional = realm.where(Devotional.class).findAll();
//    }

    public ArrayList<Devotional> Refresh() {
        ArrayList<Devotional> devotionalListItem = new ArrayList<>();
        for (Devotional devotional : realmDevotional) {
            devotionalListItem.add(devotional);
        }
        return devotionalListItem;

    }

    public void attachDataChangeListener(final DevotionalInterface devotionalInterface) {
        RealmChangeListener realmChangeListener = new RealmChangeListener() {
            @Override
            public void onChange(Object o) {
                devotionalInterface.onDevotionalListener();
            }
        };
        realm.addChangeListener(realmChangeListener);
    }


    public ArrayList<Object> fetchDummyListOfMonthDevotionals() throws ParseException {

        ArrayList<Object> listOfMonthDevotionals = new ArrayList<>();

        Object dayDev = new Devotional(1, "16.08.2020", "Love", "Hello there", "James Arthur", "Jude 20", "Bla bla....", "Rom :1-10",
                "Lorem Ipsum is simply dummy text of the printing and typesetting industry.", "John 4:13-37", "https://upload.wikimedia.org/wikipedia/commons/2/2d/Sunathar_Scenery.jpg", "August2020", "2020");

//        Object monthDev1 = new MonthDevotional("August", R.drawable.august2020, "August2020");
//        Object monthDev2 = new MonthDevotional("July", R.drawable.july2020, "July2020");
//        Object monthDev3 = new MonthDevotional("June", R.drawable.june2020, "June2020");

        Object yearDev1 = new YearDevotional("2020");
        Object yearDev2 = new YearDevotional("2019");
        Object yearDev3 = new YearDevotional("2018");

        listOfMonthDevotionals.add(dayDev);
//        listOfMonthDevotionals.add(monthDev1);
//        listOfMonthDevotionals.add(monthDev2);
//        listOfMonthDevotionals.add(monthDev3);
        listOfMonthDevotionals.add(yearDev1);
        listOfMonthDevotionals.add(yearDev2);
        listOfMonthDevotionals.add(yearDev3);

        return listOfMonthDevotionals;
    }

    public ArrayList<Object> fetchHomePageItems(Object devotional) throws ParseException {
        ArrayList<Object> listOfMonthDevotionals = new ArrayList<>();

        //devotional = fetchTodaysDevotional(todaysDate);

        listOfMonthDevotionals.add(devotional);
        Log.i(TAG, "fetchHomePageItems(Devotional): " + devotional);

        listOfMonthDevotionals.addAll(getMyHomePageMonthDevotionals());
        listOfMonthDevotionals.addAll(myHomePageYearDevotionals());


        return listOfMonthDevotionals;
    }




    public Devotional fetchMonthDevotionalFromDatabase(String currentMonthDate) {
        Log.i(TAG, "fetchMonthDevotionalFromDatabase(Devotional From DB using currentMonthDate): " + realm);

        Devotional devotional = realm.where(Devotional.class).equalTo("monthYearName", currentMonthDate).findFirst();

        return devotional;
    }

    public ArrayList<Devotional> fetchListOfDevotionalsPerMonth(String monthIdentifier) {
        ArrayList<Devotional> getDevotionalsForTheMonth = new ArrayList<>();

        RealmResults<Devotional> realmList = realm.where(Devotional.class).equalTo("monthYearName", monthIdentifier).findAll();
        Log.i(TAG, "fetchListOfDevotionalsPerMonth(RealmList): " + realmList);

        for (Devotional devotional : realmList) {
            getDevotionalsForTheMonth.add(devotional);
        }
        Log.i(TAG, "fetchListOfDevotionalsPerMonth: " + getDevotionalsForTheMonth.toString());
        return getDevotionalsForTheMonth;
    }


    public Devotional fetchDayDevotionalFromDatabase(String currentDate) {
        //Log.i(TAG, "fetchDayDevotionalFromDatabase: " + realm);

        Devotional devotional = realm.where(Devotional.class).equalTo("date", currentDate).findFirst();
        Log.i(TAG, "fetchDayDevotionalFromDatabase(Devotional From DB using current date): " + devotional);

        return devotional;
    }


    public ArrayList<YearDevotional> myHomePageYearDevotionals() {
        ArrayList<YearDevotional> arrayListOfTwoYears = new ArrayList<>();

        DateFormat dateFormat1 = new SimpleDateFormat("yyyy");
        Date year = new Date();
        String currentYearName = dateFormat1.format(year);

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, -1);
        String lastYearName = dateFormat1.format(cal.getTime());

        Log.i(TAG, "myHomePageYearDevotionals: " + currentYearName);

        YearDevotional currentYearDevotional = new YearDevotional();
        currentYearDevotional.setYearName(currentYearName);
        currentYearDevotional.setYearImage("y" + currentYearName);
        Log.i(TAG, "myHomePageYearDevotionals(currentYearDevotional): " + currentYearDevotional);
        arrayListOfTwoYears.add(currentYearDevotional);

        YearDevotional lastYearDevotional = new YearDevotional();
        lastYearDevotional.setYearName(lastYearName);
        lastYearDevotional.setYearImage("y" + lastYearName);
        Log.i(TAG, "myHomePageYearDevotionals(monthDevotionalCurrentMonth): " + lastYearDevotional);
        arrayListOfTwoYears.add(lastYearDevotional);

        return arrayListOfTwoYears;
    }

    public ArrayList<MonthDevotional> getMyHomePageMonthDevotionals() {

        ArrayList<MonthDevotional> arrayListOfThreeMonths = new ArrayList<>();

        DateFormat dateFormat = new SimpleDateFormat("MMMM");
        Date month = new Date();
        String currentMonthName = dateFormat.format(month);

        DateFormat dateFormat1 = new SimpleDateFormat("yyyy");
        Date year = new Date();
        String currentYearName = dateFormat1.format(year);

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -1);
        String lastMonthName = dateFormat.format(cal.getTime());

        Calendar cal1 = Calendar.getInstance();
        cal1.add(Calendar.MONTH, -2);
        String lastTwoMonths = dateFormat.format(cal1.getTime());

        String currentMonthYearName = currentMonthName + currentYearName;
        Log.i(TAG, "getMyHomePageMonthDevotionals: " + currentMonthYearName.toLowerCase());
        String previousMonthYearName = lastMonthName + currentYearName;
        String previousTwoMonthsYearName = lastTwoMonths + currentYearName;

        MonthDevotional monthDevotionalCurrentMonth = new MonthDevotional();

        monthDevotionalCurrentMonth.setMonthName(currentMonthName);
        monthDevotionalCurrentMonth.setMonthImage(currentMonthYearName.toLowerCase());
        monthDevotionalCurrentMonth.setMonthDevotional_id(currentMonthYearName);
        Log.i(TAG, "getMyHomePageMonthDevotionals(monthDevotionalCurrentMonth): " + monthDevotionalCurrentMonth);
        arrayListOfThreeMonths.add(monthDevotionalCurrentMonth);

        MonthDevotional previousMonthDevotional = new MonthDevotional();
        previousMonthDevotional.setMonthName(lastMonthName);
        previousMonthDevotional.setMonthImage(previousMonthYearName.toLowerCase());
        previousMonthDevotional.setMonthDevotional_id(previousMonthYearName);
        Log.i(TAG, "getMyHomePageMonthDevotionals(previousMonthDevotional): " + previousMonthDevotional);
        arrayListOfThreeMonths.add(previousMonthDevotional);

        MonthDevotional previousTwoMonthDevotional = new MonthDevotional();
        previousTwoMonthDevotional.setMonthName(lastTwoMonths);
        previousTwoMonthDevotional.setMonthImage(previousTwoMonthsYearName.toLowerCase());
        previousTwoMonthDevotional.setMonthDevotional_id(previousTwoMonthsYearName);
        Log.i(TAG, "getMyHomePageMonthDevotionals(previousTwoMonthDevotional): " + previousTwoMonthDevotional);
        arrayListOfThreeMonths.add(previousTwoMonthDevotional);

        return arrayListOfThreeMonths;
    }
}
