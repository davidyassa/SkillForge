/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main;

import javax.swing.*;
import controller.*;
import frontend.*;
import backend.JsonDatabaseManager;
import backend.Student;
import backend.Instructor;

public class FrameManager extends JFrame {

    private final JsonDatabaseManager db;
    private final UserService userService;
    private final CourseService courseService;
    private final CertificateService certService;

    private final QuizService quizService;
    private Student currentStudent;
    private Instructor currentInstructor;

    public FrameManager() {
        this.setTitle("Skill Forge");
        this.setSize(800, 600);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);

        db = new JsonDatabaseManager("users.json", "courses.json");
        userService = new UserService(db);
        courseService = new CourseService(db);
        certService = new CertificateService(db);

        quizService = new QuizService(db);
        showMainMenu();
        this.setVisible(true);
    }

    public JsonDatabaseManager getDb() {
        return db;
    }

    public UserService getUserService() {
        return userService;
    }

    public CourseService getCourseService() {
        return courseService;
    }

    public CertificateService getCertService() {
        return certService;
    }

    public void setCurrentStudent(Student s) {
        currentStudent = s;
        currentInstructor = null;
    }

    public Student getCurrentStudent() {
        return currentStudent;
    }

    public void setCurrentInstructor(Instructor i) {
        currentInstructor = i;
        currentStudent = null;
    }

    public Instructor getCurrentInstructor() {
        return currentInstructor;
    }

    public final void showMainMenu() {
        JPanel panel = new JPanel();
        panel.setLayout(null);

        JButton loginButton = new JButton("Login");
        loginButton.setBounds(300, 150, 150, 40);
        panel.add(loginButton);

        JButton signupButton = new JButton("Sign up");
        signupButton.setBounds(300, 230, 150, 40);
        panel.add(signupButton);

        JButton exitButton = new JButton("Exit");
        exitButton.setBounds(300, 310, 150, 40);
        panel.add(exitButton);

        loginButton.addActionListener(e -> switchPanel(new LoginPanel(this)));
        signupButton.addActionListener(e -> switchPanel(new SignupPanel(this)));
        exitButton.addActionListener(e -> System.exit(0));

        setContentPane(panel);
        revalidate();
        repaint();
    }

    public void switchPanel(JPanel panel) {
        setContentPane(panel);
        revalidate();
        repaint();
    }

    public static void main(String[] args) {
        new FrameManager();
    }

    public QuizService getQuizService() {
        return quizService;
    }
}
