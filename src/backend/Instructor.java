/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package backend;

import java.util.ArrayList;

public class Instructor extends User {

    private ArrayList<String> createdCourses;

    public Instructor(String userId, String username, String email, String passwordHash) {
        super(userId, "INSTRUCTOR", username, email, passwordHash);
        createdCourses = new ArrayList<>();
    }

    public Instructor(String userId, String username, String email, String passwordHash, ArrayList<String> createdCourses) {
        super(userId, "INSTRUCTOR", username, email, passwordHash);
        if (createdCourses != null) {
            this.createdCourses = new ArrayList<>(createdCourses);
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

    public void removeCourse(String courseId) {
        createdCourses.remove(courseId);
    }

    public void addCreatedCourse(String courseId) {
        createdCourses.add(courseId);
    }

    public Course createCourse(String courseTitle, String courseDesc) {
        String id = JsonDatabaseManager.generateCourseId();
        Course c = new Course(
                id,
                courseTitle,
                courseDesc,
                getID());
        addCreatedCourse(id);
        JsonDatabaseManager.addCourse(c);
        JsonDatabaseManager.saveUsers();

        return c;
    }

    public void editCourse(Course course, String newTitle, String newDescription) {
        if (newTitle != null) {
            course.setTitle(newTitle);
        }
        if (newDescription != null) {
            course.setDescription(newDescription);
        }
        JsonDatabaseManager.saveCourses();
    }

    public void deleteCourse(Course course) {
        this.createdCourses.remove(course.getID());

        new JsonDatabaseManager("users.json", "courses.json").removeCourse(course.getID());
        JsonDatabaseManager.saveUsers();
    }

    public Lesson addLesson(Course course, String lessonTitle, String content) {
        Lesson lesson = new Lesson(
                JsonDatabaseManager.generateLessonId(course),
                lessonTitle,
                content
        );
        course.getLessons().add(lesson);
        JsonDatabaseManager.saveCourses();
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
        JsonDatabaseManager.saveCourses();
    }

    public void deleteLesson(Course course, String lessonId) {
        course.getLessons().removeIf(l -> l.getID().equals(lessonId));
        JsonDatabaseManager.saveCourses();
    }
}
