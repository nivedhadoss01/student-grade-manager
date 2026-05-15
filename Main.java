package com.grademanager;

import com.grademanager.models.Student;
import com.grademanager.services.GradeManager;
import com.grademanager.services.FileHandler;
import com.grademanager.utils.Validator;
import java.util.Scanner;

public class Main {
    private static GradeManager manager;
    private static FileHandler fileHandler;
    private static Scanner scanner;
    
    public static void main(String[] args) {
        manager = new GradeManager();
        fileHandler = new FileHandler();
        scanner = new Scanner(System.in);
        
        System.out.println("=================================");
        System.out.println("   STUDENT GRADE MANAGER v1.0");
        System.out.println("=================================");
        
        // Load existing data
        GradeManager loaded = fileHandler.loadData();
        if (loaded != null) {
            manager = loaded;
            System.out.println("✓ Loaded " + manager.getTotalStudentCount() + " existing students");
        } else {
            System.out.println("✓ Starting with empty database");
            addSampleData();
        }
        
        boolean running = true;
        while (running) {
            printMenu();
            int choice = getIntInput("\nEnter your choice: ");
            
            switch (choice) {
                case 1 -> addStudent();
                case 2 -> viewAllStudents();
                case 3 -> addGrade();
                case 4 -> viewStudentDetails();
                case 5 -> viewClassStatistics();
                case 6 -> viewTopPerformers();
                case 7 -> removeStudent();
                case 8 -> {
                    fileHandler.saveData(manager);
                    System.out.println("✓ Data saved. Goodbye!");
                    running = false;
                }
                default -> System.out.println("✗ Invalid choice! Please enter 1-8");
            }
        }
        scanner.close();
    }
    
    private static void printMenu() {
        System.out.println("\n┌─────────────────────────────────┐");
        System.out.println("│            M E N U              │");
        System.out.println("├─────────────────────────────────┤");
        System.out.println("│ 1. Add Student                  │");
        System.out.println("│ 2. View All Students            │");
        System.out.println("│ 3. Add Grade to Student         │");
        System.out.println("│ 4. View Student Details         │");
        System.out.println("│ 5. View Class Statistics        │");
        System.out.println("│ 6. View Top Performers          │");
        System.out.println("│ 7. Remove Student               │");
        System.out.println("│ 8. Save & Exit                  │");
        System.out.println("└─────────────────────────────────┘");
    }
    
    private static void addSampleData() {
        System.out.println("\n📚 Adding sample data for demonstration...");
        manager.addStudent("S001", "Alice Johnson", "alice@example.com");
        manager.addStudent("S002", "Bob Smith", "bob@example.com");
        manager.addStudent("S003", "Carol Davis", "carol@example.com");
        
        manager.addGradeToStudent("S001", "Math Exam", 85, 100);
        manager.addGradeToStudent("S001", "Science Project", 92, 100);
        manager.addGradeToStudent("S002", "Math Exam", 78, 100);
        manager.addGradeToStudent("S003", "Math Exam", 95, 100);
        
        System.out.println("✓ Added 3 sample students with grades");
    }
    
    private static void addStudent() {
        System.out.println("\n--- Add New Student ---");
        
        String id = getStringInput("Student ID (3-10 chars, letters/numbers): ");
        if (!Validator.isValidStudentId(id)) {
            System.out.println("✗ Invalid ID format!");
            return;
        }
        
        String name = getStringInput("Full Name: ");
        if (!Validator.isValidName(name)) {
            System.out.println("✗ Invalid name!");
            return;
        }
        
        String email = getStringInput("Email: ");
        if (!Validator.isValidEmail(email)) {
            System.out.println("✗ Invalid email format!");
            return;
        }
        
        try {
            manager.addStudent(id, name, email);
            System.out.println("✓ Student added successfully!");
        } catch (Exception e) {
            System.out.println("✗ Error: " + e.getMessage());
        }
    }
    
    private static void viewAllStudents() {
        System.out.println("\n--- All Students ---");
        var students = manager.getAllStudents();
        if (students.isEmpty()) {
            System.out.println("📭 No students found.");
        } else {
            System.out.println("\nID    | Name                | Email                    | Average");
            System.out.println("------+---------------------+--------------------------+--------");
            for (Student s : students) {
                System.out.printf("%-5s | %-19s | %-24s | %.2f (%s)%n",
                    s.getId(), 
                    truncate(s.getName(), 19),
                    truncate(s.getEmail(), 24),
                    s.calculateAverage(),
                    s.getLetterGrade());
            }
        }
    }
    
    private static void addGrade() {
        System.out.println("\n--- Add Grade ---");
        viewAllStudents();
        
        String id = getStringInput("\nEnter Student ID: ");
        String assignment = getStringInput("Assignment Name: ");
        double score = getDoubleInput("Score achieved: ");
        double maxScore = getDoubleInput("Maximum possible score: ");
        
        if (!Validator.isValidScore(score, maxScore)) {
            System.out.println("✗ Invalid score! Must be between 0 and " + maxScore);
            return;
        }
        
        try {
            manager.addGradeToStudent(id, assignment, score, maxScore);
            System.out.println("✓ Grade added successfully!");
        } catch (Exception e) {
            System.out.println("✗ Error: " + e.getMessage());
        }
    }
    
    private static void viewStudentDetails() {
        System.out.println("\n--- Student Details ---");
        String id = getStringInput("Enter Student ID: ");
        
        try {
            Student student = manager.getStudent(id);
            System.out.println("\n╔════════════════════════════════════════╗");
            System.out.println("║ Student Information                    ║");
            System.out.println("╠════════════════════════════════════════╣");
            System.out.printf("║ ID:      %-30s║%n", student.getId());
            System.out.printf("║ Name:    %-30s║%n", student.getName());
            System.out.printf("║ Email:   %-30s║%n", student.getEmail());
            System.out.printf("║ Average: %-30s║%n", String.format("%.2f (%s)", 
                student.calculateAverage(), student.getLetterGrade()));
            System.out.println("╠════════════════════════════════════════╣");
            System.out.println("║ Grades                                  ║");
            System.out.println("╠════════════════════════════════════════╣");
            
            if (student.getGrades().isEmpty()) {
                System.out.println("║  No grades recorded yet               ║");
            } else {
                for (Grade g : student.getGrades()) {
                    String gradeLine = String.format("  %s: %.1f/%.1f", 
                        g.getAssignmentName(), g.getScore(), g.getMaxScore());
                    System.out.printf("║ %-38s║%n", gradeLine);
                }
            }
            System.out.println("╚════════════════════════════════════════╝");
        } catch (Exception e) {
            System.out.println("✗ Error: " + e.getMessage());
        }
    }
    
    private static void viewClassStatistics() {
        System.out.println("\n--- Class Statistics ---");
        var stats = manager.getClassStatistics();
        
        System.out.println("\n┌────────────────────────────┐");
        System.out.println("│     CLASS PERFORMANCE      │");
        System.out.println("├────────────────────────────┤");
        System.out.printf("│ Total Students: %-11d│%n", stats.get("totalStudents").intValue());
        System.out.printf("│ Class Average: %-11.2f│%n", stats.get("average"));
        System.out.printf("│ Highest Grade: %-11.2f│%n", stats.get("highest"));
        System.out.printf("│ Lowest Grade:  %-11.2f│%n", stats.get("lowest"));
        System.out.println("└────────────────────────────┘");
        
        // Grade distribution
        System.out.println("\nGrade Distribution:");
        var students = manager.getAllStudents();
        int a = 0, b = 0, c = 0, d = 0, f = 0;
        for (Student s : students) {
            switch (s.getLetterGrade()) {
                case "A" -> a++;
                case "B" -> b++;
                case "C" -> c++;
                case "D" -> d++;
                case "F" -> f++;
            }
        }
        
        System.out.println("  A: " + repeatChar('█', a) + " (" + a + ")");
        System.out.println("  B: " + repeatChar('█', b) + " (" + b + ")");
        System.out.println("  C: " + repeatChar('█', c) + " (" + c + ")");
        System.out.println("  D: " + repeatChar('█', d) + " (" + d + ")");
        System.out.println("  F: " + repeatChar('█', f) + " (" + f + ")");
    }
    
    private static void viewTopPerformers() {
        System.out.println("\n--- Top 3 Performers ---");
        var topStudents = manager.getTopPerformers(3);
        if (topStudents.isEmpty()) {
            System.out.println("No students found.");
        } else {
            System.out.println("\n🏆 TOP PERFORMERS 🏆");
            for (int i = 0; i < topStudents.size(); i++) {
                Student s = topStudents.get(i);
                String medal = i == 0 ? "🥇" : (i == 1 ? "🥈" : "🥉");
                System.out.printf("%s %d. %-20s %.2f%% (%s)%n", 
                    medal, i+1, s.getName(), s.calculateAverage(), s.getLetterGrade());
            }
        }
    }
    
    private static void removeStudent() {
        System.out.println("\n--- Remove Student ---");
        viewAllStudents();
        String id = getStringInput("\nEnter Student ID to remove: ");
        
        try {
            Student student = manager.getStudent(id);
            System.out.print("Are you sure you want to remove " + student.getName() + "? (yes/no): ");
            String confirm = scanner.nextLine();
            if (confirm.equalsIgnoreCase("yes")) {
                manager.removeStudent(id);
                System.out.println("✓ Student removed successfully!");
            } else {
                System.out.println("Operation cancelled.");
            }
        } catch (Exception e) {
            System.out.println("✗ Error: " + e.getMessage());
        }
    }
    
    // Helper methods
    private static String getStringInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }
    
    private static int getIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }
    
    private static double getDoubleInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Double.parseDouble(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }
    
    private static String truncate(String str, int length) {
        if (str == null) return "";
        if (str.length() <= length) return str;
        return str.substring(0, length - 3) + "...";
    }
    
    private static String repeatChar(char c, int count) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count && i < 20; i++) {
            sb.append(c);
        }
        return sb.toString();
    }
}
