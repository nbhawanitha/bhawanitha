import java.io.*;
import java.time.LocalDate;
import java.util.*;

public class StudentTracker {
    private ArrayList<Student> students;
    private ArrayList<Course> courses;
    private Scanner scanner;

    public StudentTracker() {
        students = new ArrayList<>();
        courses = new ArrayList<>();
        scanner = new Scanner(System.in);
    }

    // Add a student
    public void createStudent() {
        System.out.print("Enter NPTEL ID: ");
        String id = scanner.nextLine();
        System.out.print("Enter Name: ");
        String name = scanner.nextLine();
        students.add(new Student(id, name));
        System.out.println("Student added!");
    }

    // Add a course
    public void addCourse() {
        System.out.print("Enter Course Code: ");
        String code = scanner.nextLine();
        System.out.print("Enter Course Name: ");
        String name = scanner.nextLine();
        courses.add(new Course(code, name));
        System.out.println("Course added!");
    }

    // Register an exam attempt
    public void registerAttempt() {
        System.out.print("Enter NPTEL ID: ");
        String id = scanner.nextLine();
        Student student = findStudentById(id);
        if (student == null) {
            System.out.println("Student not found.");
            return;
        }

        System.out.println("Available Courses:");
        for (int i = 0; i < courses.size(); i++) {
            System.out.println((i + 1) + ". " + courses.get(i));
        }
        System.out.print("Choose course index: ");
        int choice = Integer.parseInt(scanner.nextLine()) - 1;
        if (choice < 0 || choice >= courses.size()) {
            System.out.println("Invalid choice.");
            return;
        }

        Course course = courses.get(choice);

        System.out.print("Enter score: ");
        int score = Integer.parseInt(scanner.nextLine());

        System.out.print("Enter date (YYYY-MM-DD): ");
        LocalDate date = LocalDate.parse(scanner.nextLine());

        student.addAttempt(new ExamAttempt(course, score, date));

        System.out.println("Exam attempt registered!");
    }

    // Track attempts
    public void trackAttempts() {
        System.out.print("Enter NPTEL ID: ");
        String id = scanner.nextLine();
        Student student = findStudentById(id);
        if (student == null) {
            System.out.println("Student not found.");
            return;
        }
        System.out.println("Exam Attempts for " + student.getName() + ":");
        for (ExamAttempt attempt : student.getAttempts()) {
            System.out.println(attempt);
        }
    }

    // Store to file
    public void saveToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("students.dat"))) {
            oos.writeObject(students);
            System.out.println("Data saved successfully!");
        } catch (IOException e) {
            System.out.println("Error saving data: " + e.getMessage());
        }
    }

    // Load from file
    @SuppressWarnings("unchecked")
    public void loadFromFile() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("students.dat"))) {
            students = (ArrayList<Student>) ois.readObject();
            System.out.println("Data loaded successfully!");
        } catch (FileNotFoundException e) {
            System.out.println("No saved data found.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading data: " + e.getMessage());
        }
    }

    // Sort and group
    public void sortByName() {
        students.sort(Comparator.comparing(Student::getName));
        System.out.println("Students sorted by name:");
        for (Student s : students) {
            System.out.println(s);
        }
    }

    // Query by subject or ID
    public void search() {
        System.out.print("Enter NPTEL ID or Course Code: ");
        String input = scanner.nextLine();

        for (Student student : students) {
            if (student.getNptelId().equalsIgnoreCase(input)) {
                System.out.println("Student found: " + student);
                for (ExamAttempt attempt : student.getAttempts()) {
                    System.out.println(attempt);
                }
            } else {
                for (ExamAttempt attempt : student.getAttempts()) {
                    if (attempt.getCourse().getCourseCode().equalsIgnoreCase(input)) {
                        System.out.println("Student: " + student.getName() + " | " + attempt);
                    }
                }
            }
        }
    }

    // Report generation
    public void generateReport() {
        System.out.println("Batch-wise report:");
        Map<String, List<Student>> grouped = new HashMap<>();
        for (Student s : students) {
            String batch = s.getNptelId().substring(0, 4); // example: first 4 chars
            grouped.putIfAbsent(batch, new ArrayList<>());
            grouped.get(batch).add(s);
        }

        for (String batch : grouped.keySet()) {
            System.out.println("Batch: " + batch);
            for (Student s : grouped.get(batch)) {
                System.out.println(" - " + s);
            }
        }
    }

    // CSV export
    public void exportCSV() {
        try (PrintWriter pw = new PrintWriter(new File("export.csv"))) {
            pw.println("NPTEL ID,Name,Course Code,Course Name,Score,Date");
            for (Student s : students) {
                for (ExamAttempt attempt : s.getAttempts()) {
                    pw.printf("%s,%s,%s,%s,%d,%s%n",
                            s.getNptelId(),
                            s.getName(),
                            attempt.getCourse().getCourseCode(),
                            attempt.getCourse().getCourseName(),
                            attempt.getScore(),
                            attempt.getDate());
                }
            }
            System.out.println("CSV exported successfully!");
        } catch (IOException e) {
            System.out.println("Error exporting CSV: " + e.getMessage());
        }
    }

    private Student findStudentById(String id) {
        for (Student s : students) {
            if (s.getNptelId().equalsIgnoreCase(id)) {
                return s;
            }
        }
        return null;
    }

    // Menu-driven method
    public void start() {
        loadFromFile();
        while (true) {
            System.out.println("\n1. Create Student");
            System.out.println("2. Add Course");
            System.out.println("3. Register Exam Attempt");
            System.out.println("4. Track Attempts");
            System.out.println("5. Sort Students by Name");
            System.out.println("6. Search by ID or Course");
            System.out.println("7. Generate Report");
            System.out.println("8. Export CSV");
            System.out.println("9. Save & Exit");
            System.out.print("Choose option: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1": createStudent(); break;
                case "2": addCourse(); break;
                case "3": registerAttempt(); break;
                case "4": trackAttempts(); break;
                case "5": sortByName(); break;
                case "6": search(); break;
                case "7": generateReport(); break;
                case "8": exportCSV(); break;
                case "9": saveToFile(); System.out.println("Exiting..."); return;
                default: System.out.println("Invalid option.");
            }
        }
    }

    public static void main(String[] args) {
        StudentTracker tracker = new StudentTracker();
        tracker.start();
    }
}