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

    private String studentId;
    private String courseId;
    private String certificateId;
    private String issueDate;

    public Certificate(String studentId, String courseId) {
        this.studentId = studentId;
        this.courseId = courseId;
        LocalDate date = LocalDate.now();
        this.issueDate = date.format(DateTimeFormatter.ofPattern("yyMMdd"));
        certificateId = JsonDatabaseManager.generateCertificateID(studentId, courseId, this.issueDate);

    }

}
