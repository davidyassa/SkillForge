package frontend;

import backend.Certificate;
import main.FrameManager;
import backend.Course;
import backend.Lesson;
import backend.Quiz;
import backend.Student;
import controller.*;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class StudentDashboardFrame extends JPanel {

    private Student currentStudent;
    private CourseService cs;
    private FrameManager frame;

    private DefaultListModel<Course> availableCoursesModel;
    private DefaultListModel<Course> enrolledCoursesModel;
    private DefaultListModel<Lesson> lessonsModel;
    private DefaultListModel<Certificate> certificatesModel;

    private JList<Course> availableCoursesList;
    private JList<Course> enrolledCoursesList;
    private JList<Lesson> lessonsList;
    private JList<Certificate> certificatesList;

    private JButton enrollButton;
    private JButton completeLessonButton;
    private JButton logoutButton;
    private JButton viewCertButton;
    private JButton downloadCertButton;

    private JPopupMenu downloadMenu;
    private JMenuItem txtOption;
    private JMenuItem pdfOption;

    private JPanel availablePanel;
    private JPanel enrolledPanel;
    private JPanel lessonsPanel;
    private JPanel certButtons;
    private JPanel certificatesPanel;

    private JLabel studentNameLabel;
    private JLabel progressLabel;

    public StudentDashboardFrame(FrameManager frame) {
        this.frame = frame;
        this.currentStudent = frame.getCurrentStudent();
        this.cs = frame.getCourseService();

        availableCoursesModel = new DefaultListModel<>();
        enrolledCoursesModel = new DefaultListModel<>();
        lessonsModel = new DefaultListModel<>();
        certificatesModel = new DefaultListModel<>();

        setLayout(new BorderLayout(10, 10));

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        studentNameLabel = new JLabel("Welcome, " + currentStudent.getUsername());
        studentNameLabel.setFont(new Font("Arial", Font.BOLD, 16));

        progressLabel = new JLabel(" Select a course to view progress");
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
        certificatesList = new JList<>(certificatesModel);
        //Buttons
        enrollButton = new JButton("Enroll in Selected Course");
        completeLessonButton = new JButton("Mark Lesson as Completed");
        viewCertButton = new JButton("View");
        downloadCertButton = new JButton("Download ▼");

        //Panels
        availablePanel = createPanel("Available Courses", availableCoursesList, enrollButton);
        enrolledPanel = createPanel("My Courses", enrolledCoursesList, null);
        lessonsPanel = createPanel("Lessons", lessonsList, completeLessonButton);
        certButtons = new JPanel(new GridLayout(1, 2, 10, 0));
        certificatesPanel = new JPanel(new BorderLayout());

        certificatesPanel.setBorder(BorderFactory.createTitledBorder("Certificates Earned"));
        certificatesPanel.add(new JScrollPane(certificatesList), BorderLayout.CENTER);
        certButtons.add(viewCertButton);
        certButtons.add(downloadCertButton);
        certificatesPanel.add(certButtons, BorderLayout.SOUTH);

        // dropdown certView menu
        downloadMenu = new JPopupMenu();
        txtOption = new JMenuItem("Download as .txt");
        pdfOption = new JMenuItem("Download as PDF");

        downloadMenu.add(txtOption);
        downloadMenu.add(pdfOption);

        JSplitPane lessonsSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, lessonsPanel, certificatesPanel);
        lessonsSplit.setResizeWeight(0.8);
        lessonsSplit.setDividerLocation(0.8);

        JSplitPane mainSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, availablePanel, enrolledPanel);
        mainSplit.setResizeWeight(0.5);
        mainSplit.setDividerLocation(0.5);

        JSplitPane rightSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, mainSplit, lessonsSplit);
        rightSplit.setResizeWeight(0.66);

        add(rightSplit, BorderLayout.CENTER);

        loadAvailableCourses();
        loadEnrolledCourses();
        loadCertificates();

        downloadCertButton.addActionListener(e -> {
            downloadMenu.show(downloadCertButton, 0, downloadCertButton.getHeight());
        });
        enrollButton.addActionListener(e -> enrollSelectedCourse());
        completeLessonButton.addActionListener(e -> completeLesson());
        viewCertButton.addActionListener(e -> {
            Certificate cert = certificatesList.getSelectedValue();
            CertificateService certServ = frame.getCertService();
            if (cert != null) {
                JOptionPane.showMessageDialog(
                        this,
                        certServ.certificateText(cert),
                        "Certificate Details",
                        JOptionPane.INFORMATION_MESSAGE
                );
            }
        });
        txtOption.addActionListener(e -> {
            Certificate cert = certificatesList.getSelectedValue();
            CertificateService certServ = frame.getCertService();
            if (cert != null) {
                certServ.saveAsTextFile(cert);
                JOptionPane.showMessageDialog(this, "Certificate saved as TXT.");
            }
        });

        pdfOption.addActionListener(e -> {
            Certificate cert = certificatesList.getSelectedValue();
            CertificateService certServ = frame.getCertService();

            if (cert != null) {
                certServ.saveAsPDF(cert);
                JOptionPane.showMessageDialog(this, "Certificate saved as PDF.");
            }
        });

        enrolledCoursesList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                loadLessons();
            }
        });
        lessonsList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(
                    JList<?> list, Object value, int index,
                    boolean isSelected, boolean cellHasFocus) {

                Component comp = super.getListCellRendererComponent(
                        list, value, index, isSelected, cellHasFocus);

                Lesson lesson = (Lesson) value;
                Course selectedCourse = enrolledCoursesList.getSelectedValue();

                if (selectedCourse != null) {
                    boolean completed = cs.isLessonCompleted(
                            currentStudent.getID(),
                            lesson.getID()
                    );

                    if (completed) {
                        comp.setForeground(new Color(0, 128, 0));
                        comp.setFont(comp.getFont().deriveFont(Font.BOLD));
                    } else {
                        comp.setForeground(Color.BLACK);
                        comp.setFont(comp.getFont().deriveFont(Font.PLAIN));
                    }
                }

                return comp;
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
        List<Course> all = cs.getAllCourses();
        List<String> enrolled = currentStudent.getEnrolledCourses();
        for (Course c : all) {
            if (!enrolled.contains(c.getID())) {
                availableCoursesModel.addElement(c);
            }
        }
    }

    private void loadEnrolledCourses() {
        enrolledCoursesModel.clear();
        List<Course> courses = cs.getEnrolledCourses(currentStudent.getID());
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
            double p = cs.getCourseProgress(currentStudent.getID(), c.getID());
            progressLabel.setText("Progress: " + String.format("%.1f%%", p));
        }
        loadCertificates();
    }

    private void loadCertificates() {
        certificatesModel.clear();
        Student s = this.currentStudent;
        for (Certificate cert : s.getCertificates()) {
            certificatesModel.addElement(cert);
        }

    }

    private void enrollSelectedCourse() {
        Course c = availableCoursesList.getSelectedValue();
        if (c == null) {
            JOptionPane.showMessageDialog(this, "Select a course first.");
            return;
        }
        boolean ok = cs.enrollStudent(currentStudent.getID(), c.getID());
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

        if (!l.getQuizzes().isEmpty()) {
            Quiz q = l.getQuizzes().get(0);

            boolean alreadyPassed = frame.getQuizService().hasStudentPassedQuiz(currentStudent.getID(), c.getID(), q.getID());

            if (!alreadyPassed) {
                int choice = JOptionPane.showConfirmDialog(this,
                        "This lesson has a quiz. You must pass it to complete the lesson.\nTake quiz now?",
                        "Quiz Required", JOptionPane.YES_NO_OPTION);

                if (choice == JOptionPane.YES_OPTION) {
                    QuizTakingDialog quizDialog = new QuizTakingDialog(frame, q, currentStudent.getID(), c.getID(), frame.getQuizService());
                    quizDialog.setVisible(true);

                    if (!quizDialog.isPassed()) {
                        return;
                    }
                } else {
                    return;
                }
            }
        }

        boolean ok = cs.completeLesson(currentStudent.getID(), c.getID(), l.getID());
        if (ok) {
            loadLessons();
            loadCertificates();
            JOptionPane.showMessageDialog(this, "Lesson completed!");
        }
    }
}
