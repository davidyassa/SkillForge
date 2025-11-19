/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package frontend;

import javax.swing.*;
import controller.UserService;
import backend.Student;
import backend.Instructor;
import backend.User;

public class LoginPanel extends JPanel {

    private JTextField gmailField;
    private JPasswordField passwordField;

    public LoginPanel(FrameManager frame) {
        super();
        setLayout(null);

        JLabel loginword = new JLabel("LOGIN");
        loginword.setBounds(400, 25, 200, 60);
        add(loginword);

        JLabel gmail = new JLabel("Gmail / Username:");
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

        JButton loginButton = new JButton("Login");
        loginButton.setBounds(325, 300, 150, 40);
        add(loginButton);

        JButton logoutButton = new JButton("Logout");
        logoutButton.setBounds(325, 370, 150, 40);
        add(logoutButton);

        UserService userService = frame.getUserService();

        loginButton.addActionListener(e -> {
            String input = gmailField.getText().trim();
            String passwordinput = new String(passwordField.getPassword()).trim();

            if(input.isEmpty() || passwordinput.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all fields!");
                return;
            }

          
            User user = userService.login(input, passwordinput);
            if(user == null) {
                JOptionPane.showMessageDialog(this, "Invalid email/username or password!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

          
            if(user instanceof Student s) {
                frame.setCurrentStudent(s);
                frame.switchPanel(new StudentDashboardFrame(frame));
            } else if(user instanceof Instructor i) {
                frame.setCurrentInstructor(i);
                frame.switchPanel(new InstructorDashboardFrame(frame));
            }
        });

        logoutButton.addActionListener(e -> frame.showMainMenu());
    }
}
