/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package backend;

/**
 *
 * @author DELL 7550
 */
public class Quiz implements Searchable {

    private String quizId;
    private boolean passed;

    public boolean isPassed() {
        return passed;
    }

    public void markAsPassed(boolean passed) {
        this.passed = passed;
    }

    @Override
    public String getID() {
        return quizId;
    }

    public int getQuizUniqueID(){
        String [] parts = quizId.split("-"); //Q2-1
        return Integer.parseInt(parts[1]);
    }
    
    @Override
    public String toString(){
        return quizId;
    }
}
