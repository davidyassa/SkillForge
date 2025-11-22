/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package frontend;

import main.FrameManager;
import backend.Course;
import backend.JsonDatabaseManager;
import controller.CourseService;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class AdminDashboardFrame extends JPanel {

    private final FrameManager frame;
    private final JsonDatabaseManager db;
    private final CourseService courseService;

    private final DefaultListModel<Course> pendingCoursesModel;
    private final JList<Course> pendingCoursesList;
    private final JTextArea courseDetails;
    private final JButton approveButton, declineButton;
    private final JButton logoutButton;

    public AdminDashboardFrame(FrameManager frame) {
        this.frame = frame;
        this.db = frame.getDb();
        this.courseService = frame.getCourseService();

        setLayout(new BorderLayout(10, 10));

        // --- Top panel with title and logout ---
        JPanel topPanel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel("Admin Dashboard - Pending Courses");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        topPanel.add(titleLabel, BorderLayout.WEST);

        logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> frame.showMainMenu());
        topPanel.add(logoutButton, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);

        // --- Left: Pending Courses List ---
        pendingCoursesModel = new DefaultListModel<>();
        pendingCoursesList = new JList<>(pendingCoursesModel);
        pendingCoursesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane listScroll = new JScrollPane(pendingCoursesList);
        listScroll.setPreferredSize(new Dimension(250, 400));

        // --- Center: Course Details ---
        courseDetails = new JTextArea();
        courseDetails.setEditable(false);
        courseDetails.setLineWrap(true);
        courseDetails.setWrapStyleWord(true);
        JScrollPane detailsScroll = new JScrollPane(courseDetails);

        // --- Bottom: Approve / Decline buttons ---
        approveButton = new JButton("Approve");
        declineButton = new JButton("Decline");
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(approveButton);
        buttonPanel.add(declineButton);

        // --- Combine center and buttons ---
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(detailsScroll, BorderLayout.CENTER);
        centerPanel.add(buttonPanel, BorderLayout.SOUTH);

        // --- Split pane ---
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, listScroll, centerPanel);
        splitPane.setDividerLocation(250);
        add(splitPane, BorderLayout.CENTER);

        // --- Load pending courses ---
        loadPendingCourses();

        // --- List selection ---
        pendingCoursesList.addListSelectionListener(e -> showCourseDetails());

        // --- Button actions ---
        approveButton.addActionListener(e -> updateCourseStatus("APPROVED"));
        declineButton.addActionListener(e -> updateCourseStatus("DECLINED"));
    }

    private void loadPendingCourses() {
        pendingCoursesModel.clear();
        ArrayList<Course> pendingCourses = db.getPendingCourses();
        for (Course c : pendingCourses) {
            pendingCoursesModel.addElement(c);
        }
    }

    private void showCourseDetails() {
        Course selected = pendingCoursesList.getSelectedValue();
        if (selected != null) {
            StringBuilder details = new StringBuilder();
            details.append("ID: ").append(selected.getID()).append("\n");
            details.append("Title: ").append(selected.getTitle()).append("\n");
            details.append("Instructor ID: ").append(selected.getInstructorId()).append("\n");
            details.append("Description: ").append(selected.getDescription()).append("\n");
            details.append("Approval State: ").append(selected.getApprovalstate()).append("\n");
            details.append("Lessons:\n");
            selected.getLessons().forEach(l -> details.append("  - ").append(l.getTitle()).append("\n"));
            courseDetails.setText(details.toString());
        } else {
            courseDetails.setText("");
        }
    }

    private void updateCourseStatus(String newStatus) {
        Course selected = pendingCoursesList.getSelectedValue();
        if (selected != null) {
            db.updateCourseApproval(selected, newStatus);
            loadPendingCourses();
            courseDetails.setText("");
            JOptionPane.showMessageDialog(this, "Course " + newStatus.toLowerCase() + " successfully!");
        } else {
            JOptionPane.showMessageDialog(this, "Please select a course first.");
        }
    }
}
