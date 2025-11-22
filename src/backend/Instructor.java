/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
 /*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package backend;

import java.util.ArrayList;

public class Instructor extends User {

    private static JsonDatabaseManager db;
    private ArrayList<String> createdCourses = new ArrayList<>();

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
        String id = JsonDatabaseManager.generateCourseID();
        Course c = new Course(
                id,
                courseTitle,
                courseDesc,
                getID());
        addCourse(id);
        db.addCourse(c);
        db.saveUsers();

        return c;
    }

    public void editCourse(Course course, String newTitle, String newDescription) {
        if (newTitle != null) {
            course.setTitle(newTitle);
        }
        if (newDescription != null) {
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
                JsonDatabaseManager.generateLessonID(course),
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
}
