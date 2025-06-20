package com.project.vestiart.services.interfaces;

import com.itextpdf.text.DocumentException;
import com.project.vestiart.dto.IdeaDTO;
import com.project.vestiart.models.Idea;

import java.io.IOException;

public interface PdfService {

    byte[] generatePdf(Idea idea) throws IOException, DocumentException;

}
