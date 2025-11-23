/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package frontend;

import main.FrameManager;
import javax.swing.*;
import controller.UserService;
import backend.Student;
import backend.Instructor;

public class SignupPanel extends JPanel {

    private JTextField emailField;
    private JTextField usernameField;
    private JPasswordField passwordField;

    public SignupPanel(FrameManager frame) {
        setLayout(null);

        JLabel signupLabel = new JLabel("SIGN UP");
        signupLabel.setBounds(350, 25, 200, 60);
        add(signupLabel);

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setBounds(100, 100, 200, 40);
        add(emailLabel);

        emailField = new JTextField();
        emailField.setBounds(300, 100, 300, 40);
        add(emailField);

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setBounds(100, 160, 200, 40);
        add(usernameLabel);

        usernameField = new JTextField();
        usernameField.setBounds(300, 160, 300, 40);
        add(usernameField);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(100, 220, 200, 40);
        add(passwordLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(300, 220, 300, 40);
        add(passwordField);

        JButton studentButton = new JButton("Student");
        studentButton.setBounds(180, 300, 120, 40);
        add(studentButton);

        JButton instructorButton = new JButton("Instructor");
        instructorButton.setBounds(330, 300, 120, 40);
        add(instructorButton);

        JButton adminButton = new JButton("Admin");
        adminButton.setBounds(480, 300, 120, 40);
        add(adminButton);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.setBounds(330, 400, 120, 40);
        add(cancelButton);

        UserService userService = frame.getUserService();

        studentButton.addActionListener(e -> {
            String email = emailField.getText().trim();
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword());

            if (email.isEmpty() || username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all fields!");
                return;
            }

            try {
                Student s = userService.registerStudent(username, email, password);
                frame.setCurrentStudent(s);
                JOptionPane.showMessageDialog(this, "Account created successfully!");
                frame.switchPanel(new StudentDashboardFrame(frame));
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        instructorButton.addActionListener(e -> {
            String email = emailField.getText().trim();
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword());

            if (email.isEmpty() || username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all fields!");
                return;
            }

            try {
                Instructor ins = userService.registerInstructor(username, email, password);
                frame.setCurrentInstructor(ins);
                JOptionPane.showMessageDialog(this, "Account created successfully!");
                frame.switchPanel(new InstructorDashboardFrame(frame));
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        cancelButton.addActionListener(e -> frame.showMainMenu());
    }
}
