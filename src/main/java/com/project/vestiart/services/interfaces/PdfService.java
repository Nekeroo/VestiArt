package com.project.vestiart.services.interfaces;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfDocument;
import com.project.vestiart.dto.IdeaDTO;
import com.project.vestiart.models.Idea;
import com.project.vestiart.models.PdfInfos;

import java.io.IOException;

public interface PdfService {

    PdfInfos generatePdf(Idea idea) throws IOException, DocumentException;

    byte[] buildPDFFromIdea(Idea idea) throws IOException, DocumentException;

}
