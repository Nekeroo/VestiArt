package com.project.vestiart.controllers;

import com.itextpdf.text.DocumentException;
import com.project.vestiart.services.PdfService;
import com.project.vestiart.services.PdfServiceImpl;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class PdfController {

    private final PdfService pdfService;

    public PdfController(PdfServiceImpl pdfServiceImpl) {
        this.pdfService = pdfServiceImpl;
    }

    @GetMapping("/pdf")
    public String createSamplePDF() throws IOException, DocumentException {
            return pdfService.generatePdf();
    }

}
