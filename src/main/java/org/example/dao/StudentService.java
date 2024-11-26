package org.example.dao;

import org.example.exception.StudentNotFoundException;
import org.example.models.Student;

import javax.swing.*;
import java.io.*;
import java.util.*;

public class StudentService {
    private List<Student> studentDatabase = new ArrayList<>();

    public StudentService() {
        loadStudentsFromFile();
    }

    // Add a student
    public synchronized String addStudent(Student student) {
        try {
            int studentID = generateUniqueID();
            student.setStudentID(studentID);

            studentDatabase.add(student);
            saveStudentsToFile();
            return "Student added successfully.";
        } catch (Exception e) {
            return "Student was not added due to an unexpected error: " + e.getMessage();
        }
    }

    // Generate a unique ID for new students
    private int generateUniqueID() {
        int studentID = 1;
        while (findStudentById(studentID).isPresent()) {
            studentID++;
        }
        return studentID;
    }

    // Clear all students
    public synchronized String clearAllStudents() {
        try {
            studentDatabase.clear();
            saveStudentsToFile();
            return "All student records have been cleared successfully.";
        } catch (Exception e) {
            return "Error clearing students: " + e.getMessage();
        }
    }

    // View all students
    public synchronized List<Student> getAllStudents() {
        return new ArrayList<>(studentDatabase);
    }

    // View all sorted students
    public synchronized List<Student> getAllSortedStudents() {
        List<Student> sortedStudents = new ArrayList<>(studentDatabase);
        sortedStudents.sort(Comparator.comparing(Student::getStudentID));
        return sortedStudents;
    }

    // Find student by ID
    public synchronized Optional<Student> findStudentById(int id) {
        return studentDatabase.stream()
                .filter(student -> student.getStudentID() == id)
                .findFirst();
    }

    // Update student details
    public synchronized String updateStudentDetails(int id, String firstName, String lastName, int age, String course) {
        Optional<Student> studentOpt = findStudentById(id);
        if (studentOpt.isPresent()) {
            Student existingStudent = studentOpt.get();
            existingStudent.setFirstName(firstName);
            existingStudent.setLastName(lastName);
            existingStudent.setAge(age);
            existingStudent.setCourse(course);

            saveStudentsToFile();
            return "Student details updated successfully.";
        } else {
            return "Student with ID " + id + " not found!";
        }
    }

    // Delete a student
    public synchronized String deleteStudentByID(int id) {
        try {
            Optional<Student> studentOpt = findStudentById(id);
            if (studentOpt.isPresent()) {
                studentDatabase.remove(studentOpt.get());
                saveStudentsToFile();
                return "Student deleted successfully.";
            } else {
                throw new StudentNotFoundException("Student with ID " + id + " not found!");
            }
        } catch (StudentNotFoundException e) {
            return e.getMessage();
        }
    }

    // Save students to a text file
    private synchronized void saveStudentsToFile() {
        new Thread(() -> {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("students.txt"))) {
                for (Student student : studentDatabase) {
                    writer.write(formatStudentData(student));
                    writer.newLine();
                }
                System.out.println("Students saved to file successfully.");
            } catch (IOException e) {
                System.err.println("Error saving students to file: " + e.getMessage());
            }
        }).start();
    }


    // Load students from a text file
    private synchronized void loadStudentsFromFile() {
        new Thread(() -> {
            studentDatabase.clear();
            File file = new File("students.txt");
            if (file.exists()) {
                try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        Student student = parseStudentData(line);
                        studentDatabase.add(student);
                    }
                    System.out.println("Students loaded from file successfully.");
                } catch (IOException e) {
                    System.err.println("Error loading students from file: " + e.getMessage());
                }
            } else {
                System.out.println("File does not exist. No students to load.");
            }
        }).start();
    }


    // Helper method to format a student's data as a string
    private String formatStudentData(Student student) {
        return String.format("%d,%s,%s,%d,%s",
                student.getStudentID(), student.getFirstName(), student.getLastName(), student.getAge(), student.getCourse());
    }

    // Helper method to parse a student's data from a formatted string
    private Student parseStudentData(String data) {
        String[] parts = data.split(",");
        int id = Integer.parseInt(parts[0]);
        String firstName = parts[1];
        String lastName = parts[2];
        int age = Integer.parseInt(parts[3]);
        String course = parts[4];
        return new Student(id, firstName, lastName, age, course);
    }
}
