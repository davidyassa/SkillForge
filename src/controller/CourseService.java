/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import backend.*;
import java.util.ArrayList;

public class CourseService {

    private final JsonDatabaseManager db;

    public CourseService(JsonDatabaseManager dbManager) {
        this.db = dbManager;
    }

    public ArrayList<Course> getAllCourses() {
        return db.getCourses();
    }

    public ArrayList<Course> getEnrolledCourses(String studentId) {
        User user = db.findUserById(studentId);
        if (user instanceof Student student) {
            return db.getCoursesForStudent(student);
        }
        return new ArrayList<>();
    }

    public boolean enrollStudent(String studentId, String courseId) {
        User user = db.findUserById(studentId);
        Course course = db.findCourseById(courseId);
        if (user instanceof Student && course != null) {
            db.enrollStudentInCourse((Student) user, course);
            return true;
        }
        return false;
    }

    public double getCourseProgress(String studentId, String courseID) {
        Student student = (Student) db.findUserById(studentId);
        if (student.getEnrolledCourses().contains(courseID)) {
            return student.getProgress(courseID);
        }
        return 0;
    }

    public boolean completeLesson(String studentId, String courseId, String lessonId) {
        Student s = (Student) db.findUserById(studentId);
        if (s == null || !s.isEnrolled(courseId)) {
            return false;
        }
        s.markLessonCompleted(courseId, lessonId);
        JsonDatabaseManager.saveCourses();
        JsonDatabaseManager.saveUsers();
        return true;
    }

    public Course getCourse(String id) {
        return db.findCourseById(id);
    }

}
