/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
 /*
 * Question.java
 */
package backend;

import java.util.ArrayList;

public class Question {

    private String questionText;
    private ArrayList<String> options;
    private int correctOptionIndex; // 0 for the first option, 1 for second, etc.

    public Question(String questionText, ArrayList<String> options, int correctOptionIndex) {
        this.questionText = questionText;
        this.options = options;
        this.correctOptionIndex = correctOptionIndex;
    }

    public String getQuestionText() {
        return questionText;
    }

    public ArrayList<String> getOptions() {
        return options;
    }

    // Helper to check if a specific answer index is the correct one
    public boolean isCorrect(int index) {
        return index == correctOptionIndex;
    }
}
