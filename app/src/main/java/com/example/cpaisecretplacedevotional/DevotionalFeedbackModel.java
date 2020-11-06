package com.example.cpaisecretplacedevotional;

public class DevotionalFeedbackModel {
    private String name;
    private String feedback;
    private String timeDateSent;

    public String getTimeDateSent() {
        return timeDateSent;
    }

    public void setTimeDateSent(String timeDateSent) {
        this.timeDateSent = timeDateSent;
    }

    public DevotionalFeedbackModel(String name, String feedback, String timeDateSent) {
        this.name = name;
        this.feedback = feedback;
        this.timeDateSent = timeDateSent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }
}
