/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main;

import backend.JsonDatabaseManager;
import controller.*;

public class TestMain {

    public static final JsonDatabaseManager db = new JsonDatabaseManager("users.json", "courses.json");
    public static final UserService uServ = new UserService(db);
    public static final CourseService cServ = new CourseService(db);

    public static void main(String[] args) {
        System.out.println(JsonDatabaseManager.getUsers());
        try {
            uServ.registerStudent("dodo1234", "davidyassa", "dodo4321");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }
}
