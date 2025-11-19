/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package frontend;

/**
 *
 * @author DELL 7550
 */
import backend.Instructor;
import controller.CourseService;
import java.util.logging.Logger;

public class InstructorDashboardFrame extends javax.swing.JFrame {

    private static final Logger logger = Logger.getLogger(InstructorDashboardFrame.class.getName());

    private Instructor currentInstructor;
    private CourseService courseService;

    public InstructorDashboardFrame(Instructor instructor) {
        this.currentInstructor = instructor;
        this.courseService = new CourseService(dbManager);
        initComponents();
        customInit();
    }

    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 300, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>                        

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
    }

}
