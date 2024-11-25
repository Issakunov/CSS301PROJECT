package org.example;

import org.example.dao.StudentService;
import org.example.models.Student;

import java.util.List;
import java.util.Scanner;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {

        StudentService studentService = new StudentService();

        Scanner sc = new Scanner(System.in);

        playInstructions();

        boolean programRunning = true;

        // Program runs until it implicitly quite by command 5 or until exception is thrown
        while(programRunning) {
            System.out.print("Enter command: ");
            int command = sc.nextInt();
            System.out.println();
            if (command == 1 ) {
                List<Student> studentList = studentService.getAllStudents();
                for (Student student : studentList) {
                    System.out.println(student);
                }
            } else if (command == 2 ) {
                List<Student> studentList = studentService.getAllSortedStudents();
                for (Student student : studentList) {
                    System.out.println(student);
                }
            } else if (command == 3) {
                System.out.println(studentService.addStudent(sc));
            }else if (command == 4) {
                System.out.print("Enter student id to search: ");
                int studentID = sc.nextInt();
                System.out.println();
                System.out.println(studentService.findStudentById(studentID));
            }else if (command == 5) {
                System.out.print("Enter student id to delete: ");
                int studentID = sc.nextInt();
                System.out.println();
                System.out.println(studentService.deleteStudentByID(studentID));
            }else if (command == 6) {
                System.out.print("Enter student id to update: ");
                int studentID = sc.nextInt();
                if (studentService.findStudentById(studentID).isPresent()) {
                    System.out.println(studentService.updateStudentDetails(studentID, sc));
                }else {
                    System.out.println("No such student with ID: " + studentID);
                }
            }else if (command == 7) {
                System.out.println(studentService.clearAllStudents());
            }else if (command == 8) {
                playInstructions();
            }else if (command == 9) {
                programRunning = false;
            }else {
                System.out.println("Not a valid command, please see the instructions below:");
                playInstructions();
            }

            System.out.println("PROGRAM STOPPED!");
        }
    }
    public static void playInstructions() {
        System.out.println("ATTENTION: Instructions to the program");
        System.out.println("Please select a number to execute a command:");
        System.out.println("1 - get all students");
        System.out.println("2 - get all SORTED students");
        System.out.println("3 - add a new student");
        System.out.println("4 - find student by ID");
        System.out.println("5 - delete student by id");
        System.out.println("6 - update student by id");
        System.out.println("7 - clear students");
        System.out.println("8 - play the instructions again");
        System.out.println("9 - stop the program");
    }
}