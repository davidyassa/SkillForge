/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import backend.Certificate;
import backend.JsonDatabaseManager;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import java.io.File;
import java.io.FileWriter;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.layout.*;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.property.TextAlignment;

public class CertificateService {

    private final JsonDatabaseManager db;

    public CertificateService(JsonDatabaseManager db) {
        this.db = db;
    }

    public Certificate getCertificate(String id) {
        return db.findCertificateById(id);
    }

    public String certificateText(Certificate cert) {
        return cert.getTextBlock();
    }

    public void saveAsTextFile(Certificate cert) {
        try {
            File dir = new File("./certificates");
            if (!dir.exists()) {
                dir.mkdirs(); //create folder if it doesn't alr exist 
            }
            String file = "./certificates/" + cert.getID() + ".txt";

            try (FileWriter writer = new FileWriter(file)) {
                writer.write(certificateText(cert));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveAsPDF(Certificate cert) {
        try {
            File dir = new File("./certificates");
            if (!dir.exists()) {
                dir.mkdirs();
            }

            String path = "./certificates/" + cert.getID() + ".pdf";

            PdfWriter writer = new PdfWriter(path);
            PdfDocument pdf = new PdfDocument(writer);
            PageSize landscape = PageSize.A4.rotate(); // landscape Certificate
            try (Document doc = new Document(pdf, landscape)) {
                PdfFont font = PdfFontFactory.createFont(StandardFonts.TIMES_ROMAN);
                TextAlignment center = TextAlignment.CENTER;
                

                // ---- 1. ADD LOGO ----
                String imgPath = "src/resources/certLogo.png";
                ImageData imgData = ImageDataFactory.create(imgPath);
                Image certLogo = new Image(imgData);

                certLogo.setFixedPosition(25, landscape.getHeight() - 115); // Logo position
                certLogo.scaleToFit(100, 100);
                doc.add(certLogo);

                Paragraph title = new Paragraph("CERTIFICATE OF COMPLETION")
                        .setFont(font)
                        .setFontSize(26)
                        .setTextAlignment(center)
                        .setMarginTop(80);

                doc.add(title);

                String[] lines = cert.getTextBlock().split("\n");

                Paragraph block = new Paragraph().setTextAlignment(center); // center the whole block

                for (int i = 0; i < lines.length; i++) {
                    if (i == 0 || i == lines.length - 1) {
                        continue;
                    }
                    block.add(lines[i] + "\n").setMultipliedLeading(2); // left-align the fields and space them out a lil
                }

                block.setFont(font).setFontSize(16).setMarginTop(40);
                doc.add(block);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
