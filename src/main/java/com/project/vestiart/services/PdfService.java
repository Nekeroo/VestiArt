package com.project.vestiart.services;

import com.itextpdf.text.DocumentException;

import java.io.FileNotFoundException;

public interface PdfService {

    String generatePdf(String textGenerated, String pdfTitle) throws FileNotFoundException, DocumentException;

}
