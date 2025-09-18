import java.io.Serializable;
import java.time.LocalDate;

public class ExamAttempt implements Serializable {
    private Course course;
    private int score;
    private LocalDate date;

    public ExamAttempt(Course course, int score, LocalDate date) {
        this.course = course;
        this.score = score;
        this.date = date;
    }

    public Course getCourse() {
        return course;
    }

    public int getScore() {
        return score;
    }

    public LocalDate getDate() {
        return date;
    }

    @Override
    public String toString() {
        return course + " | Score: " + score + " | Date: " + date;
    }
}
