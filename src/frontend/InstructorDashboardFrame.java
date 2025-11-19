/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package frontend;

import backend.Course;
import backend.Instructor;
import backend.JsonDatabaseManager;
import controller.CourseService;
import java.util.logging.Logger;
import backend.JsonDatabaseManager;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class InstructorDashboardFrame extends JPanel {

    private Instructor currentInstructor;
    private CourseService courseService;
    private static JsonDatabaseManager db;
    private FrameManager frame;
  
   public void setDB(JsonDatabaseManager dbm) {
        db = dbm;
    }

    public InstructorDashboardFrame(Instructor instructor) {
        this.currentInstructor = instructor;
        JsonDatabaseManager dbManager = new JsonDatabaseManager("users.json", "courses.json");
        this.courseService = new CourseService(dbManager);
        initComponents();
        customInit();
   
    private DefaultListModel<Course> createdCoursesModel;
    private JList<Course> createdCoursesList;
    private JButton createCourseButton;
    private JButton logoutButton;
    private JLabel welcomeLabel;

    public InstructorDashboardFrame(FrameManager frame) {
        this.frame = frame;
        this.currentInstructor = frame.getCurrentInstructor();
        this.courseService = frame.getCourseService();

        setLayout(new BorderLayout(10, 10));

        // Top panel
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

    private void customInit() {
if (currentInstructor != null) {
            setTitle("Instructor Dashboard - " + currentInstructor.getUsername());
        } else {
             setTitle("Instructor Dashboard");
        }

        loadCoursesData();
    }

    private void loadCoursesData() {
logger.info("Loading courses for instructor: " + (currentInstructor != null ? currentInstructor.getUsername() : "Unknown"));
        welcomeLabel = new JLabel("Welcome, " + currentInstructor.getUsername());
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 16));
        topPanel.add(welcomeLabel, BorderLayout.WEST);

        logoutButton = new JButton("Logout");
        topPanel.add(logoutButton, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);

        // Center panel: created courses
        createdCoursesModel = new DefaultListModel<>();
        createdCoursesList = new JList<>(createdCoursesModel);
        createCourseButton = new JButton("Create New Course");

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBorder(BorderFactory.createTitledBorder("My Created Courses"));
        centerPanel.add(new JScrollPane(createdCoursesList), BorderLayout.CENTER);
        centerPanel.add(createCourseButton, BorderLayout.SOUTH);

        add(centerPanel, BorderLayout.CENTER);

        // Load instructor's courses
        loadCreatedCourses();

        // Action listeners
        createCourseButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Course creation not implemented yet.");
        });

        logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> frame.showMainMenu());
    }

    private void loadCreatedCourses() {
        createdCoursesModel.clear();
        List<Course> courses = db.getCoursesForInstructor(currentInstructor.getID());
        for (Course c : courses) {
            createdCoursesModel.addElement(c);
        }
    }
}
