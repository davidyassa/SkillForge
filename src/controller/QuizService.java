/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import backend.JsonDatabaseManager;
import backend.Lesson;
import backend.Quiz;

/**
 *
 * @author DELL 7550
 */
public class QuizService {

    private final JsonDatabaseManager db;

    public QuizService(JsonDatabaseManager db) {
        this.db = db;
    }

    public void addQuiz(String lessonID, Quiz quiz) {
        Lesson l = JsonDatabaseManager.findLessonById(lessonID);
        for (Quiz q : l.getQuizzes()) {
            if (q.getLessonId().equals(lessonID)) {
                return;
            }
        }
        l.addQuiz(quiz);
    }
    
    
}
