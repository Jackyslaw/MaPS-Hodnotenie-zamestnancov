package com.hackag.fibimeter.output;

/**
 * Created by Steve on 26.10.2017.
 */
public class CompetencyReport {

    private String competencyName;
    private String competencyDescription;
    private String yourSelfScore;
    private String feedbackScore;

    public CompetencyReport(String competencyName, String competencyDescription, String yourSelfScore, String feedbackScore) {
        this.competencyName = competencyName;
        this.competencyDescription = competencyDescription;
        this.yourSelfScore = yourSelfScore;
        this.feedbackScore = feedbackScore;
    }

    public String getCompetencyName() {
        return competencyName;
    }

    public String getCompetencyDescription() {
        return competencyDescription;
    }

    public String getYourSelfScore() {
        return yourSelfScore;
    }

    public String getFeedbackScore() {
        return feedbackScore;
    }
}
