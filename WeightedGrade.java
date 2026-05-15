package com.grademanager.models;

public class WeightedGrade extends Grade {
    private double weight;
    
    public WeightedGrade(String assignmentName, double score, 
                         double maxScore, double weight) {
        super(assignmentName, score, maxScore);
        this.weight = weight;
    }
    
    public double getWeight() { return weight; }
    
    public double getWeightedScore() {
        return getPercentage() * (weight / 100);
    }
    
    @Override
    public String toString() {
        return super.toString() + String.format(" (Weight: %.0f%%)", weight);
    }
}
