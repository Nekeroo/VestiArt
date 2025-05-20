package com.project.vestiart.controllers;

import com.itextpdf.text.DocumentException;
import com.project.vestiart.models.input.RequestInput;
import com.project.vestiart.services.interfaces.OpenAIService;
import com.project.vestiart.services.interfaces.PdfService;
import com.project.vestiart.utils.PromptUtils;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.image.ImageResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.ai.chat.model.Generation;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@RestController
@RequestMapping("/openapi/ai")
public class OpenAIController {

    private final String imageFilesPath = "output/image/" ;
    private final OpenAIService openAIService;
    private final PdfService pdfService;

    public OpenAIController(OpenAIService openAIService, PdfService pdfService) {
        this.openAIService = openAIService;
        this.pdfService = pdfService;
    }

    @GetMapping("/message")
    public String getChatResponse(@RequestBody RequestInput requestInput) throws DocumentException, FileNotFoundException {

        String prompt = PromptUtils.formatPromptRequest(requestInput.getPerson(), requestInput.getReference(), requestInput.getType());

        ChatResponse response = openAIService.createMessage(prompt);

        Generation generation = response.getResult() ;
        return generation.getOutput().getText();
    }

    // TODO : Refacto méthode getImage
    // Objectif : Recevoir une chaîne de byte[] pour l'envoyer au front / intégrer au PDF

    @GetMapping("/image")
    public String getImage(@RequestParam String prompt) throws IOException {
        /*ImageResponse response = openAIService.getImage(prompt);

        // Génère un nom de fichier simplifié
        String fileName = prompt
                .substring(0, Math.min(prompt.length(), 20))
                .toLowerCase()
                .replace(",", "")
                .replace("'", "")
                .replace(" ", "_") + ".png";

        // Chemin complet de sauvegarde
        String filePath = imageFilesPath + File.separator + fileName;

        // Récupération de l'URL de l'image depuis la réponse
        String imageUrl = response.getResult().getOutput().getUrl(); // à adapter selon la structure réelle

        // Téléchargement et enregistrement de l'image
        try (InputStream in = new URL(imageUrl).openStream()) {
            Files.copy(in, Paths.get(filePath), StandardCopyOption.REPLACE_EXISTING);
        }

        return "Image saved with name : " + filePath;*/
        return "";
    }

}
