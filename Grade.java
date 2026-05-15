package com.grademanager.models;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Grade implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String assignmentName;
    private double score;
    private double maxScore;
    private LocalDateTime dateRecorded;
    
    public Grade(String assignmentName, double score, double maxScore) {
        if (score < 0 || score > maxScore) {
            throw new IllegalArgumentException("Score must be between 0 and " + maxScore);
        }
        this.assignmentName = assignmentName;
        this.score = score;
        this.maxScore = maxScore;
        this.dateRecorded = LocalDateTime.now();
    }
    
    public String getAssignmentName() { return assignmentName; }
    public double getScore() { return score; }
    public double getMaxScore() { return maxScore; }
    public double getPercentage() { return (score / maxScore) * 100; }
    public LocalDateTime getDateRecorded() { return dateRecorded; }
    
    @Override
    public String toString() {
        return String.format("%s: %.1f/%.1f (%.1f%%) on %s", 
            assignmentName, score, maxScore, getPercentage(), 
            dateRecorded.toLocalDate());
    }
}
