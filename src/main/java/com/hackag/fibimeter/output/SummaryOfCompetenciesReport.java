package com.hackag.fibimeter.output;

import java.util.List;

/**
 * Created by Steve on 26.10.2017.
 */
public class SummaryOfCompetenciesReport {

    private String personName;
    private List<CompetencyReport> competencyReports;

    public SummaryOfCompetenciesReport(String personName, List<CompetencyReport> competencyReports) {
        this.personName = personName;
        this.competencyReports = competencyReports;
    }

    public String getPersonName() {
        return personName;
    }

    public List<CompetencyReport> getCompetencyReports() {
        return competencyReports;
    }
}
