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

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        JsonDatabaseManager db = new JsonDatabaseManager("users.json", "courses.json");
        CourseService cServ = new CourseService(db);
        UserService uServ = new UserService(db);

    }

}
