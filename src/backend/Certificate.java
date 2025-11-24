/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package backend;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author DELL 7550
 */
public class Certificate {

    private String courseTitle;
    private String studentId;
    private String courseId;
    private String certificateId;
    private String issueDate;

    public Certificate(String studentId, String courseId) {
        this.courseTitle = JsonDatabaseManager.getCourseTitle(courseId);
        this.studentId = studentId;
        this.courseId = courseId;
        LocalDate date = LocalDate.now();
        this.issueDate = date.format(DateTimeFormatter.ofPattern("yyMMdd"));
        certificateId = JsonDatabaseManager.generateCertificateID(studentId, courseId, this.issueDate);

    }

    public String getStudentId() {
        return studentId;
    }

    public String getCourseId() {
        return courseId;
    }

    public String getID() {
        return certificateId;
    }

    public String getIssueDate() {
        return issueDate;
    }

    public String getTextBlock() {
        int i = 0;
        String y = issueDate.substring(i++, ++i);
        String m = issueDate.substring(i++, ++i);
        String d = issueDate.substring(i++, ++i);
        String dateText = d + "/" + m + "/" + "20" + y;

        return "====== CERTIFICATE ======\n"
                + "Course        : " + courseTitle + "\n"
                + "Course ID     : " + courseId + "\n"
                + "Student ID    : " + studentId + "\n"
                + "Certificate ID: " + certificateId + "\n"
                + "Issued On     : " + dateText + "\n"
                + "========================\n";
    }

    @Override
    public String toString() {
        if (courseTitle == null) {
            courseTitle = JsonDatabaseManager.getCourseTitle(courseId);
            // because at startup courseTitles aren't loaded yet
        }
        return certificateId;
    }
}
