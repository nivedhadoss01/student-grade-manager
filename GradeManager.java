package com.grademanager.services;

import com.grademanager.models.Student;
import com.grademanager.models.Grade;
import java.io.Serializable;
import java.util.*;

public class GradeManager implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Map<String, Student> students;
    
    public GradeManager() {
        this.students = new HashMap<>();
    }
    
    // Student CRUD operations
    public void addStudent(String id, String name, String email) {
        if (students.containsKey(id)) {
            throw new IllegalStateException("Student with ID " + id + " already exists");
        }
        Student student = new Student(id, name, email);
        students.put(id, student);
    }
    
    public Student getStudent(String id) {
        Student student = students.get(id);
        if (student == null) {
            throw new NoSuchElementException("Student not found with ID: " + id);
        }
        return student;
    }
    
    public List<Student> getAllStudents() {
        return new ArrayList<>(students.values());
    }
    
    public void removeStudent(String id) {
        if (students.remove(id) == null) {
            throw new NoSuchElementException("Student not found with ID: " + id);
        }
    }
    
    public void updateStudentEmail(String id, String newEmail) {
        Student student = getStudent(id);
        student.setEmail(newEmail);
    }
    
    // Grade operations
    public void addGradeToStudent(String studentId, String assignmentName, 
                                  double score, double maxScore) {
        Student student = getStudent(studentId);
        Grade grade = new Grade(assignmentName, score, maxScore);
        student.addGrade(grade);
    }
    
    public List<Student> getTopPerformers(int count) {
        return students.values().stream()
            .sorted((s1, s2) -> Double.compare(
                s2.calculateAverage(), s1.calculateAverage()))
            .limit(count)
            .toList();
    }
    
    public Map<String, Double> getClassStatistics() {
        DoubleSummaryStatistics stats = students.values().stream()
            .mapToDouble(Student::calculateAverage)
            .summaryStatistics();
        
        Map<String, Double> statistics = new HashMap<>();
        statistics.put("average", stats.getAverage());
        statistics.put("highest", stats.getMax());
        statistics.put("lowest", stats.getMin());
        statistics.put("totalStudents", (double) stats.getCount());
        
        return statistics;
    }
    
    public int getTotalStudentCount() {
        return students.size();
    }
}
