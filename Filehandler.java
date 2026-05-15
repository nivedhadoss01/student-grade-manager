package com.grademanager.services;

import java.io.*;

public class FileHandler {
    private static final String SAVE_FILE = "grades_data.ser";
    
    public void saveData(GradeManager manager) {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(SAVE_FILE))) {
            oos.writeObject(manager);
            System.out.println("✓ Data saved successfully to " + SAVE_FILE);
        } catch (IOException e) {
            System.err.println("✗ Error saving data: " + e.getMessage());
        }
    }
    
    public GradeManager loadData() {
        File file = new File(SAVE_FILE);
        if (!file.exists()) {
            return null;
        }
        
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(SAVE_FILE))) {
            return (GradeManager) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("✗ Error loading data: " + e.getMessage());
            return null;
        }
    }
    
    public boolean dataFileExists() {
        return new File(SAVE_FILE).exists();
    }
}
