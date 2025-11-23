/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package backend;

import java.util.ArrayList;

public class Lesson implements Searchable {

    private final String lessonID;
    private String title;
    private String content;
    private ArrayList<Quiz> quizzes;
    private final String[] resources;

    public Lesson(String lessonId, String title, String content) {
        this.lessonID = lessonId;
        this.title = title;
        this.content = content;
        this.quizzes = new ArrayList<>();
        this.resources = null;
    }

    public Lesson(String lessonId, String title, String content, String[] resources) {
        this.lessonID = lessonId;
        this.title = title;
        this.content = content;
        this.resources = resources.clone();
    }

    @Override
    public String getID() {
        return lessonID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String[] getResources() {
        return resources;
    }

    public ArrayList<Quiz> getQuizzes() {
        return quizzes;
    }

    public void addQuiz(Quiz quiz) {
        quizzes.add(quiz);
    }

    public void removeQuiz(String quizID) {
        quizzes.removeIf(q -> q.getID().equals(quizID));
    }

    public int getLessonUniqueID() {
        String[] parts = lessonID.split("-"); //L2-1
        return Integer.parseInt(parts[1]);
    }

    @Override
    public String toString() {
        return title;
    }
}
