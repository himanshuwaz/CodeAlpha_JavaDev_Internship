import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.Collections; // For min/max on ArrayList

/**
 * Represents a single student with their name and a list of grades.
 */
class Student {
    private String name;
    private List<Double> grades;

    /**
     * Constructor for the Student class.
     * @param name The name of the student.
     */
    public Student(String name) {
        this.name = name;
        this.grades = new ArrayList<>();
    }

    /**
     * Gets the name of the student.
     * @return The student's name.
     */
    public String getName() {
        return name;
    }

    /**
     * Adds a grade to the student's list of grades.
     * @param grade The grade to add.
     */
    public void addGrade(double grade) {
        if (grade >= 0 && grade <= 100) { // Assuming grades are between 0 and 100
            this.grades.add(grade);
        } else {
            System.out.println("Invalid grade. Grade must be between 0 and 100.");
        }
    }

    /**
     * Gets the list of grades for the student.
     * @return A list of the student's grades.
     */
    public List<Double> getGrades() {
        return grades;
    }

    /**
     * Calculates the average grade for the student.
     * @return The average grade, or 0.0 if no grades are available.
     */
    public double calculateAverageGrade() {
        if (grades.isEmpty()) {
            return 0.0;
        }
        double sum = 0;
        for (double grade : grades) {
            sum += grade;
        }
        return sum / grades.size();
    }

    /**
     * Finds the highest grade for the student.
     * @return The highest grade, or 0.0 if no grades are available.
     */
    public double getHighestGrade() {
        if (grades.isEmpty()) {
            return 0.0;
        }
        return Collections.max(grades);
    }

    /**
     * Finds the lowest grade for the student.
     * @return The lowest grade, or 0.0 if no grades are available.
     */
    public double getLowestGrade() {
        if (grades.isEmpty()) {
            return 0.0;
        }
        return Collections.min(grades);
    }

    /**
     * Returns a string representation of the student's grades and statistics.
     * @return A formatted string with student information.
     */
    @Override
    public String toString() {
        return String.format("  - Grades: %s%n  - Average: %.2f%n  - Highest: %.2f%n  - Lowest: %.2f",
                grades.isEmpty() ? "No grades yet" : grades.toString(),
                calculateAverageGrade(),
                getHighestGrade(),
                getLowestGrade());
    }
}

/**
 * Manages a collection of students and provides grade tracking functionalities.
 */
public class GradeTracker {
    private List<Student> students;
    private Scanner scanner;

    /**
     * Constructor for the GradeTracker class.
     */
    public GradeTracker() {
        this.students = new ArrayList<>();
        this.scanner = new Scanner(System.in);
    }

    /**
     * Adds a new student to the tracker.
     */
    public void addStudent() {
        System.out.print("Enter student name: ");
        String name = scanner.nextLine();
        if (name.trim().isEmpty()) {
            System.out.println("Student name cannot be empty. Please try again.");
            return;
        }
        // Check if student already exists
        for (Student s : students) {
            if (s.getName().equalsIgnoreCase(name)) {
                System.out.println("Student with this name already exists. Please use a unique name.");
                return;
            }
        }
        students.add(new Student(name));
        System.out.println("Student '" + name + "' added successfully.");
    }

    /**
     * Adds grades for an existing student.
     */
    public void addGradesToStudent() {
        if (students.isEmpty()) {
            System.out.println("No students added yet. Please add a student first.");
            return;
        }

        System.out.print("Enter the name of the student to add grades for: ");
        String studentName = scanner.nextLine();
        Student student = findStudent(studentName);

        if (student == null) {
            System.out.println("Student '" + studentName + "' not found.");
            return;
        }

        System.out.println("Enter grades for " + student.getName() + " (enter -1 to finish):");
        while (true) {
            try {
                System.out.print("Enter grade: ");
                double grade = scanner.nextDouble();
                if (grade == -1) {
                    break;
                }
                student.addGrade(grade);
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number for the grade.");
                scanner.next(); // Consume the invalid input
            }
        }
        scanner.nextLine(); // Consume the remaining newline character
        System.out.println("Grades added for " + student.getName() + ".");
    }

    /**
     * Finds a student by name.
     * @param name The name of the student to find.
     * @return The Student object if found, otherwise null.
     */
    private Student findStudent(String name) {
        for (Student student : students) {
            if (student.getName().equalsIgnoreCase(name)) {
                return student;
            }
        }
        return null;
    }

    /**
     * Displays a summary report of all students and their grades.
     */
    public void displaySummaryReport() {
        if (students.isEmpty()) {
            System.out.println("No students to display report for.");
            return;
        }

        System.out.println("\n--- Student Grade Report ---");
        for (Student student : students) {
            System.out.println("Student: " + student.getName());
            System.out.println(student.toString());
            System.out.println("----------------------------");
        }

        // Calculate and display overall statistics
        displayOverallStatistics();
        System.out.println("--- End of Report ---\n");
    }

    /**
     * Calculates and displays overall average, highest, and lowest grades across all students.
     */
    private void displayOverallStatistics() {
        if (students.isEmpty()) {
            return;
        }

        List<Double> allGrades = new ArrayList<>();
        for (Student student : students) {
            allGrades.addAll(student.getGrades());
        }

        if (allGrades.isEmpty()) {
            System.out.println("No grades entered across all students for overall statistics.");
            return;
        }

        double overallSum = 0;
        for (double grade : allGrades) {
            overallSum += grade;
        }
        double overallAverage = overallSum / allGrades.size();
        double overallHighest = Collections.max(allGrades);
        double overallLowest = Collections.min(allGrades);

        System.out.println("\n--- Overall Class Statistics ---");
        System.out.printf("  - Overall Average Grade: %.2f%n", overallAverage);
        System.out.printf("  - Overall Highest Grade: %.2f%n", overallHighest);
        System.out.printf("  - Overall Lowest Grade: %.2f%n", overallLowest);
        System.out.println("--------------------------------");
    }

    /**
     * Displays the main menu for the grade tracker.
     */
    public void displayMenu() {
        System.out.println("\n--- Student Grade Tracker Menu ---");
        System.out.println("1. Add New Student");
        System.out.println("2. Add Grades to Student");
        System.out.println("3. Display Summary Report");
        System.out.println("4. Exit");
        System.out.print("Enter your choice: ");
    }

    /**
     * Main method to run the Student Grade Tracker application.
     * @param args Command line arguments (not used).
     */
    public static void main(String[] args) {
        GradeTracker tracker = new GradeTracker();
        int choice;
        do {
            tracker.displayMenu();
            try {
                choice = tracker.scanner.nextInt();
                tracker.scanner.nextLine(); // Consume newline

                switch (choice) {
                    case 1:
                        tracker.addStudent();
                        break;
                    case 2:
                        tracker.addGradesToStudent();
                        break;
                    case 3:
                        tracker.displaySummaryReport();
                        break;
                    case 4:
                        System.out.println("Exiting Grade Tracker. Goodbye!");
                        break;
                    default:
                        System.out.println("Invalid choice. Please enter a number between 1 and 4.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                tracker.scanner.nextLine(); // Consume the invalid input
                choice = 0; // Set choice to 0 to re-display menu
            }
        } while (choice != 4);

        tracker.scanner.close(); // Close the scanner when done
    }
}
