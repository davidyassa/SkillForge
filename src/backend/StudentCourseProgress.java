/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package backend;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
/**
 *
 * @author DELL 7550
 */
public class StudentCourseProgress {

    private final String studentID;
    private final String courseID;
    private boolean isCourseCompleted;
    private double progress; //0 <= progress <= 100
    private final ArrayList<String> completedLessons = new ArrayList<>();
    private final Map<String, Double> lessonQuizScores = new HashMap<>();

    private static JsonDatabaseManager db;

    public static void setDB(JsonDatabaseManager dbm) {
        db = dbm;
    }

    public StudentCourseProgress(String studentID, String courseID) {
        this.studentID = studentID;
        this.courseID = courseID;
        isCourseCompleted = false;
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

        if (progress >= 100) {
            isCourseCompleted = true;
        }
    }

    public void completeLesson(String lessonID) {
        if (!completedLessons.contains(lessonID)) {
            completedLessons.add(lessonID);
            updateProgress();
        }
    }

    ////////////////
    public void recordLessonScore(String lessonId, double score) {
        // Simple validation: score must be positive
        if (score >= 0) {
            this.lessonQuizScores.put(lessonId, score);
        }
    }

    /**
     * ADDED 3: Missing method required by Instructor.java (getLessonQuizAverage)
     * Returns the quiz score for a specific lesson, or -1.0 if not found/taken.
     */
    public double getLessonScore(String lessonId) {
        // Retrieves the score, returning -1.0 if the key is not found (quiz not taken)
        return this.lessonQuizScores.getOrDefault(lessonId, -1.0); 
    }
    
    // ADDED 4: Getter for the full scores map (Useful for advanced analysis/saving)
    public Map<String, Double> getLessonQuizScores() {
        return lessonQuizScores;
    }
    public boolean isIsCourseCompleted() {
        return isCourseCompleted;
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

}
