/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package frontend;

import controller.*;
import backend.Quiz;
/**
 *
 * @author Mostafa
 */
import main.FrameManager;

public class QuizTakingDialog {

    private boolean passed;

    public QuizTakingDialog(FrameManager frame, Quiz q, String studentID, String courseID, QuizService qService) {
    }

    public boolean isPassed() {
        return passed;
    }

    void setVisible(boolean b) {
        throw new UnsupportedOperationException("Not Implemented yet.");
    }

}
