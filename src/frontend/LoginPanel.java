/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package frontend;

import javax.swing.*;
import java.io.*;

public class LoginPanel extends JPanel {

    private JTextField gmailField;
    private JPasswordField passwordField;

    public LoginPanel(FrameManager frame) {
        super();
        setLayout(null);

        JLabel loginword = new JLabel("LOGIN");
        loginword.setBounds(400, 25, 200, 60);
        add(loginword);

        JLabel gmail = new JLabel("Gmail:");
        gmail.setBounds(100, 100, 200, 60);
        add(gmail);

        JLabel password = new JLabel("Password:");
        password.setBounds(100, 200, 200, 60);
        add(password);

        gmailField = new JTextField();
        gmailField.setBounds(300, 110, 300, 40);
        add(gmailField);

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
       /* loginButton.addActionListener(e -> {
            String gmailInput = gmailField.getText().trim();
            String passwordInput = new String(passwordField.getPassword()).trim();

            if (checkCredentials(gmailInput, passwordInput)) {
                frame.switchPanel(new StudentDashboardFrame(frame));
            } else {
                JOptionPane.showMessageDialog(this, "Invalid Gmail or password.");
            }
        });
    }

    private boolean checkCredentials(String gmail, String password) {
        try (BufferedReader br = new BufferedReader(new FileReader("users.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    if (parts[0].trim().equals(gmail) && parts[1].trim().equals(password)) {
                        return true;
                    }
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error reading file: " + e.getMessage());
        }
        return false;
    }*/
    }
}