/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import backend.*;

/**
 *
 * @author DELL 7550
 */
/*
 * QuizService.java
 */
import java.util.ArrayList;

public class QuizService {

    private final JsonDatabaseManager db;

    public QuizService(JsonDatabaseManager dbManager) {
        this.db = dbManager;
    }

    // --- Instructor: Creating a Quiz ---
    public boolean createQuiz(String lessonID, ArrayList<Question> questions) {
        Lesson lesson = JsonDatabaseManager.findLessonById(lessonID);
        if (lesson == null) {
            return false;
        }

        // Generate a unique ID like "Q1-1-1"
        String quizID = JsonDatabaseManager.generateQuizID(lessonID);

        Quiz newQuiz = new Quiz(quizID);
        for (Question q : questions) {
            newQuiz.addQuestion(q);
        }

        lesson.addQuiz(newQuiz);
        db.saveCourses(); // IMPORTANT: Save changes to courses.json
        return true;
    }

    // --- Student: Taking a Quiz ---
    public int submitQuiz(String studentID, String courseID, String quizID, int[] answers) {
        Student student = (Student) db.findUserById(studentID);
        Quiz quiz = findQuizById(quizID); // Helper method below

        if (student == null || quiz == null) {
            return -1;
        }

        // 1. Calculate Score
        int score = quiz.calculateScore(answers);
        int totalQuestions = quiz.getQuestions().size();

        // 2. Check Pass/Fail (50% threshold)
        double percentage = (double) score / totalQuestions;
        boolean passed = percentage >= 0.5;

        // 3. Save Result if Passed
        if (passed) {
            StudentCourseProgress scp = student.getSCP(courseID);
            if (scp != null) {
                scp.passQuiz(quizID);
                db.saveUsers(); // IMPORTANT: Save changes to users.json
            }
        }

        return score;
    }

    public boolean hasStudentPassedQuiz(String studentID, String courseID, String quizID) {
        Student student = (Student) db.findUserById(studentID);
        if (student != null) {
            StudentCourseProgress scp = student.getSCP(courseID);
            if (scp != null) {
                return scp.isQuizPassed(quizID);
            }
        }
        return false;
    }

    // Helper method to find a quiz object anywhere in the database
    private Quiz findQuizById(String quizID) {
        for (Course c : db.getCourses()) {
            for (Lesson l : c.getLessons()) {
                for (Quiz q : l.getQuizzes()) {
                    if (q.getID().equals(quizID)) {
                        return q;
                    }
                }
            }
        }
        return null;
    }
}
