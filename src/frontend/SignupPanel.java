/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package frontend;

import javax.swing.*;
import java.io.*;

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
        studentButton.setBounds(250, 300, 120, 40);
        add(studentButton);

        JButton instructorButton = new JButton("Instructor");
        instructorButton.setBounds(400, 300, 120, 40);
        add(instructorButton);
        
         JButton logoutButton = new JButton("Logout");
        logoutButton.setBounds(325, 370, 150, 40);
        add(logoutButton);
       // studentButton.addActionListener(e -> frame.switchPanel(new StudentDashboardFrame(frame)));
        //instructorButton.addActionListener(e -> frame.switchPanel(new InstructorDashboardFrame(frame)));

        logoutButton.addActionListener(e -> frame.showMainMenu());
    }
    
}