package com.project.vestiart.services;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.project.vestiart.dto.IdeaDTO;
import com.project.vestiart.services.interfaces.PdfService;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;

@Service
public class PdfServiceImpl implements PdfService {

    public byte[] generatePdf(IdeaDTO ideaDTO) throws IOException, DocumentException {
        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        PdfWriter.getInstance(document, out);

        document.open();

        Paragraph par = new Paragraph(ideaDTO.description());

        Image urlImage = Image.getInstance(new URL(ideaDTO.imageUrl()));
        urlImage.scaleToFit(200, 200);
        document.add(urlImage);
        document.add(par);

        document.close();

        return out.toByteArray();
    }

}
