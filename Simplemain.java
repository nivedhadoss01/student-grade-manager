package com.grademanager;

import com.grademanager.models.Student;
import com.grademanager.models.Grade;
import com.grademanager.services.GradeManager;
import java.util.Scanner;

public class SimpleMain {
    private static GradeManager manager = new GradeManager();
    private static Scanner scanner = new Scanner(System.in);
    
    public static void main(String[] args) {
        System.out.println("=== STUDENT GRADE MANAGER ===\n");
        
        // Add sample data
        manager.addStudent("S001", "Alice Johnson", "alice@email.com");
        manager.addStudent("S002", "Bob Smith", "bob@email.com");
        manager.addGradeToStudent("S001", "Math Test", 85, 100);
        manager.addGradeToStudent("S001", "Science", 92, 100);
        manager.addGradeToStudent("S002", "Math Test", 78, 100);
        
        boolean running = true;
        while (running) {
            System.out.println("\n1. View All Students");
            System.out.println("2. Add Grade");
            System.out.println("3. View Student Details");
            System.out.println("4. View Class Stats");
            System.out.println("5. Exit");
            System.out.print("Choice: ");
            
            int choice = Integer.parseInt(scanner.nextLine());
            
            switch (choice) {
                case 1:
                    System.out.println("\nAll Students:");
                    for (Student s : manager.getAllStudents()) {
                        System.out.println("  " + s);
                    }
                    break;
                case 2:
                    System.out.print("Student ID: ");
                    String id = scanner.nextLine();
                    System.out.print("Assignment: ");
                    String assign = scanner.nextLine();
                    System.out.print("Score: ");
                    double score = Double.parseDouble(scanner.nextLine());
                    System.out.print("Max Score: ");
                    double max = Double.parseDouble(scanner.nextLine());
                    manager.addGradeToStudent(id, assign, score, max);
                    System.out.println("Grade added!");
                    break;
                case 3:
                    System.out.print("Student ID: ");
                    String sid = scanner.nextLine();
                    Student s = manager.getStudent(sid);
                    System.out.println("\n" + s);
                    System.out.println("Grades:");
                    for (Grade g : s.getGrades()) {
                        System.out.println("  - " + g);
                    }
                    break;
                case 4:
                    var stats = manager.getClassStatistics();
                    System.out.printf("\nClass Average: %.2f%%\n", stats.get("average"));
                    System.out.printf("Highest: %.2f%%\n", stats.get("highest"));
                    System.out.printf("Lowest: %.2f%%\n", stats.get("lowest"));
                    break;
                case 5:
                    running = false;
                    break;
            }
        }
        System.out.println("Goodbye!");
        scanner.close();
    }
}
