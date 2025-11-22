package frontend;

import main.FrameManager;
import backend.Course;
import backend.Lesson;
import backend.Student;
import controller.CourseService;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class StudentDashboardFrame extends JPanel {

    private Student currentStudent;
    private CourseService courseService;
    private FrameManager frame;

    private DefaultListModel<Course> availableCoursesModel;
    private DefaultListModel<Course> enrolledCoursesModel;
    private DefaultListModel<Lesson> lessonsModel;

    private JList<Course> availableCoursesList;
    private JList<Course> enrolledCoursesList;
    private JList<Lesson> lessonsList;

    private JButton enrollButton;
    private JButton completeLessonButton;
    private JButton logoutButton;

    private JLabel studentNameLabel;
    private JLabel progressLabel;

    public StudentDashboardFrame(FrameManager frame) {
        this.frame = frame;
        this.currentStudent = frame.getCurrentStudent();
        this.courseService = frame.getCourseService();

        availableCoursesModel = new DefaultListModel<>();
        enrolledCoursesModel = new DefaultListModel<>();
        lessonsModel = new DefaultListModel<>();

        setLayout(new BorderLayout(10, 10));

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        studentNameLabel = new JLabel("Welcome, " + currentStudent.getUsername());
        studentNameLabel.setFont(new Font("Arial", Font.BOLD, 16));

        progressLabel = new JLabel("Select a course to view progress");
        progressLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        topPanel.add(studentNameLabel, BorderLayout.WEST);
        topPanel.add(progressLabel, BorderLayout.CENTER);

        logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> frame.showMainMenu());
        topPanel.add(logoutButton, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);

        availableCoursesList = new JList<>(availableCoursesModel);
        enrolledCoursesList = new JList<>(enrolledCoursesModel);
        lessonsList = new JList<>(lessonsModel);

        enrollButton = new JButton("Enroll in Selected Course");
        completeLessonButton = new JButton("Mark Lesson as Completed");

        JPanel availablePanel = createPanel("Available Courses", availableCoursesList, enrollButton);
        JPanel enrolledPanel = createPanel("My Courses", enrolledCoursesList, null);
        JPanel lessonsPanel = createPanel("Lessons", lessonsList, completeLessonButton);

        JSplitPane mainSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, availablePanel, enrolledPanel);
        mainSplit.setResizeWeight(0.5);

        JSplitPane rightSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, mainSplit, lessonsPanel);
        rightSplit.setResizeWeight(0.66);

        add(rightSplit, BorderLayout.CENTER);

        loadAvailableCourses();
        loadEnrolledCourses();

        enrollButton.addActionListener(e -> enrollSelectedCourse());
        completeLessonButton.addActionListener(e -> completeLesson());
        enrolledCoursesList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                loadLessons();
            }
        });
    }

    private JPanel createPanel(String title, JComponent list, JButton button) {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createTitledBorder(title));
        panel.add(new JScrollPane(list), BorderLayout.CENTER);
        if (button != null) {
            panel.add(button, BorderLayout.SOUTH);
        }
        return panel;
    }

    private void loadAvailableCourses() {
        availableCoursesModel.clear();
        List<Course> all = courseService.getAllCourses();
        List<String> enrolled = currentStudent.getEnrolledCourses();
        for (Course c : all) {
            if (!enrolled.contains(c.getID())) {
                availableCoursesModel.addElement(c);
            }
        }
    }

    private void loadEnrolledCourses() {
        enrolledCoursesModel.clear();
        List<Course> courses = courseService.getEnrolledCourses(currentStudent.getID());
        for (Course c : courses) {
            enrolledCoursesModel.addElement(c);
        }
    }

    private void loadLessons() {
        lessonsModel.clear();
        Course c = enrolledCoursesList.getSelectedValue();
        if (c != null) {
            for (Lesson l : c.getLessons()) {
                lessonsModel.addElement(l);
            }
            double p = courseService.getCourseProgress(currentStudent.getID(), c.getID());
            progressLabel.setText("Progress: " + String.format("%.1f%%", p));
        }
    }

    private void enrollSelectedCourse() {
        Course c = availableCoursesList.getSelectedValue();
        if (c == null) {
            JOptionPane.showMessageDialog(this, "Select a course first.");
            return;
        }
        boolean ok = courseService.enrollStudent(currentStudent.getID(), c.getID());
        if (ok) {
            loadAvailableCourses();
            loadEnrolledCourses();
            JOptionPane.showMessageDialog(this, "Enrolled!");
        }
    }

    private void completeLesson() {
        Course c = enrolledCoursesList.getSelectedValue();
        Lesson l = lessonsList.getSelectedValue();
        if (c == null || l == null) {
            JOptionPane.showMessageDialog(this, "Select a lesson.");
            return;
        }
        boolean ok = courseService.completeLesson(currentStudent.getID(), c.getID(), l.getID());
        if (ok) {
            loadLessons();
            JOptionPane.showMessageDialog(this, "Lesson completed!");
        }
    }
}
