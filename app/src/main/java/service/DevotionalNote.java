package service;

import java.util.Comparator;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class DevotionalNote extends RealmObject implements Comparable<DevotionalNote>{

    @PrimaryKey
    private int devotionalNote_id;
    private String devDate;
    private String devTitle;
    private String devotionalNoteTitle;
    private String devotionalNoteContent;
    private String noteCreatedAtDate;
    private String noteCreatedAtTime;
    private String noteUpdatedAtDate;
    private String noteUpdatedAtTime;


    public String getDevTitle() {
        return devTitle;
    }

    public void setDevTitle(String devTitle) {
        this.devTitle = devTitle;
    }

    public int getDevotionalNote_id() {
        return devotionalNote_id;
    }

    public void setDevotionalNote_id(int devotionalNote_id) {
        this.devotionalNote_id = devotionalNote_id;
    }

    public String getDevDate() {
        return devDate;
    }

    public void setDevDate(String devDate) {
        this.devDate = devDate;
    }

    public String getNoteCreatedAtTime() {
        return noteCreatedAtTime;
    }

    public void setNoteCreatedAtTime(String noteCreatedAtTime) {
        this.noteCreatedAtTime = noteCreatedAtTime;
    }

    public String getNoteUpdatedAtTime() {
        return noteUpdatedAtTime;
    }

    public void setNoteUpdatedAtTime(String noteUpdatedAtTime) {
        this.noteUpdatedAtTime = noteUpdatedAtTime;
    }

    public String getDevotionalNoteTitle() {
        return devotionalNoteTitle;
    }

    public void setDevotionalNoteTitle(String devotionalNoteTitle) {
        this.devotionalNoteTitle = devotionalNoteTitle;
    }

    public String getDevotionalNoteContent() {
        return devotionalNoteContent;
    }

    public void setDevotionalNoteContent(String devotionalNoteContent) {
        this.devotionalNoteContent = devotionalNoteContent;
    }

    public String getNoteCreatedAtDate() {
        return noteCreatedAtDate;
    }

    public void setNoteCreatedAtDate(String noteCreatedAtDate) {
        this.noteCreatedAtDate = noteCreatedAtDate;
    }

    public String getNoteUpdatedAtDate() {
        return noteUpdatedAtDate;
    }

    public void setNoteUpdatedAtDate(String noteUpdatedAtDate) {
        this.noteUpdatedAtDate = noteUpdatedAtDate;
    }

    @Override
    public int compareTo(DevotionalNote o) {
        return this.devotionalNote_id - o.getDevotionalNote_id();
    }
    public static Comparator<DevotionalNote> devotionalNoteComparatorDate = new Comparator<DevotionalNote>() {
        @Override
        public int compare(DevotionalNote o1, DevotionalNote o2) {
            return o1.getDevDate().compareTo(o2.getDevDate());
        }

    };
}
