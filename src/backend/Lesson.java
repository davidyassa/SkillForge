/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package backend;

public class Lesson {

    private final String lessonID;
    private String title;
    private String content;
    private final String[] resources;

    public Lesson(String lessonId, String title, String content) {
        this.lessonID = lessonId;
        this.title = title;
        this.content = content;
        this.resources = null;
    }

    public Lesson(String lessonId, String title, String content, String[] resources) {
        this.lessonID = lessonId;
        this.title = title;
        this.content = content;
        this.resources = resources.clone();
    }

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

    @Override
    public String toString() {
        return title;
    }
}
