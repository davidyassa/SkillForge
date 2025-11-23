/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/*
 * QuizCreationDialog.java
 */
package frontend;

import backend.Question;
import backend.Lesson;
import controller.QuizService;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class QuizCreationDialog extends JDialog {
    
    private final QuizService qs;
    private final Lesson lesson;
    private final ArrayList<Question> questions = new ArrayList<>();
    private final DefaultListModel<String> listModel = new DefaultListModel<>();
    
    public QuizCreationDialog(Frame owner, Lesson lesson, QuizService qs) {
        super(owner, "Create Quiz for " + lesson.getTitle(), true);
        this.qs = qs;
        this.lesson = lesson;
        
        initUI();
    }
    
    private void initUI() {
        setLayout(new BorderLayout(10, 10));
        setSize(600, 500);
        setLocationRelativeTo(getOwner());
        
        // List to show questions added so far
        JList<String> qList = new JList<>(listModel);
        add(new JScrollPane(qList), BorderLayout.CENTER);
        
        JPanel bottom = new JPanel();
        JButton addQ = new JButton("Add Question");
        JButton save = new JButton("Save Quiz");
        bottom.add(addQ);
        bottom.add(save);
        add(bottom, BorderLayout.SOUTH);
        
        addQ.addActionListener(e -> addQuestionDialog());
        
        save.addActionListener(e -> {
            if (questions.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Add at least one question.");
                return;
            }
            // Send data to Controller
            qs.createQuiz(lesson.getID(), questions);
            JOptionPane.showMessageDialog(this, "Quiz created successfully!");
            dispose(); // Close the window
        });
    }
    
    private void addQuestionDialog() {
        // Simple input fields for 1 Question and 4 Options
        JTextField qText = new JTextField();
        JTextField op1 = new JTextField();
        JTextField op2 = new JTextField();
        JTextField op3 = new JTextField();
        JTextField op4 = new JTextField();
        
        String[] options = {"Option 1", "Option 2", "Option 3", "Option 4"};
        JComboBox<String> correctBox = new JComboBox<>(options);
        
        JPanel p = new JPanel(new GridLayout(6, 2, 5, 5));
        p.add(new JLabel("Question Text:")); p.add(qText);
        p.add(new JLabel("Option 1:")); p.add(op1);
        p.add(new JLabel("Option 2:")); p.add(op2);
        p.add(new JLabel("Option 3:")); p.add(op3);
        p.add(new JLabel("Option 4:")); p.add(op4);
        p.add(new JLabel("Correct Option:")); p.add(correctBox);
        
        int res = JOptionPane.showConfirmDialog(this, p, "New Question", JOptionPane.OK_CANCEL_OPTION);
        if (res == JOptionPane.OK_OPTION) {
            // Validation: Ensure fields are not empty
            if(qText.getText().trim().isEmpty() || op1.getText().trim().isEmpty()) {
                 JOptionPane.showMessageDialog(this, "Fields cannot be empty.");
                 return;
            }

            ArrayList<String> ops = new ArrayList<>();
            ops.add(op1.getText());
            ops.add(op2.getText());
            ops.add(op3.getText());
            ops.add(op4.getText());
            
            // Create the Question object
            Question q = new Question(qText.getText(), ops, correctBox.getSelectedIndex());
            questions.add(q);
            listModel.addElement("Q: " + q.getQuestionText());
        }
    }
}