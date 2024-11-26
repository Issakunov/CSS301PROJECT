package org.example;

import org.example.dao.StudentService;
import org.example.models.Student;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class StudentManagementGUI {
    private final StudentService studentService;

    public StudentManagementGUI() {
        studentService = new StudentService();
        createAndShowGUI();
    }

    private void createAndShowGUI() {
        JFrame frame = new JFrame("Student Management System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 500);
        frame.setLayout(new BorderLayout());

        // Top Panel for buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 7));

        JButton viewAllButton = new JButton("View All Students");
        JButton addButton = new JButton("Add Student");
        JButton deleteButton = new JButton("Delete Student");
        JButton searchButton = new JButton("Search Student");
        JButton updateButton = new JButton("Update Student");
        JButton sortButton = new JButton("Sort Students");
        JButton exitButton = new JButton("Exit");

        buttonPanel.add(viewAllButton);
        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(searchButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(sortButton);
        buttonPanel.add(exitButton);

        // Center Panel for displaying output
        JPanel tablePanel = new JPanel(new BorderLayout());
        JTable studentTable = new JTable();
        studentTable.setEnabled(false); // Make the table read-only
        JScrollPane tableScrollPane = new JScrollPane(studentTable);
        tablePanel.add(tableScrollPane, BorderLayout.CENTER);

        // Add components to the frame
        frame.add(buttonPanel, BorderLayout.NORTH);
        frame.add(tablePanel, BorderLayout.CENTER);

        // Action Listeners for buttons
        viewAllButton.addActionListener(e -> {
            List<Student> students = studentService.getAllStudents();
            if (students.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "No students found.");
            } else {
                updateTable(studentTable, students);
            }
        });

        searchButton.addActionListener(e -> {
            String id = JOptionPane.showInputDialog(frame, "Enter Student ID to search:");
            if (id != null && validateID(id)) {
                int studentId = Integer.parseInt(id);
                studentService.findStudentById(studentId).ifPresentOrElse(
                        student -> updateTable(studentTable, List.of(student)),
                        () -> JOptionPane.showMessageDialog(frame, "Student not found.")
                );
            }
        });

        addButton.addActionListener(e -> {
            JTextField firstNameField = new JTextField();
            JTextField lastNameField = new JTextField();
            JTextField ageField = new JTextField();
            JTextField courseField = new JTextField();

            Object[] inputFields = {
                    "First Name:", firstNameField,
                    "Last Name:", lastNameField,
                    "Age:", ageField,
                    "Course:", courseField
            };

            int option = JOptionPane.showConfirmDialog(frame, inputFields, "Add New Student", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                String firstName = firstNameField.getText().trim();
                String lastName = lastNameField.getText().trim();
                String ageText = ageField.getText().trim();
                String course = courseField.getText().trim();

                if (validateName(firstName) && validateName(lastName) && validateAge(ageText) && validateCourse(course)) {
                    int age = Integer.parseInt(ageText);
                    studentService.addStudent(new Student(0, firstName, lastName, age, course));
                    JOptionPane.showMessageDialog(frame, "Student added successfully.");
                } else {
                    JOptionPane.showMessageDialog(frame, "Invalid input. Please correct the errors and try again.");
                }
            }
        });

        sortButton.addActionListener(e -> {
            List<Student> sortedStudents = studentService.getAllSortedStudents();
            if (sortedStudents.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "No students to sort.");
            } else {
                updateTable(studentTable, sortedStudents);
            }
        });

        deleteButton.addActionListener(e -> {
            String id = JOptionPane.showInputDialog(frame, "Enter Student ID to delete:");
            if (id != null && validateID(id)) {
                int studentId = Integer.parseInt(id);
                String result = studentService.deleteStudentByID(studentId);
                JOptionPane.showMessageDialog(frame, result);
            }
        });

        updateButton.addActionListener(e -> {
            String id = JOptionPane.showInputDialog(frame, "Enter Student ID to update:");
            if (id != null && validateID(id)) {
                int studentId = Integer.parseInt(id);
                if (studentService.findStudentById(studentId).isPresent()) {
                    JTextField firstNameField = new JTextField();
                    JTextField lastNameField = new JTextField();
                    JTextField ageField = new JTextField();
                    JTextField courseField = new JTextField();

                    Object[] inputFields = {
                            "Updated First Name:", firstNameField,
                            "Updated Last Name:", lastNameField,
                            "Updated Age:", ageField,
                            "Updated Course:", courseField
                    };

                    int option = JOptionPane.showConfirmDialog(frame, inputFields, "Update Student", JOptionPane.OK_CANCEL_OPTION);
                    if (option == JOptionPane.OK_OPTION) {
                        String firstName = firstNameField.getText().trim();
                        String lastName = lastNameField.getText().trim();
                        String ageText = ageField.getText().trim();
                        String course = courseField.getText().trim();

                        if (validateName(firstName) && validateName(lastName) && validateAge(ageText) && validateCourse(course)) {
                            int age = Integer.parseInt(ageText);
                            String result = studentService.updateStudentDetails(studentId, firstName, lastName, age, course);
                            JOptionPane.showMessageDialog(frame, result);
                        } else {
                            JOptionPane.showMessageDialog(frame, "Invalid input. Please correct the errors and try again.");
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(frame, "Student not found.");
                }
            }
        });

        exitButton.addActionListener(e -> System.exit(0));

        // Show the frame
        frame.setVisible(true);
    }

    private void updateTable(JTable table, List<Student> students) {
        String[] columnNames = {"ID", "First Name", "Last Name", "Age", "Course"};
        Object[][] data = new Object[students.size()][5];

        for (int i = 0; i < students.size(); i++) {
            Student student = students.get(i);
            data[i][0] = student.getStudentID();
            data[i][1] = student.getFirstName();
            data[i][2] = student.getLastName();
            data[i][3] = student.getAge();
            data[i][4] = student.getCourse();
        }

        DefaultTableModel model = new DefaultTableModel(data, columnNames);
        table.setModel(model);
    }

    private boolean validateID(String id) {
        if (!id.matches("\\d+")) {
            JOptionPane.showMessageDialog(null, "ID must be a numeric value.");
            return false;
        }
        return true;
    }

    private boolean validateName(String name) {
        if (name.isEmpty() || !name.matches("[a-zA-Z]+")) {
            JOptionPane.showMessageDialog(null, "Name must contain only letters and cannot be empty.");
            return false;
        }
        return true;
    }

    private boolean validateAge(String age) {
        if (!age.matches("\\d+") || Integer.parseInt(age) <= 0) {
            JOptionPane.showMessageDialog(null, "Age must be a positive numeric value.");
            return false;
        }
        return true;
    }

    private boolean validateCourse(String course) {
        if (course.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Course cannot be empty.");
            return false;
        }
        return true;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(StudentManagementGUI::new);
    }
}
