package com.project.vestiart.services;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.project.vestiart.models.dto.IdeaDTO;
import com.project.vestiart.services.interfaces.PdfService;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

@Service
public class PdfServiceImpl implements PdfService {

    public String generatePdf(IdeaDTO ideaDTO) throws FileNotFoundException, DocumentException {
        Document document = new Document();

        PdfWriter.getInstance(document, new FileOutputStream(ideaDTO.title() + ".pdf"));

        document.open();

        Paragraph par = new Paragraph(ideaDTO.description());

        document.add(par);

        document.close();

        return "Fichier Crée";

    }

}
