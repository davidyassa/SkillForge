/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package main;

import backend.JsonDatabaseManager;
import controller.*;

/**
 *
 * @author DELL 7550
 */
public class Main {

    public static final JsonDatabaseManager db = new JsonDatabaseManager("users.json", "courses.json");
    public static final UserService uServ = new UserService(db);
    public static final CourseService cServ = new CourseService(db);

    public static void main(String[] args) {
        System.out.println(db.getUsers());
        try {
            uServ.registerStudent("abdo", "abdo", "abdo");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

}
