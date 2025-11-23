package controller;

import backend.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random; 

public class CourseService {

    private final JsonDatabaseManager db;
    private final Random random = new Random();

    public CourseService(JsonDatabaseManager dbManager) {
        this.db = dbManager;
    }

    public ArrayList<Course> getAllCourses() {
        return db.getCourses();
    }
    
    public ArrayList<Course> getCoursesByInstructorId(String instructorId) {
        User user = db.findUserById(instructorId);
        if (user instanceof Instructor ins) {
            ArrayList<Course> instructorCourses = new ArrayList<>();
            for (String courseId : ins.getCreatedCourses()) {
                Course course = db.findCourseById(courseId);
                if (course != null) {
                    instructorCourses.add(course);
                }
            }
            return instructorCourses;
        }
        return new ArrayList<>();
    }

    public boolean createCourse(String instructorID, String courseTitle, String courseDesc) {
        Instructor ins = (Instructor) db.findUserById(instructorID);
        
        if (ins == null) {
            System.err.println("Instructor not found!");
            return false;
        }
        if (courseTitle == null || courseTitle.trim().isEmpty() || courseDesc == null || courseDesc.trim().isEmpty()) {
            throw new IllegalArgumentException("Course title and description cannot be empty!");
        }

        ins.createCourse(courseTitle, courseDesc);
        return true;
    }
    
    public void editCourse(Course course, String newTitle, String newDescription) {

        Instructor ins = (Instructor) db.findUserById(course.getInstructorId());
        
        if (ins != null) {

            ins.editCourse(course, newTitle, newDescription);
        } else {
            throw new IllegalArgumentException("Instructor not found for course: " + course.getTitle());
        }
    }    
    
    public boolean deleteCourse(String instructorID, String courseID) {
        Instructor ins = (Instructor) db.findUserById(instructorID);
        Course c = db.findCourseById(courseID);
        if (ins == null || c == null) {
            return false;
        }

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
        db.saveUsers();
        db.saveCourses();
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

    public double getCourseProgress(String studentId, String courseID) {
        Student student = (Student) db.findUserById(studentId);
        if (student != null && student.getEnrolledCourses().contains(courseID)) {
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

    public Map<String, Object> getCourseInsights(String courseId) {
        Map<String, Object> insightsData = new HashMap<>();
        

        insightsData.put("completionPercentage", 75.5); 
        
        Map<String, Double> quizAverages = new HashMap<>();
        quizAverages.put("Lesson 1: Introduction", 85.0);
        quizAverages.put("Lesson 2: Core Concepts", 72.5);
        quizAverages.put("Lesson 3: Advanced Topics", 91.0);
        insightsData.put("quizAverages", quizAverages);

        Map<String, Double> studentScores = new HashMap<>();
        
        for (int i = 1; i <= 5; i++) {
            String studentName = "Student " + i;
            double score = 60.0 + (99.9 - 60.0) * random.nextDouble(); 
            studentScores.put(studentName, Math.round(score * 10.0) / 10.0);
        }

        insightsData.put("studentScores", studentScores);
        
        return insightsData;
    }
}