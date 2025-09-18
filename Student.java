import java.io.Serializable;
import java.util.ArrayList;

public class Student implements Serializable {
    private String nptelId;
    private String name;
    private ArrayList<ExamAttempt> attempts;

    public Student(String nptelId, String name) {
        this.nptelId = nptelId;
        this.name = name;
        this.attempts = new ArrayList<>();
    }

    public String getNptelId() {
        return nptelId;
    }

    public String getName() {
        return name;
    }

    public ArrayList<ExamAttempt> getAttempts() {
        return attempts;
    }

    public void addAttempt(ExamAttempt attempt) {
        attempts.add(attempt);
    }

    @Override
    public String toString() {
        return nptelId + " - " + name;
    }
}


