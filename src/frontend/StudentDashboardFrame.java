/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package frontend;

import backend.Course;
import backend.Lesson;
import backend.Student;
import controller.CourseService;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class StudentDashboardFrame extends JFrame {

    private Student currentStudent;
    private CourseService courseService;

    private DefaultListModel<Course> availableCoursesModel;
    private DefaultListModel<Course> enrolledCoursesModel;
    private DefaultListModel<Lesson> lessonsModel;

    private JList<Course> availableCoursesList;
    private JList<Course> enrolledCoursesList;
    private JList<Lesson> lessonsList;
    private JButton enrollButton;
    private JButton completeLessonButton;
    private JLabel studentNameLabel;
    private JLabel progressLabel;
    private JPanel topPanel;
    private JSplitPane mainSplit;
    private JSplitPane rightSplit;

    public StudentDashboardFrame(Student student, CourseService courseService) {
        this.currentStudent = student;
        this.courseService = courseService;

        availableCoursesModel = new DefaultListModel<>();
        enrolledCoursesModel = new DefaultListModel<>();
        lessonsModel = new DefaultListModel<>();

        buildUI();

        loadAvailableCourses();
        loadEnrolledCourses();

        studentNameLabel.setText("Welcome, " + currentStudent.getUsername());
        progressLabel.setText("Select an enrolled course to see progress");

        setTitle("Student Dashboard: " + student.getUsername());
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    private void buildUI() {
        setLayout(new BorderLayout(10, 10));

        topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        studentNameLabel = new JLabel();
        studentNameLabel.setFont(new Font("Arial", Font.BOLD, 16));
        progressLabel = new JLabel();
        progressLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        topPanel.add(studentNameLabel, BorderLayout.WEST);
        topPanel.add(progressLabel, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        availableCoursesList = new JList<>(availableCoursesModel);
        enrolledCoursesList = new JList<>(enrolledCoursesModel);
        lessonsList = new JList<>(lessonsModel);

        enrollButton = new JButton("Enroll in Selected Course");
        completeLessonButton = new JButton("Mark Lesson as Completed");

        JPanel availablePanel = createTitledPanel("Available Courses", availableCoursesList, enrollButton);
        JPanel enrolledPanel = createTitledPanel("My Enrolled Courses", enrolledCoursesList, null);
        JPanel lessonsPanel = createTitledPanel("Course Lessons", lessonsList, completeLessonButton);

        mainSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, availablePanel, enrolledPanel);
        mainSplit.setResizeWeight(0.5);

        rightSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, mainSplit, lessonsPanel);
        rightSplit.setResizeWeight(0.66);

        add(rightSplit, BorderLayout.CENTER);

        enrollButton.addActionListener(e -> enrollSelectedCourse());
        completeLessonButton.addActionListener(e -> markSelectedLessonAsComplete());
        enrolledCoursesList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                loadLessonsForSelectedCourse();
            }
        });
    }

    private JPanel createTitledPanel(String title, JComponent list, JButton button) {
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
        List<Course> allCourses = courseService.getAllCourses();
        List<String> enrolledIds = currentStudent.getEnrolledCourses();

        for (Course course : allCourses) {
            if (!enrolledIds.contains(course.getID())) {
                availableCoursesModel.addElement(course);
            }
        }
    }

    private void loadEnrolledCourses() {
        enrolledCoursesModel.clear();
        List<Course> enrolledCourses = courseService.getEnrolledCourses(currentStudent.getID());
        for (Course course : enrolledCourses) {
            enrolledCoursesModel.addElement(course);
        }
    }

    private void enrollSelectedCourse() {
        Course selectedCourse = availableCoursesList.getSelectedValue();
        if (selectedCourse != null) {
            boolean success = courseService.enrollStudent(currentStudent.getID(), selectedCourse.getID());
            if (success) {
                loadAvailableCourses();
                loadEnrolledCourses();
                JOptionPane.showMessageDialog(this, "Enrollment successful!");
            } else {
                JOptionPane.showMessageDialog(this, "Enrollment failed.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a course to enroll.", "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void loadLessonsForSelectedCourse() {
        Course selectedCourse = enrolledCoursesList.getSelectedValue();
        if (selectedCourse != null) {
            lessonsModel.clear();
            List<Lesson> lessons = selectedCourse.getLessons();
            if (lessons != null) {
                for (Lesson lesson : lessons) {
                    lessonsModel.addElement(lesson);
                }
            }
            double progress = courseService.getCourseProgress(currentStudent.getID(), selectedCourse.getID());
            progressLabel.setText(String.format("Progress in [%s]: %.1f%%", selectedCourse.getTitle(), progress));
        } else {
            lessonsModel.clear();
            progressLabel.setText("Select an enrolled course to see progress");
        }
    }

    private void markSelectedLessonAsComplete() {
        Course selectedCourse = enrolledCoursesList.getSelectedValue();
        Lesson selectedLesson = lessonsList.getSelectedValue();
        if (selectedCourse != null && selectedLesson != null) {
            boolean success = courseService.completeLesson(
                    currentStudent.getID(),
                    selectedCourse.getID(),
                    selectedLesson.getID()
            );
            if (success) {
                loadLessonsForSelectedCourse();
                JOptionPane.showMessageDialog(this, "Lesson marked as complete!");
            } else {
                JOptionPane.showMessageDialog(this, "Action failed.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a course and a lesson.", "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }
}
