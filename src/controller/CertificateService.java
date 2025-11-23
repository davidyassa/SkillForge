/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import backend.Certificate;
import backend.JsonDatabaseManager;
import java.io.File;
import java.io.FileWriter;

/**
 *
 * @author DELL 7550
 */
public class CertificateService {

    private final JsonDatabaseManager db;

    public CertificateService(JsonDatabaseManager db) {
        this.db = db;
    }

    public Certificate getCertificate(String id) {
        return db.findCertificateById(id);
    }

    public void saveAsTextFile(Certificate cert) {
        try {
            File dir = new File("./certificates");
            if (!dir.exists()) {
                dir.mkdirs(); //create folder if it doesn't alr exist 
            }
            String file = "./certificates/" + cert.getID() + ".txt";

            try (FileWriter writer = new FileWriter(file)) {
                writer.write("===== CERTIFICATE =====\n");
                writer.write("Certificate ID: " + cert.getID() + "\n");
                writer.write("Student ID    : " + cert.getStudentId() + "\n");
                writer.write("Course ID     : " + cert.getCourseId() + "\n");
                writer.write("Issued On     : " + cert.getIssueDate() + "\n");
                writer.write("========================\n");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
