package frontend;

import backend.Instructor;
import backend.Course; 
import backend.JsonDatabaseManager; 
import controller.CourseService;
import java.awt.BorderLayout; 
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.Map; 
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*; 

public class InstructorDashboardFrame extends JPanel { 

    private static final Logger logger = Logger.getLogger(InstructorDashboardFrame.class.getName());

    private FrameManager frameManager;
    private Instructor currentInstructor;
    private CourseService courseService;

    private JList<Course> coursesList;
    private DefaultListModel<Course> coursesListModel;
    private JButton createCourseButton;
    private JButton editCourseButton;
    private JButton deleteCourseButton;
    private JButton manageLessonsButton;
    private JButton viewStudentsButton;
    private JButton viewInsightsButton;
    private JButton logoutButton;

    public InstructorDashboardFrame(FrameManager frame) {
        this.frameManager = frame;
        this.currentInstructor = frame.getCurrentInstructor();
        
        if (this.currentInstructor == null) {
            JOptionPane.showMessageDialog(null, "Error: Instructor session not found.", "Fatal Error", JOptionPane.ERROR_MESSAGE);
            throw new IllegalStateException("Instructor must be set in FrameManager before launching dashboard.");
        }

        JsonDatabaseManager dbManager = new JsonDatabaseManager("users.json", "courses.json");
        Instructor.setDB(dbManager);
        this.courseService = new CourseService(dbManager);

        initComponents(); 
        customInit(); 
    }

    private void initComponents() {
     //   setTitle("Instructor Dashboard - Welcome, " + currentInstructor.getUsername());
     //   setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
      setLayout(new BorderLayout(5, 5));
      JLabel welcomeLabel = new JLabel("Instructor Dashboard - Welcome, " + currentInstructor.getUsername(), SwingConstants.CENTER);
        
        logoutButton = new JButton("Logout"); 
        
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.add(welcomeLabel, BorderLayout.CENTER);
        headerPanel.add(logoutButton, BorderLayout.EAST);
        
        coursesListModel = new DefaultListModel<>();
        coursesList = new JList<>(coursesListModel);
        JScrollPane scrollPane = new JScrollPane(coursesList);
        
        createCourseButton = new JButton("Create Course"); 
        editCourseButton = new JButton("Edit Course");     
        deleteCourseButton = new JButton("Delete Course"); 
        manageLessonsButton = new JButton("Manage Lessons"); 
        viewStudentsButton = new JButton("View Students"); 
        viewInsightsButton = new JButton("View Insights");
        logoutButton = new JButton("Logout"); 

        JPanel actionButtonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        actionButtonsPanel.add(createCourseButton);
        actionButtonsPanel.add(editCourseButton);
        actionButtonsPanel.add(deleteCourseButton);
        actionButtonsPanel.add(manageLessonsButton);
        actionButtonsPanel.add(viewStudentsButton);
        actionButtonsPanel.add(viewInsightsButton);
        
       /* JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.add(new JLabel("Your Courses:", SwingConstants.LEFT), BorderLayout.WEST);
        headerPanel.add(logoutButton, BorderLayout.EAST);
        
        getContentPane().setLayout(new BorderLayout(5, 5)); 
        getContentPane().add(headerPanel, BorderLayout.NORTH);
        getContentPane().add(scrollPane, BorderLayout.CENTER);
        getContentPane().add(actionButtonsPanel, BorderLayout.SOUTH);
        
        pack();
        setSize(700, 500);
        setLocationRelativeTo(null);*/
       
       actionButtonsPanel.add(deleteCourseButton);
        actionButtonsPanel.add(manageLessonsButton);
        actionButtonsPanel.add(viewStudentsButton);
        actionButtonsPanel.add(viewInsightsButton);
       
       add(headerPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(actionButtonsPanel, BorderLayout.SOUTH);

        createCourseButton.addActionListener(this::createCourseButtonActionPerformed);
        editCourseButton.addActionListener(this::editCourseButtonActionPerformed);
        deleteCourseButton.addActionListener(this::deleteCourseButtonActionPerformed);
        manageLessonsButton.addActionListener(this::manageLessonsButtonActionPerformed);
        viewStudentsButton.addActionListener(this::viewStudentsButtonActionPerformed);
        logoutButton.addActionListener(this::logoutButtonActionPerformed);
        viewInsightsButton.addActionListener(this::viewInsightsButtonActionPerformed);
    }
    
    private void customInit() {
        loadCoursesData();
    }

    private void loadCoursesData() {
        logger.info("Loading courses for instructor: " + currentInstructor.getUsername());

        try {
            List<Course> createdCourses = courseService.getCoursesByInstructorId(currentInstructor.getID());
            
            coursesListModel.clear();
            if (createdCourses != null) {
                for (Course course : createdCourses) {
                    coursesListModel.addElement(course);
                }
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error loading courses.", e);
            JOptionPane.showMessageDialog(this, "Error loading courses: " + e.getMessage(), 
                                         "Data Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    

    private void createCourseButtonActionPerformed(ActionEvent evt) {
        String title = JOptionPane.showInputDialog(this, "Enter course title:", "Create Course", JOptionPane.QUESTION_MESSAGE);
        
        if (title == null) return;
        
        String description = JOptionPane.showInputDialog(this, "Enter course description:", "Create Course", JOptionPane.QUESTION_MESSAGE);
        
        if (description == null) return;
        
        if (!title.trim().isEmpty() && !description.trim().isEmpty()) {
            try {
                courseService.createCourse(currentInstructor.getID(), title.trim(), description.trim());
                JOptionPane.showMessageDialog(this, "Course created successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadCoursesData();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Title and Description cannot be empty.", "Input Error", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void editCourseButtonActionPerformed(ActionEvent evt) {
        Course selectedCourse = coursesList.getSelectedValue();
        if (selectedCourse == null) {
            JOptionPane.showMessageDialog(this, "Please select a course to edit.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String newTitle = JOptionPane.showInputDialog(this, "Enter new title:", selectedCourse.getTitle());
        if (newTitle == null) return; 

        String newDescription = JOptionPane.showInputDialog(this, "Enter new description:", selectedCourse.getDescription());
        if (newDescription == null) return; 

        if (newTitle.trim().isEmpty() || newDescription.trim().isEmpty()) {
             JOptionPane.showMessageDialog(this, "Title and Description cannot be empty.", "Input Error", JOptionPane.ERROR_MESSAGE);
             return;
        }

        try {
            courseService.editCourse(selectedCourse, newTitle.trim(), newDescription.trim()); 
            loadCoursesData();
            JOptionPane.showMessageDialog(this, "Course updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error saving changes: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void deleteCourseButtonActionPerformed(ActionEvent evt) {
        Course selectedCourse = coursesList.getSelectedValue();
        if (selectedCourse == null) {
            JOptionPane.showMessageDialog(this, "Please select a course to delete.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete the course '" + selectedCourse.getTitle() + "'?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            
            try {
                if(courseService.deleteCourse(currentInstructor.getID(), selectedCourse.getID())) {
                    loadCoursesData(); 
                    JOptionPane.showMessageDialog(this, "Course deleted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Deletion failed. Course not found or instructor mismatch.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Data Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void manageLessonsButtonActionPerformed(ActionEvent evt) {
        Course selectedCourse = coursesList.getSelectedValue();
        if (selectedCourse == null) {
            JOptionPane.showMessageDialog(this, "Please select a course to manage lessons.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }
        logger.info("Manage Lessons action initiated for: " + selectedCourse.getTitle());

    }
    
    private void viewStudentsButtonActionPerformed(ActionEvent evt) {
        Course selectedCourse = coursesList.getSelectedValue();
        if (selectedCourse == null) {
            JOptionPane.showMessageDialog(this, "Please select a course to view enrolled students.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }
        logger.info("View Students action initiated for: " + selectedCourse.getTitle());

    }

    private void viewInsightsButtonActionPerformed(ActionEvent evt) {
        Course selectedCourse = coursesList.getSelectedValue();
        if (selectedCourse == null) {
            JOptionPane.showMessageDialog(this, "Please select a course to view insights.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Map<String, Object> insights = courseService.getCourseInsights(selectedCourse.getID());

            StringBuilder insightMessage = new StringBuilder();
            insightMessage.append("Course Insights: ").append(selectedCourse.getTitle()).append("\n\n");
            
            Double completion = (Double) insights.getOrDefault("completionPercentage", 0.0);
            insightMessage.append("Course Completion: ").append(String.format("%.1f%%", completion)).append("\n\n");
            

            Map<String, Double> quizAverages = (Map<String, Double>) insights.getOrDefault("quizAverages", Map.of());
            insightMessage.append("Quiz Averages per Lesson \n");
            quizAverages.forEach((lesson, avg) -> 
                insightMessage.append("- ").append(lesson).append(": ").append(String.format("%.1f%%", avg)).append("\n")
            );
            insightMessage.append("\n");
            
            Map<String, Double> studentScores = (Map<String, Double>) insights.getOrDefault("studentScores", Map.of());
            insightMessage.append("Top Student Scores \n");
            
            studentScores.entrySet().stream()
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .limit(3)
                .forEach(entry -> 
                    insightMessage.append("- ").append(entry.getKey()).append(": ").append(String.format("%.1f", entry.getValue())).append("\n")
                );

            JOptionPane.showMessageDialog(this, insightMessage.toString(), 
                                          "Course Analytics", JOptionPane.INFORMATION_MESSAGE);

            logger.info("View Insights action initiated for: " + selectedCourse.getTitle());

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error loading insights.", e);
            JOptionPane.showMessageDialog(this, "Error loading course insights: " + e.getMessage(), 
                                         "Data Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void logoutButtonActionPerformed(ActionEvent evt) {
        logger.info("User " + currentInstructor.getUsername() + " logging out.");
        frameManager.showMainMenu();
    }
}