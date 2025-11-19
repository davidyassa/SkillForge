/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import backend.*;

public class UserService {

    private final JsonDatabaseManager db;

    public UserService(JsonDatabaseManager db) {
        this.db = db;
    }

    /**
     *
     * @param input may be email or username
     * @param password plainText password
     * @return true only if login succeeds
     */
    public User login(String input, String password) {
        User u;
        if (db.isValidEmail(input)) {
            u = db.findUserByEmail(input);
        } else {
            u = db.findUserByUsername(input);
        }
        if (u == null) {
            return null;
        }
        return u.isPasswordCorrect(password) ? u : null;
    }

    public Student registerStudent(String username, String email, String password) throws IllegalArgumentException {
        if (userExistsByUsername(username)) {
            throw new IllegalArgumentException("Username already exists!");
        }
        if (userExistsByEmail(email)) {
            throw new IllegalArgumentException("Email already exists!");
        }
        if (!db.isValidEmail(email)) {
            throw new IllegalArgumentException("Invalid email!");
        }

        String id = JsonDatabaseManager.generateUserId();
        String hash = JsonDatabaseManager.HashUtil.hashPassword(password);

        Student s = new Student(id, username, email, hash);
        db.addUser(s);
        return s;
    }

    public Instructor registerInstructor(String username, String email, String password) throws IllegalArgumentException {
        if (userExistsByUsername(username)) {
            throw new IllegalArgumentException("Username already exists!");
        }
        if (userExistsByEmail(email)) {
            throw new IllegalArgumentException("Email already exists!");
        }
        if (!db.isValidEmail(email)) {
            throw new IllegalArgumentException("Invalid email!");
        }

        String id = JsonDatabaseManager.generateUserId();
        String hash = JsonDatabaseManager.HashUtil.hashPassword(password);

        Instructor ins = new Instructor(id, username, email, hash);
        db.addUser(ins);
        return ins;
    }

    public boolean deleteUser(String userId) {
        User u = db.findUserById(userId);
        if (u == null) {
            return false;
        }
        if (u instanceof Student s) {
            // remove student from each course
            for (Course c : db.getCourses()) {
                c.getEnrolledStudents().remove(s.getID());
            }
        }
        if (u instanceof Instructor ins) {
            // delete every course created by this instructor
            for (String courseId : ins.getCreatedCourses()) {
                Course c = db.findCourseById(courseId);
                if (c != null) {
                    for (String studentId : c.getEnrolledStudents()) {
                        Student st = (Student) db.findUserById(studentId);
                        if (st != null) {
                            st.getEnrolledCourses().remove(courseId);
                            StudentCourseProgress scp = st.getSCP(courseId);
                            if (scp != null) {
                                st.getAllProgress().remove(scp);
                            }
                        }
                    }
                }
                db.removeCourse(courseId);
            }
        }
        db.removeUser(u);
        return true;
    }

    public boolean updateUserInfo(String userId, String newUsername, String newEmail) {
        User u = db.findUserById(userId);
        if (u == null) {
            return false;
        }
        if (!u.getUsername().equals(newUsername) && userExistsByUsername(newUsername)) {
            throw new IllegalArgumentException("Username already taken!");
        }

        if (!u.getEmail().equals(newEmail) && userExistsByEmail(newEmail)) {
            throw new IllegalArgumentException("Email already exists!");
        }

        if (!db.isValidEmail(newEmail)) {
            throw new IllegalArgumentException("Invalid email format!");
        }

        u.setUsername(newUsername);
        u.setEmail(newEmail);

        db.saveUsers();
        return true;
    }

    public boolean userExistsById(String id) {
        return db.findUserById(id) != null;
    }

    public boolean userExistsByEmail(String email) {
        return db.findUserByEmail(email) != null;
    }

    public boolean userExistsByUsername(String username) {
        return db.findUserByUsername(username) != null;
    }

}
