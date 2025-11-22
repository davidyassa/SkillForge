/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package backend;

public class Admin extends User {

    public Admin(String userId, String username, String email, String passwordHash) {
        super(userId, "ADMIN", username, email, passwordHash);
    }

    public String approveCourse(Course course) {
        course.setApprovalstate("APPROVED");
        return "APPROVED";
    }

    public String rejectCourse(Course course) {
        course.setApprovalstate("REJECTED");
        return "REJECTED";
    }

    public String pendingCourse(Course course) {
        course.setApprovalstate("PENDING");
        return "PENDING";
    }
}
