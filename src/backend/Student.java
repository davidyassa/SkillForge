/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package backend;

import java.util.ArrayList;

public class Student extends User {

    private final ArrayList<String> enrolledCourses = new ArrayList<>(); //Courses IDs
    private final ArrayList<Certificate> certificates = new ArrayList<>(); //Certificates
    private final ArrayList<StudentCourseProgress> coursesProgress = new ArrayList<>(); //Progress trackers

    public Student(String userID, String username, String email, String passwordHash) {
        super(userID, "STUDENT", username, email, passwordHash);
    }

    public ArrayList<String> getEnrolledCourses() {
        return enrolledCourses;
    }

    public void addSCP(StudentCourseProgress scp) {
        coursesProgress.add(scp);
    }

    public StudentCourseProgress getSCP(String courseID) {
        if (enrolledCourses.contains(courseID)) {
            for (StudentCourseProgress scp : coursesProgress) {
                if (scp.getCourseID().equals(courseID)) {
                    return scp;
                }
            }
        }
        return null;
    }

    public double getProgress(String courseID) {
        StudentCourseProgress scp = getSCP(courseID);
        if (scp == null) {
            return 0;
        }
        return scp.getProgress();
    }

    public ArrayList<StudentCourseProgress> getAllProgress() {
        return coursesProgress;
    }

    public boolean isEnrolled(String courseID) {
        return enrolledCourses.contains(courseID);
    }

    public void enrollCourse(String courseID) {
        if (!isEnrolled(courseID)) {
            enrolledCourses.add(courseID);
        }
    }

    public void markLessonCompleted(String courseID, String lessonID) {
        StudentCourseProgress scp = getSCP(courseID);
        if (scp != null) {
            scp.completeLesson(lessonID);
        }
    }

    public void addCertificate(Certificate cert) {
        certificates.add(cert);
    }

    public ArrayList<Certificate> getCertificates() {
        return certificates;
    }
}
