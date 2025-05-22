package com.project.vestiart.controllers;

import com.itextpdf.text.DocumentException;
import com.project.vestiart.models.BucketInfos;
import com.project.vestiart.models.dto.IdeaDTO;
import com.project.vestiart.services.PdfServiceImpl;
import com.project.vestiart.services.interfaces.PdfService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/pdf")
public class PdfController {

    private final PdfService pdfService;

    public PdfController(PdfService pdfService) {
        this.pdfService = pdfService;
    }

    @PostMapping("/generate")
    public ResponseEntity<byte[]> generatePdf(@RequestBody IdeaDTO ideaDTO) throws DocumentException, IOException {
        byte[] pdfBytes = pdfService.generatePdf(ideaDTO);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", ideaDTO.title() + ".pdf");

        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    }


}


