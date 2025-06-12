package com.project.vestiart.controllers;

import com.itextpdf.text.DocumentException;
import com.project.vestiart.dto.IdeaDTO;
import com.project.vestiart.dto.PdfGenerationResponse;
import com.project.vestiart.models.BucketInfos;
import com.project.vestiart.models.Idea;
import com.project.vestiart.services.AsyncService;
import com.project.vestiart.services.BucketService;
import com.project.vestiart.services.interfaces.IdeaService;
import com.project.vestiart.services.interfaces.PdfService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
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
    private final AsyncService asyncService;
    private final IdeaService ideaService;

    public PdfController(PdfService pdfService, BucketService bucketService, AsyncService asyncService, IdeaService ideaService) {
        this.pdfService = pdfService;
        this.bucketService = bucketService;
        this.asyncService = asyncService;
        this.ideaService = ideaService;
    }

    @PostMapping("/generate")
    public ResponseEntity<?> generatePdf(@RequestBody List<IdeaDTO> listIdeaDTOs) throws IOException {
        List<CompletableFuture<String>> futures = listIdeaDTOs.stream()
                .map(input -> asyncService.runAsync(() -> {
                    try {
                        BucketInfos bucketInfos;
                        byte[] pdfBytes = pdfService.generatePdf(input);
                        bucketInfos = bucketService.uploadFileFromGeneration(input.tag1(), input.tag2(), pdfBytes, ".pdf");

                        Optional<Idea> idea = ideaService.getIdeaByIdExterne(input.idExterneImage());

                        System.out.println(bucketInfos.getUrl());

                        idea.ifPresent(value -> value.setIdExternePdf(bucketInfos.getIdExterne()));
                        idea.ifPresent(value -> value.setPdf(bucketInfos.getUrl()));

                        ideaService.updateIdea(idea.get());

                        return bucketInfos.getUrl();
                    } catch (DocumentException | IOException e) {
                        throw new RuntimeException(e);
                    }
                }))
                .toList();

        List<String> listPDF = futures.stream()
                .map(CompletableFuture::join)
                .toList();

        return ResponseEntity.ok().body(PdfGenerationResponse.builder().pdfs(listPDF).build());
    }


}


