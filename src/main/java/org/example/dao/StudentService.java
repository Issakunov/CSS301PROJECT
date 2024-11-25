package org.example.dao;

import org.example.exception.StudentNotFoundException;
import org.example.models.Student;

import java.io.*;
import java.util.*;

public class StudentService {
    private final List<Student> studentDataBase;

    public StudentService() {
        this.studentDataBase = new ArrayList<>();
    }

    // Add a student
    public String addStudent(Scanner scanner) {
        try {
            scanner.nextLine(); // Consume any leftover newline

            System.out.print("Enter first name: ");
            String firstName = scanner.nextLine();
            System.out.println();

            System.out.print("Enter last name: ");
            String lastName = scanner.nextLine();
            System.out.println();

            System.out.print("Enter age: ");
            int age = scanner.nextInt();
            scanner.nextLine(); // Consume the leftover newline after reading an integer
            System.out.println();

            System.out.print("Enter course: ");
            String course = scanner.nextLine();
            System.out.println();

            int studentID = 1;
            while (findStudentById(studentID).isPresent()) {
                studentID++;
            }

            studentDataBase.add(new Student(studentID, firstName, lastName, age, course));
//            saveStudentsToFile();
            return "Student added successfully";
        } catch (InputMismatchException e) {
            return "Invalid input! Please enter correct data types.";
        } catch (Exception e) {
            return "Student was not added due to an unexpected error!";
        }
    }

    // Clear all students
    public String clearAllStudents() {
        try {
            studentDataBase.clear();
//          saveStudentsToFile();
            return "All student records have been cleared successfully.";
        } catch (InputMismatchException e) {
        return "Error clearing students : " + e.getMessage();
    }
    }


    // View all students
    public List<Student> getAllStudents() {
        return new ArrayList<>(studentDataBase);
    }

    // View all SORTED students
    public List<Student> getAllSortedStudents() {
        Collections.sort(studentDataBase);
        return new ArrayList<>(studentDataBase);
    }

    // Find student by ID
    public Optional<Student> findStudentById(int id) {
        return studentDataBase.stream()
                .filter(student -> student.getStudentID() == id)
                .findFirst();
    }

    // Update student details
    public String updateStudentDetails(int id, Scanner scanner) {
        Optional<Student> studentOpt = findStudentById(id);
        if (studentOpt.isPresent()) {

            scanner.nextLine(); // Consume any leftover newline from previous input

            System.out.print("Enter updated firstname: ");
            String updatedFirstName = scanner.nextLine();
            System.out.println();

            System.out.print("Enter updated last name: ");
            String updatedLastName = scanner.nextLine();
            System.out.println();

            System.out.print("Enter updated age: ");
            int updatedAge = scanner.nextInt();
            scanner.nextLine(); // Consume the leftover newline after reading an integer
            System.out.println();

            System.out.print("Enter updated course: ");
            String updatedCourse = scanner.nextLine();
            System.out.println();

            Student existingStudent = studentOpt.get();
            existingStudent.setFirstName(updatedFirstName);
            existingStudent.setLastName(updatedLastName);
            existingStudent.setAge(updatedAge);
            existingStudent.setCourse(updatedCourse);

//            saveStudentsToFile(); // Save updated list to file
            return "Student details updated successfully";
        } else {
            return "Student with ID " + id + " not found!";
        }
    }


    // Delete a student
    public String deleteStudentByID(int id) {
        try {
            Optional<Student> studentOpt = findStudentById(id);
            if (studentOpt.isPresent()) {
                studentDataBase.remove(studentOpt.get());
                saveStudentsToFile(); // Save updated list to file
                return "Student deleted successfully";
            } else {
                throw new StudentNotFoundException("Student with ID " + id + " not found!");
            }
        } catch (StudentNotFoundException e) {
            return e.getMessage();
        }
    }

    // Save students to file
    // Save students to a text file
    private void saveStudentsToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("students.txt"))) {
            for (Student student : studentDataBase) {
                writer.write(formatStudentData(student));
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving students to file: " + e.getMessage());
        }
    }

    /*
     * This method is not in use after switching to project phase 2
     * */
    // Load students from a text file
    private List<Student> loadStudentsFromFile() {
        List<Student> students = new ArrayList<>();
        File file = new File("students.txt");
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    Student student = parseStudentData(line);
                    students.add(student);
                }
            } catch (IOException e) {
                System.err.println("Error loading students from file: " + e.getMessage());
            }
        }
        return students; // Return loaded list of students
    }

    /*
     * This method is not in use after switching to project phase 2
     * */
    // Helper method to format a student's data as a string
    private String formatStudentData(Student student) {
        return String.format("%d,%s,%s,%d,%s",
                student.getStudentID(), student.getFirstName(), student.getLastName(), student.getAge(), student.getCourse());
    }

    /*
    * This method is not in use after switching to project phase 2
    * */
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
