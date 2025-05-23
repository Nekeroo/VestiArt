package com.project.vestiart.controllers;

import com.itextpdf.text.DocumentException;
import com.project.vestiart.models.BucketInfos;
import com.project.vestiart.models.dto.IdeaDTO;
import com.project.vestiart.services.BucketService;
import com.project.vestiart.services.PdfServiceImpl;
import com.project.vestiart.services.interfaces.PdfService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@RestController
@RequestMapping("/pdf")
public class PdfController {

    private final PdfService pdfService;
    private final BucketService bucketService;

    public PdfController(PdfService pdfService, BucketService bucketService) {
        this.pdfService = pdfService;
        this.bucketService = bucketService;
    }

    @PostMapping("/generate")
    public ResponseEntity<byte[]> generatePdf(@RequestBody List<IdeaDTO> listIdeaDTOs) throws IOException {
        ExecutorService executorService = Executors.newFixedThreadPool(Math.min(listIdeaDTOs.size(), 10));

        List<CompletableFuture<byte[]>> futures = listIdeaDTOs.stream()
                .map(input -> CompletableFuture.supplyAsync(() -> {
                    try {
                        byte[] pdfBytes = pdfService.generatePdf(input);
                        bucketService.uploadFileFromGeneration(input.tag1(), input.tag2(), pdfBytes, "pdf");
                        return pdfBytes;
                    } catch (DocumentException | IOException e) {
                        throw new RuntimeException(e);
                    }
                }, executorService))
                .toList();

        List<byte[]> listPDF = futures.stream()
                .map(CompletableFuture::join)
                .toList();

        executorService.shutdown();

        byte[] zipBytes = null;

        if (listPDF.size() > 1) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try (ZipOutputStream zos = new ZipOutputStream(baos)) {
                for (int i = 0; i < listPDF.size(); i++) {
                    byte[] pdf = listPDF.get(i);
                    ZipEntry entry = new ZipEntry("document_" + (i + 1) + ".pdf");
                    zos.putNextEntry(entry);
                    zos.write(pdf);
                    zos.closeEntry();
                }
            }

            zipBytes = baos.toByteArray();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", zipBytes != null ? "documents.zip" : "document.pdf");

        return new ResponseEntity<>(zipBytes != null ? zipBytes : listPDF.getFirst(), headers, HttpStatus.OK);
    }



}


