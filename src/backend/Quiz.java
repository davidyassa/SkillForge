/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/*
 * Quiz.java
 */
package backend;

import java.util.ArrayList;

public class Quiz implements Searchable {

    private String quizId;
    private ArrayList<Question> questions; // NEW: We store questions, not the "passed" status

    public Quiz(String quizId) {
        this.quizId = quizId;
        this.questions = new ArrayList<>();
    }

    @Override
    public String getID() {
        return quizId;
    }

    public ArrayList<Question> getQuestions() {
        return questions;
    }

    public void addQuestion(Question q) {
        questions.add(q);
    }
    
    // Logic to calculate score based on provided answer indices
    public int calculateScore(int[] answers) {
        int score = 0;
        for (int i = 0; i < questions.size(); i++) {
            // Check if the answer matches the question's correct index
            if (i < answers.length && questions.get(i).isCorrect(answers[i])) {
                score++;
            }
        }
        return score;
    }

    public int getQuizUniqueID(){
        // Assumes ID format like "Q1-1-1" (Course-Lesson-Quiz)
        String [] parts = quizId.split("-"); 
        // We grab the last number as the unique ID
        return Integer.parseInt(parts[parts.length - 1]);
    }
    
    @Override
    public String toString(){
        return quizId;
    }
}