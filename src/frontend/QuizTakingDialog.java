package frontend;

import backend.Question;
import backend.Quiz;
import controller.Q​uizService;
import main.FrameManager;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class QuizTakingDialog extends JDialog {

    private boolean passed = false;

    public QuizTakingDialog(FrameManager frame, Quiz quiz, String studentID,
            String courseID, QuizService qService) {

        super(frame, "Quiz", true);

        List<Question> questions = quiz.getQuestions();
        int total = questions.size();
        int[] answers = new int[total];

        setSize(550, 420);
        setLayout(new BorderLayout(10, 10));
        setLocationRelativeTo(frame);

        // ========= TOP LABEL: Question X of Y =========
        JLabel progressLabel = new JLabel("Question 1 of " + total, SwingConstants.CENTER);
        progressLabel.setFont(new Font("Arial", Font.BOLD, 16));
        add(progressLabel, BorderLayout.NORTH);

        // ========= CENTER PANEL WITH QUESTION =========
        JPanel questionPanel = new JPanel(new BorderLayout(10, 10));
        add(questionPanel, BorderLayout.CENTER);

        // ========= BOTTOM BUTTONS =========
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton backBtn = new JButton("Back");
        JButton nextBtn = new JButton("Next");
        bottom.add(backBtn);
        bottom.add(nextBtn);
        add(bottom, BorderLayout.SOUTH);

        final int[] index = {0};

        // initial draw
        drawQuestion(questionPanel, questions, answers, index[0]);

        // ========= NEXT BUTTON =========
        nextBtn.addActionListener(e -> {

            index[0]++;

            if (index[0] < total) {
                // move to next
                progressLabel.setText("Question " + (index[0] + 1) + " of " + total);
                drawQuestion(questionPanel, questions, answers, index[0]);
            } else {
                // SUBMIT QUIZ
                int score = qService.submitQuiz(studentID, courseID, quiz.getID(), answers);
                passed = (double) score / total >= 0.5;

                if (passed) {
                    JOptionPane.showMessageDialog(
                            this,
                            "Quiz Passed!",
                            "Quiz Result",
                            JOptionPane.INFORMATION_MESSAGE
                    );

                    frame.switchPanel(new StudentDashboardFrame(frame));
                } else {
                    JOptionPane.showMessageDialog(
                            this,
                            "You did not pass the quiz.\nTry again!",
                            "Quiz Result",
                            JOptionPane.ERROR_MESSAGE
                    );
                }

                dispose();
            }
        });

        // ========= BACK BUTTON =========
        backBtn.addActionListener(e -> {
            if (index[0] == 0) {
                return; // can't go back from Q1
            }
            index[0]--;
            progressLabel.setText("Question " + (index[0] + 1) + " of " + total);
            drawQuestion(questionPanel, questions, answers, index[0]);
        });

        setVisible(true);
    }

    private void drawQuestion(JPanel panel, List<Question> list, int[] answers, int index) {
        panel.removeAll();

        Question q = list.get(index);

        JLabel qLabel = new JLabel("<html><h3>" + q.getQuestionText() + "</h3></html>");

        ButtonGroup bg = new ButtonGroup();
        JPanel optPanel = new JPanel(new GridLayout(q.getOptions().size(), 1));

        for (int i = 0; i < q.getOptions().size(); i++) {
            JRadioButton btn = new JRadioButton(q.getOptions().get(i));
            int finalI = i;

            btn.addActionListener(e -> answers[index] = finalI);

            // restore selection if the user pressed "Back"
            if (answers[index] == i) {
                btn.setSelected(true);
            }

            bg.add(btn);
            optPanel.add(btn);
        }

        panel.add(qLabel, BorderLayout.NORTH);
        panel.add(optPanel, BorderLayout.CENTER);

        panel.revalidate();
        panel.repaint();
    }

    public boolean isPassed() {
        return passed;
    }
}
