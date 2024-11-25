package org.example.exception;

public class StudentNotFoundException extends Exception {

    public StudentNotFoundException(String s) {
        super(s);
    }

    public StudentNotFoundException() {
        super("Student not found");
    }
}
