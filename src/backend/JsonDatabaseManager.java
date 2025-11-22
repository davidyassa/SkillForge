/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package backend;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import backend.util.RuntimeTypeAdapterFactory;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class JsonDatabaseManager {

    private static String usersFile;
    private static String coursesFile;
    private static final ArrayList<User> users = new ArrayList<>();
    private static final ArrayList<Course> courses = new ArrayList<>();

    public JsonDatabaseManager(String usersFile, String coursesFile) {
        JsonDatabaseManager.usersFile = usersFile;
        JsonDatabaseManager.coursesFile = coursesFile;
        StudentCourseProgress.setDB(this);
        Instructor.setDB(this);

        loadUsers();
        loadCourses();
    }

    public static class HashUtil { //inner class for hashing passwords (SHA-256)

        public static String hashPassword(String password) {
            try {
                java.security.MessageDigest md = java.security.MessageDigest.getInstance("SHA-256");
                byte[] hashBytes = md.digest(password.getBytes("UTF-8"));

                StringBuilder hex = new StringBuilder();
                for (byte b : hashBytes) {
                    hex.append(String.format("%02x", b));
                }
                return hex.toString();

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public ArrayList<Course> getCourses() {
        return courses;
    }

    public ArrayList<String> getUsernames() {
        ArrayList<String> usernames = new ArrayList<>();
        for (User u : users) {
            usernames.add(u.getUsername());
        }
        return usernames;
    }

    public User findUserByEmail(String email) {
        for (User user : users) {
            if (user.getEmail().equalsIgnoreCase(email)) {
                return user;
            }
        }
        return null;
    }

    public User findUserById(String userID) {
        for (User user : users) {
            if (user.getID().equals(userID)) {
                return user;
            }
        }
        return null;
    }

    public User findUserByUsername(String username) {
        for (User user : users) {
            if (user.getUsername().equalsIgnoreCase(username)) {
                return user;
            }
        }
        return null;
    }

    public Course findCourseById(String courseID) {
        for (Course course : courses) {
            if (course.getID().equals(courseID)) {
                return course;
            }
        }
        return null;
    }

    public Lesson findLessonById(String lessonID) {
        for (Course c : courses) {
            for (Lesson l : c.getLessons()) {
                if (l.getID().equals(lessonID)) {
                    return l;
                }
            }
        }
        return null;
    }

    public boolean isValidEmail(String input) { //validate and detect emails
        return input.matches("^[^@]{2,}@[A-Za-z0-9.-]{2,}\\.[A-Za-z0-9.-]{2,}$");
        //format: ab@cd.ef
    }

    public void addUser(User user) {
        users.add(user);
        this.saveUsers();
    }

    public void removeUser(User user) {
        users.removeIf(u -> u.getID().equals(user.getID()));
        this.saveUsers();
    }

    public void addCourse(Course course) {
    if (course.getApprovalstate() == null) {
        course.setApprovalstate("PENDING");
    }
    courses.add(course);
    saveCourses();
}

    public void removeCourse(String courseId) {
        courses.removeIf(c -> c.getID().equals(courseId));
        saveCourses();
    }

    public void updateCourses() {
        this.saveCourses();
    }

    public ArrayList<Course> getCoursesForInstructor(String instructorId) {
        ArrayList<Course> list = new ArrayList<>();
        for (Course course : courses) {
            if (course.getInstructorId().equals(instructorId)) {
                list.add(course);
            }
        }
        return list;
    }

    public ArrayList<Course> getCoursesForStudent(Student student) {
        ArrayList<Course> list = new ArrayList<>();
        for (String courseId : student.getEnrolledCourses()) {
            Course course = findCourseById(courseId);
            if (course != null) {
                list.add(course);
            }
        }
        return list;
    }

    public void enrollStudentInCourse(Student student, Course course) {
        student.enrollCourse(course.getID());
        course.enrollStudent(student.getID());
        if (student.getSCP(course.getID()) == null) {
            StudentCourseProgress scp = new StudentCourseProgress(student.getID(), course.getID());
            student.addSCP(scp);
        }
        saveUsers();
        saveCourses();
    }

    public static String generateUserID() {
        int id = 0;
        for (User u : users) {
            String numStr = u.getID().substring(1); //ignore "U"
            int num = Integer.parseInt(numStr);
            if (num > id) {
                id = num;
            }
        }
        return "U" + String.valueOf(++id);
    }

    public static String generateCourseID() {
        int id = 0;
        for (Course c : courses) {
            String numStr = c.getID().substring(1); //ignore "C"
            int num = Integer.parseInt(numStr);
            if (num > id) {
                id = num;
            }
        }
        return "C" + String.valueOf(++id);
    }

//LessonID = CourseID + generatedID, e.g. C20 -> L20-1
    public static String generateLessonID(Course course) {
        int id = 0;
        String courseStr = course.getID().substring(1); //ignore "C"
        int courseNum = Integer.parseInt(courseStr);
        for (Lesson l : course.getLessons()) {
            int lessonNum = Integer.parseInt(l.getID().split("-")[1]);
            if (id < lessonNum) {
                id = lessonNum;
            }
        }
        return "L" + String.valueOf(courseNum) + "-" + String.valueOf(++id);
    }

    public static String generateCertificateID(String studentId, String courseId, String issueDate) {
        String studentNum = studentId.substring(1);
        String courseNum = courseId.substring(1);

        return "CERT-" + issueDate + "-" + courseNum + "-" + studentNum;
    }

    private static RuntimeTypeAdapterFactory<User> getUserAdapter() { //Gson needs this to deal with extended classes 
        return RuntimeTypeAdapterFactory
                .of(User.class, "role")
                .registerSubtype(Student.class, "STUDENT")
                .registerSubtype(Instructor.class, "INSTRUCTOR");
    }

    public void loadUsers() {
        users.clear();
        Gson gson = new GsonBuilder()
                .registerTypeAdapterFactory(getUserAdapter())
                .create();

        try (FileReader reader = new FileReader(usersFile)) {
            Type listType = new TypeToken<ArrayList<User>>() {
            }.getType();
            ArrayList<User> loaded = gson.fromJson(reader, listType);

            if (loaded != null) {
                users.addAll(loaded);
            }

        } catch (Exception e) {
            // file missing = start empty
        }
    }

    public void loadCourses() {
        courses.clear();
        Gson gson = new GsonBuilder().create();

        try (FileReader reader = new FileReader(coursesFile)) {
            Type listType = new TypeToken<ArrayList<Course>>() {
            }.getType();
            ArrayList<Course> loaded = gson.fromJson(reader, listType);

            if (loaded != null) {
                courses.addAll(loaded);
            }

        } catch (Exception e) {
            // missing file → okay
        }
    }

    public void saveUsers() {
        Gson g = new GsonBuilder().setPrettyPrinting().create();
        try (FileWriter w = new FileWriter(usersFile)) {
            g.toJson(users, w);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveCourses() {
        Gson g = new GsonBuilder().setPrettyPrinting().create();
        try (FileWriter w = new FileWriter(coursesFile)) {
            g.toJson(courses, w);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public ArrayList<Course> getPendingCourses() {
    ArrayList<Course> list = new ArrayList<>();
    for (Course c : courses) {
        if (c.getApprovalstate() != null &&
            c.getApprovalstate().equalsIgnoreCase("PENDING")) {
            list.add(c);
        }
    }
    return list;
}
    
    
public void updateCourseApproval(Course course, String newState) {
    course.setApprovalstate(newState);
    saveCourses();
}


}
