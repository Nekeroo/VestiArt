package com.project.vestiart.controllers;

import com.project.vestiart.models.dto.IdeaDTO;
import com.project.vestiart.services.PdfServiceImpl;
import com.project.vestiart.services.interfaces.PdfService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("/pdf")
public class PdfController {

    private final PdfService pdfService;

    public PdfController(PdfService pdfService) {
        this.pdfService = pdfService;
    }

    @PostMapping("/generate")
    public byte[] generatePdf(@RequestBody IdeaDTO ideaDTO) {
        // TODO : Créer la méthode PDF
        return null;
    }

}


