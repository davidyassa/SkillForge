package frontend;

import main.FrameManager;
import backend.Course;
import backend.Instructor;
import backend.Lesson;
import backend.Student;
import controller.CourseService;
import controller.UserService;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Instructor dashboard panel: create/edit/delete courses, manage lessons, view
 * students. Uses FrameManager to obtain current instructor, services and db.
 */
public class InstructorDashboardFrame extends JPanel {

    private final FrameManager frame;
    private final CourseService cs;
    private final UserService us;
    private final Instructor currentInstructor;

    private DefaultListModel<Course> createdCoursesModel;
    private JList<Course> createdCoursesList;
    private JButton createCourseBtn;
    private JButton editCourseBtn;
    private JButton deleteCourseBtn;
    private JButton manageLessonsBtn;
    private JButton viewStudentsBtn;
    private JButton logoutBtn;
    private JLabel welcomeLabel;

    public InstructorDashboardFrame(FrameManager frame) {
        this.frame = frame;
        this.cs = frame.getCourseService();
        this.us = frame.getUserService();
        this.currentInstructor = frame.getCurrentInstructor();

        initUI();
        loadCreatedCourses();
    }

    private void initUI() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel top = new JPanel(new BorderLayout());
        welcomeLabel = new JLabel("Welcome, " + (currentInstructor != null ? currentInstructor.getUsername() : "Instructor"));
        welcomeLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        top.add(welcomeLabel, BorderLayout.WEST);

        logoutBtn = new JButton("Logout");
        logoutBtn.addActionListener(e -> frame.showMainMenu());
        top.add(logoutBtn, BorderLayout.EAST);

        add(top, BorderLayout.NORTH);

        createdCoursesModel = new DefaultListModel<>();
        createdCoursesList = new JList<>(createdCoursesModel);
        createdCoursesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane listScroll = new JScrollPane(createdCoursesList);

        createCourseBtn = new JButton("Create Course");
        editCourseBtn = new JButton("Edit Course");
        deleteCourseBtn = new JButton("Delete Course");
        manageLessonsBtn = new JButton("Manage Lessons");
        viewStudentsBtn = new JButton("View Students");

        JPanel buttons = new JPanel(new GridLayout(5, 1, 5, 5));
        buttons.add(createCourseBtn);
        buttons.add(editCourseBtn);
        buttons.add(deleteCourseBtn);
        buttons.add(manageLessonsBtn);
        buttons.add(viewStudentsBtn);

        JPanel center = new JPanel(new BorderLayout(8, 8));
        center.setBorder(BorderFactory.createTitledBorder("My Created Courses"));
        center.add(listScroll, BorderLayout.CENTER);
        center.add(buttons, BorderLayout.EAST);

        add(center, BorderLayout.CENTER);

        createCourseBtn.addActionListener(e -> createCourseDialog());
        editCourseBtn.addActionListener(e -> editSelectedCourse());
        deleteCourseBtn.addActionListener(e -> deleteSelectedCourse());
        manageLessonsBtn.addActionListener(e -> manageLessonsForSelectedCourse());
        viewStudentsBtn.addActionListener(e -> viewStudentsForSelectedCourse());
    }

    private void loadCreatedCourses() {
        createdCoursesModel.clear();
        if (currentInstructor == null) {
            return;
        }

        List<Course> courses = cs.getCoursesForInstructor(currentInstructor.getID());
        for (Course c : courses) {
            createdCoursesModel.addElement(c);
        }
    }

    private void createCourseDialog() {
        JTextField titleField = new JTextField();
        JTextArea descArea = new JTextArea(6, 30);
        JScrollPane descScroll = new JScrollPane(descArea);

        JPanel p = new JPanel(new BorderLayout(6, 6));
        p.add(new JLabel("Title:"), BorderLayout.NORTH);
        p.add(titleField, BorderLayout.CENTER);
        p.add(new JLabel("Description:"), BorderLayout.SOUTH);

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.add(p, BorderLayout.NORTH);
        wrapper.add(descScroll, BorderLayout.CENTER);

        int ok = JOptionPane.showConfirmDialog(this, wrapper, "Create Course", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (ok == JOptionPane.OK_OPTION) {
            String title = titleField.getText().trim();
            String desc = descArea.getText().trim();
            if (title.isEmpty() || desc.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Title and description are required.", "Validation", JOptionPane.WARNING_MESSAGE);
                return;
            }

            cs.createCourse(currentInstructor.getID(), title, desc);
            loadCreatedCourses();
            cs.saveCourses();
            us.saveUsers();
            JOptionPane.showMessageDialog(this, "Course created.");
        }
    }

    private Course getSelectedCourseOrWarn() {
        Course c = createdCoursesList.getSelectedValue();
        if (c == null) {
            JOptionPane.showMessageDialog(this, "Select a course first.", "No selection", JOptionPane.INFORMATION_MESSAGE);
        }
        return c;
    }

    private void editSelectedCourse() {
        Course sel = getSelectedCourseOrWarn();
        if (sel == null) {
            return;
        }

        JTextField titleField = new JTextField(sel.getTitle());
        JTextArea descArea = new JTextArea(sel.getDescription(), 6, 30);
        JScrollPane descScroll = new JScrollPane(descArea);

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.add(new JLabel("Title:"), BorderLayout.NORTH);
        wrapper.add(titleField, BorderLayout.CENTER);
        wrapper.add(descScroll, BorderLayout.SOUTH);

        int ok = JOptionPane.showConfirmDialog(this, wrapper, "Edit Course", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (ok == JOptionPane.OK_OPTION) {
            String newTitle = titleField.getText().trim();
            String newDesc = descArea.getText().trim();
            if (newTitle.isEmpty() || newDesc.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Title and description are required.", "Validation", JOptionPane.WARNING_MESSAGE);
                return;
            }
            currentInstructor.editCourse(sel, newTitle, newDesc);
            cs.saveCourses();
            loadCreatedCourses();
            JOptionPane.showMessageDialog(this, "Course updated.");
        }
    }

    private void deleteSelectedCourse() {
        Course sel = getSelectedCourseOrWarn();
        if (sel == null) {
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Delete course \"" + sel.getTitle() + "\" ? This will remove it for all students.", "Confirm delete", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }
        boolean ok = cs.deleteCourse(currentInstructor.getID(), sel.getID());
        if (ok) {
            cs.saveCourses();
            us.saveUsers();
            loadCreatedCourses();
            JOptionPane.showMessageDialog(this, "Course deleted.");
        } else {
            JOptionPane.showMessageDialog(this, "Failed to delete course. Make sure you are the owner.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void manageLessonsForSelectedCourse() {
        Course sel = getSelectedCourseOrWarn();
        if (sel == null) {
            return;
        }

        DefaultListModel<Lesson> lessonModel = new DefaultListModel<>();
        JList<Lesson> lessonList = new JList<>(lessonModel);
        for (Lesson L : sel.getLessons()) {
            lessonModel.addElement(L);
        }

        JButton add = new JButton("Add");
        JButton edit = new JButton("Edit");
        JButton del = new JButton("Delete");

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 8));
        bottom.add(add);
        bottom.add(edit);
        bottom.add(del);

        JPanel content = new JPanel(new BorderLayout(8, 8));
        content.add(new JScrollPane(lessonList), BorderLayout.CENTER);
        content.add(bottom, BorderLayout.SOUTH);

        JDialog dlg = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Manage lessons - " + sel.getTitle(), true);
        dlg.setContentPane(content);
        dlg.setSize(600, 400);
        dlg.setLocationRelativeTo(this);

        add.addActionListener(e -> {
            JTextField titleF = new JTextField();
            JTextArea contentA = new JTextArea(8, 30);
            JScrollPane sp = new JScrollPane(contentA);
            JPanel p = new JPanel(new BorderLayout(6, 6));
            p.add(new JLabel("Title:"), BorderLayout.NORTH);
            p.add(titleF, BorderLayout.CENTER);
            p.add(sp, BorderLayout.SOUTH);
            int r = JOptionPane.showConfirmDialog(dlg, p, "Add lesson", JOptionPane.OK_CANCEL_OPTION);
            if (r == JOptionPane.OK_OPTION) {
                String t = titleF.getText().trim();
                String cnt = contentA.getText().trim();
                if (t.isEmpty() || cnt.isEmpty()) {
                    JOptionPane.showMessageDialog(dlg, "Both title and content are required.", "Validation", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                Lesson newL = currentInstructor.addLesson(sel, t, cnt);
                lessonModel.addElement(newL);
                cs.saveCourses();
            }
        });

        edit.addActionListener(e -> {
            Lesson selL = lessonList.getSelectedValue();
            if (selL == null) {
                JOptionPane.showMessageDialog(dlg, "Select a lesson first.");
                return;
            }
            JTextField titleF = new JTextField(selL.getTitle());
            JTextArea contentA = new JTextArea(selL.getContent(), 8, 30);
            JScrollPane sp = new JScrollPane(contentA);
            JPanel p = new JPanel(new BorderLayout(6, 6));
            p.add(new JLabel("Title:"), BorderLayout.NORTH);
            p.add(titleF, BorderLayout.CENTER);
            p.add(sp, BorderLayout.SOUTH);
            int r = JOptionPane.showConfirmDialog(dlg, p, "Edit lesson", JOptionPane.OK_CANCEL_OPTION);
            if (r == JOptionPane.OK_OPTION) {
                selL.setTitle(titleF.getText().trim());
                selL.setContent(contentA.getText().trim());
                cs.saveCourses();
                lessonList.repaint();
            }
        });

        del.addActionListener(e -> {
            Lesson selL = lessonList.getSelectedValue();
            if (selL == null) {
                JOptionPane.showMessageDialog(dlg, "Select a lesson first.");
                return;
            }
            int conf = JOptionPane.showConfirmDialog(dlg, "Delete lesson \"" + selL.getTitle() + "\" ?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (conf == JOptionPane.YES_OPTION) {
                sel.getLessons().removeIf(l -> l.getID().equals(selL.getID()));
                lessonModel.removeElement(selL);
                cs.saveCourses();
            }
        });

        dlg.setVisible(true);
    }

    private void viewStudentsForSelectedCourse() {
        Course sel = getSelectedCourseOrWarn();
        if (sel == null) {
            return;
        }

        DefaultListModel<String> model = new DefaultListModel<>();
        for (String sid : sel.getEnrolledStudents()) {
            Student s = (Student) us.findUserByID(sid);
            String text = sid + (s != null ? " - " + s.getUsername() : "");
            model.addElement(text);
        }

        JList<String> list = new JList<>(model);
        JScrollPane sp = new JScrollPane(list);
        sp.setPreferredSize(new Dimension(400, 300));

        JOptionPane.showMessageDialog(this, sp, "Enrolled Students - " + sel.getTitle(), JOptionPane.PLAIN_MESSAGE);
    }
}
