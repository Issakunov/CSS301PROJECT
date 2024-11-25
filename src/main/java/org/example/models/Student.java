package org.example.models;

import java.util.InputMismatchException;

//Simple Student POJO class
public class Student implements Comparable<Student> {
    private int studentID;
    private String firstName;
    private String lastName;
    private int age;
    private String course;

    public Student(int studentID, String firstName, String lastName, int age, String course) {
        this.studentID = studentID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.course = course;
    }

    public Student() {
    }

    public int getStudentID() {
        return studentID;
    }

    public void setStudentID(int studentID) {
        this.studentID = studentID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        if (!firstName.matches("[A-Za-z]+")) {
            throw new InputMismatchException("Invalid first name: Only alphabetic characters are allowed.");
        }
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        if (!lastName.matches("[A-Za-z]+")) {
            throw new InputMismatchException("Invalid last name: Only alphabetic characters are allowed.");
        }
        this.lastName = lastName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        if (age <= 0 || age > 150) {
            throw new InputMismatchException("Invalid age: Age must be a positive number and less than 150.");
        }
        this.age = age;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        if (course == null || course.trim().isEmpty()) {
            throw new InputMismatchException("Invalid course: Course name cannot be null or empty.");
        }
        this.course = course;
    }

    @Override
    public String toString() {
        return "Student{" +
                "studentID=" + studentID +", firstName='" + firstName + ", lastName='" + lastName + ", age=" + age + ", course='" + course +'}';
    }

    @Override
    public int compareTo(Student o) {
        int lastNameComparison = this.lastName.compareTo(o.lastName);
        if (lastNameComparison !=0) {
            return lastNameComparison;
        }
        return Integer.compare(this.studentID, o.studentID);
    }
}
