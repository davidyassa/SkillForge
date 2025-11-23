/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package backend;

import java.util.ArrayList;

/**
 *
 * @author DELL 7550
 */
public class StudentCourseProgress {

    private final String studentID;
    private final String courseID;
    private boolean courseCompleted;
    private double progress; //0 <= progress <= 100
    private final ArrayList<String> completedLessons = new ArrayList<>();

    // NEW: List to store the IDs of quizzes this specific student has passed
    private final ArrayList<String> passedQuizzes = new ArrayList<>();

    private static JsonDatabaseManager db;

    public static void setDB(JsonDatabaseManager dbm) {
        db = dbm;
    }

    public StudentCourseProgress(String studentID, String courseID) {
        this.studentID = studentID;
        this.courseID = courseID;
        courseCompleted = false;
        progress = 0.0d;
    }

    public void updateProgress() {
        Course c = db.findCourseById(courseID);
        if (c == null || c.getLessons().isEmpty()) {
            progress = 0;
            return;
        }
        //        double p = 0.0;
        double completed = 0.0;
        for (Lesson l : c.getLessons()) {
            if (completedLessons.contains(l.getID())) {
                completed++;
            }

        }
        progress = (completed / c.getLessons().size()) * 100;
        Student s = (Student) db.findUserById(studentID);
        if (progress >= 100) {
            courseCompleted = true;
            s.checkAddCertificate(courseID);
        }
    }

    public void completeLesson(String lessonID) {
        if (!completedLessons.contains(lessonID)) {
            completedLessons.add(lessonID);
            updateProgress();
        }
    }

    public boolean isCourseCompleted() {
        return courseCompleted;
    }

    public boolean isLessonCompleted(String lessonID) {
        return completedLessons.contains(lessonID);
    }

    public double getProgress() {
        return progress;
    }

    public ArrayList<String> getCompletedLessons() {
        return completedLessons;
    }

    public String getStudentID() {
        return studentID;
    }

    public String getCourseID() {
        return courseID;
    }

    // --- NEW METHODS START ---
    /**
     * Records that the student has passed a specific quiz.
     *
     * @param quizID The ID of the quiz passed.
     */
    public void passQuiz(String quizID) {
        if (!passedQuizzes.contains(quizID)) {
            passedQuizzes.add(quizID);
        }
    }

    /**
     * Checks if the student has already passed a specific quiz.
     *
     * @param quizID The ID of the quiz.
     * @return true if passed, false otherwise.
     */
    public boolean isQuizPassed(String quizID) {
        return passedQuizzes.contains(quizID);
    }
    // --- NEW METHODS END ---
}
