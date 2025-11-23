package backend;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Instructor extends User {

    private static JsonDatabaseManager db;
    private ArrayList<String> createdCourses = new ArrayList<>();
    
    private final Map<String, Double> courseAverageScores = new HashMap<>();
    private final Map<String, Double> courseCompletionRates = new HashMap<>();

    private final Map<String, Map<String, Double>> lessonQuizAverages = new HashMap<>();

    public static void setDB(JsonDatabaseManager dbm) {
        db = dbm;
    }
    public Instructor(String userId, String username, String email, String passwordHash) {
        super(userId, "INSTRUCTOR", username, email, passwordHash);
        this.createdCourses = new ArrayList<>();
    }

    public Instructor(String userId, String username, String email, String passwordHash, ArrayList<String> createdCoursess) {
        super(userId, "INSTRUCTOR", username, email, passwordHash);
        if (createdCoursess != null) {
            createdCourses = new ArrayList<>(createdCoursess);
        } else {
            createdCourses = new ArrayList<>();
        }
    }
    
    public ArrayList<String> getCreatedCourses() {
        return createdCourses;
    }

    public void addCourse(String courseId) {
        if (!createdCourses.contains(courseId)) {
            createdCourses.add(courseId);
        }
    }
    
    public Course createCourse(String courseTitle, String courseDesc) {
        String id = db.generateCourseId();
        Course c = new Course(
                id,
                courseTitle,
                courseDesc,
                getID()); 
        addCourse(id);
        db.addCourse(c);
        db.saveUsers();
        db.saveCourses();

        return c;
    }

    public void editCourse(Course course, String newTitle, String newDescription) {
        if (newTitle != null && !newTitle.trim().isEmpty()) {
            course.setTitle(newTitle);
        }
        if (newDescription != null && !newDescription.trim().isEmpty()) {
            course.setDescription(newDescription);
        }
        db.saveCourses();
    }

    public void deleteCourse(String courseID) {
        createdCourses.remove(courseID);

        db.removeCourse(courseID);
        db.saveUsers();
    }

    
    public Lesson addLesson(Course course, String lessonTitle, String content) {
        Lesson lesson = new Lesson(
                db.generateLessonId(course),
                lessonTitle,
                content
        );
        course.getLessons().add(lesson);
        db.saveCourses();
        return lesson;
    }

    public void editLesson(Course course, String lessonId, String newTitle, String newContent) {
        for (Lesson l : course.getLessons()) {
            if (l.getID().equals(lessonId)) {
                if (newTitle != null) {
                    l.setTitle(newTitle);
                }
                if (newContent != null) {
                    l.setContent(newContent);
                }
                break;
            }
        }
        db.saveCourses();
    }

    public void deleteLesson(Course course, String lessonId) {
        course.getLessons().removeIf(l -> l.getID().equals(lessonId));
        db.saveCourses();
    }
    
    
    public String getUserId() {
        return getID(); 
    }

    
    public double calculateCourseAverage(String courseId) {
        Course c = db.findCourseById(courseId); 
        if (c == null) return 0;

        double total = 0;
        int count = 0;

        for (String stu : c.getEnrolledStudents()) {
            Student s = (Student) db.findUserById(stu); 
            if (s != null) {
                double p = s.getProgress(courseId); 
                total += p;
                count++;
            }
        }

        double avg = count == 0 ? 0 : total / count;
        courseAverageScores.put(courseId, avg);
        return avg;
    }

    public double calculateCompletionRate(String courseId) {
        Course c = db.findCourseById(courseId);
        if (c == null) return 0;

        double completed = 0;
        int total = c.getEnrolledStudents().size();

        for (String stu : c.getEnrolledStudents()) {
            Student s = (Student) db.findUserById(stu);
            if (s != null && s.getProgress(courseId) == 100)
                completed++;
        }

        double rate = total == 0 ? 0 : (completed / total) * 100;
        courseCompletionRates.put(courseId, rate);
        return rate;
    }

    public double getLessonQuizAverage(String courseId, String lessonId) {
        StudentCourseProgress scp;
        double total = 0;
        int count = 0;

        Course c = db.findCourseById(courseId);
        if (c == null) return 0;

        for (String stu : c.getEnrolledStudents()) {

            Student s = (Student) db.findUserById(stu);
            if (s != null) {

                scp = s.getSCP(courseId);
                if (scp != null && scp.getLessonScore(lessonId) != -1) {
                    total += scp.getLessonScore(lessonId);
                    count++;
                }
            }
        }

        double avg = count == 0 ? 0 : total / count;

        lessonQuizAverages.putIfAbsent(courseId, new HashMap<>());
        lessonQuizAverages.get(courseId).put(lessonId, avg);

        return avg;
    }
}
