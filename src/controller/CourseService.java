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

    public boolean createCourse(String instructorID, String courseTitle, String courseDesc) {
        Instructor ins = (Instructor) db.findUserById(instructorID);
        if (ins == null) {
            throw new IllegalArgumentException("Instructor not found!");
        }
        if (courseTitle == null) {
            throw new IllegalArgumentException("Course title cannot be empty!");
        }
        if (courseDesc == null) {
            throw new IllegalArgumentException("Course description cannot be empty!");
        }
        ins.createCourse(courseTitle, courseDesc);
        return true;
    }

    public boolean deleteCourse(String instructorID, String courseID) {
        Instructor ins = (Instructor) db.findUserById(instructorID);
        Course c = db.findCourseById(courseID);
        if (ins == null || c == null) {
            return false;
        }

        // Remove course from students
        for (String studentId : c.getEnrolledStudents()) {
            Student s = (Student) db.findUserById(studentId);
            if (s != null) {
                s.getEnrolledCourses().remove(courseID);
                StudentCourseProgress scp = s.getSCP(courseID);
                if (scp != null) {
                    s.getAllProgress().remove(scp);
                }
            }
        }
        ins.deleteCourse(courseID);
        return true;
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

    public ArrayList<Course> getCoursesForInstructor(String instructorId) {
        return db.getCoursesForInstructor(instructorId);
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
        db.saveCourses();
        db.saveUsers();
        return true;
    }

    public Course getCourse(String id) {
        return db.findCourseById(id);
    }

    public Certificate getCertificate(String id) {
        return db.findCertificateById(id);
    }

    public void saveCourses() {
        db.saveCourses();
    }

}
