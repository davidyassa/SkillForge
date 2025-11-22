/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package backend;

import java.util.ArrayList;

public class Course implements Searchable {

    private final String courseId;
    private String title;
    private String description;
    private String instructorId;
    private String approvalstate;
    private final ArrayList<Lesson> lessons;
    private final ArrayList<String> students; //student IDs

    public Course(String courseId, String title, String description, String instructorId) {
        this.courseId = courseId;
        this.title = title;
        this.description = description;
        this.instructorId = instructorId;
        lessons = new ArrayList<>();
        students = new ArrayList<>();
    }

    public void addLesson(Lesson lesson) {
        lessons.add(lesson);
    }

    public void removeLesson(String lessonId) {
        lessons.removeIf(l -> l.getID().equals(lessonId));
    }

    public Lesson findLesson(String lessonId) {
        for (Lesson lesson : lessons) {
            if (lesson.getID().equals(lessonId)) {
                return lesson;
            }
        }
        return null;
    }

    public void enrollStudent(String studentId) {
        if (!students.contains(studentId)) {
            students.add(studentId);
        }
    }
    
    public Boolean isApproved() {
    return approvalstate != null && approvalstate.equalsIgnoreCase("APPROVED");
}


    public String getApprovalstate() {
        return approvalstate;
    }

    public void setApprovalstate(String approvalstate) {
        this.approvalstate = approvalstate;
    }

    @Override
    public String getID() {
        return courseId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getInstructorId() {
        return instructorId;
    }

    public void setInstructorId(String instructorId) {
        this.instructorId = instructorId;
    }

    public ArrayList<Lesson> getLessons() {
        return lessons;
    }

    public ArrayList<String> getEnrolledStudents() {
        return students;
    }

    @Override
    public String toString() {
        return title;
    }
}
