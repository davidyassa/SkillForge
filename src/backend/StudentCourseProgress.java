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
    private boolean isCourseCompleted;
    private double progress; //0 <= progress <= 100
    private final ArrayList<String> completedLessons = new ArrayList<>();

    public StudentCourseProgress(String studentID, String courseID) {
        this.studentID = studentID;
        this.courseID = courseID;
        isCourseCompleted = false;
        progress = 0.0d;
    }

    public void updateProgress() {
        Course c = new JsonDatabaseManager("users.json", "courses.json").findCourseById(courseID);
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
