/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package frontend;

import javax.swing.*;

public class FrameManager extends JFrame {

    public FrameManager() {
        this.setTitle("Skill Forge");
        this.setSize(800, 600);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        showMainMenu() ;
        
        this.setVisible(true);
    }

    public void showMainMenu() {
        JPanel panel = new JPanel();
        panel.setLayout(null);

        JButton loginButton = new JButton("Login");
        loginButton.setBounds(300, 150, 150, 40);
        panel.add(loginButton);

        JButton signupButton = new JButton("Sign up");
        signupButton.setBounds(300, 230, 150, 40);
        panel.add(signupButton);

        JButton exitButton = new JButton("Exit");
        exitButton.setBounds(300, 310, 150, 40);
        panel.add(exitButton);

        loginButton.addActionListener(e -> switchPanel(new LoginPanel(this)));
        signupButton.addActionListener(e -> switchPanel(new SignupPanel(this)));
        exitButton.addActionListener(e -> System.exit(0));

        setContentPane(panel);
        revalidate();
        repaint();
    }

    public void switchPanel(JPanel panel) {
        setContentPane(panel);
        revalidate();
        repaint();
    }

    public static void main(String[] args) {
        new FrameManager();
    }
}