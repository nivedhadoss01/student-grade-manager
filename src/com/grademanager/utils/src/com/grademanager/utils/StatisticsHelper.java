package com.grademanager.utils;

import com.grademanager.models.Student;
import com.grademanager.models.Grade;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class StatisticsHelper {
    
    // Calculate median grade
    public static double calculateMedian(List<Student> students) {
        if (students.isEmpty()) return 0.0;
        
        List<Double> averages = students.stream()
            .map(Student::calculateAverage)
            .sorted()
            .toList();
        
        int size = averages.size();
        if (size % 2 == 0) {
            return (averages.get(size/2 - 1) + averages.get(size/2)) / 2.0;
        } else {
            return averages.get(size/2);
        }
    }
    
    // Count students who are passing (grade >= 60)
    public static long countPassingStudents(List<Student> students) {
        return students.stream()
            .filter(s -> s.calculateAverage() >= 60)
            .count();
    }
    
    // Get most common grade letter
    public static String getMostCommonGrade(List<Student> students) {
        Map<String, Integer> gradeCount = new HashMap<>();
        for (Student s : students) {
            String grade = s.getLetterGrade().replaceAll("[^A-F]", ""); // Remove emojis
            gradeCount.put(grade, gradeCount.getOrDefault(grade, 0) + 1);
        }
        
        return gradeCount.entrySet().stream()
            .max(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey)
            .orElse("N/A");
    }
}
