package com.project.vestiart.services;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

@Service
public class PdfServiceImpl implements PdfService {

    public String generatePdf() throws FileNotFoundException, DocumentException {
        Document document = new Document();

        PdfWriter.getInstance(document, new FileOutputStream("test.pdf"));

        document.open();

        Paragraph par = new Paragraph("Hello World");

        document.add(par);

        document.close();

        return "Fichier Crée";

    }

}
