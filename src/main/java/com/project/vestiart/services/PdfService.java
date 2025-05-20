package com.project.vestiart.services;

import com.itextpdf.text.DocumentException;

import java.io.FileNotFoundException;

public interface PdfService {

    String generatePdf() throws FileNotFoundException, DocumentException;

}
