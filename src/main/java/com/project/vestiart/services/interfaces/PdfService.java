package com.project.vestiart.services.interfaces;

import com.itextpdf.text.DocumentException;
import com.project.vestiart.models.dto.IdeaDTO;

import java.io.FileNotFoundException;

public interface PdfService {

    String generatePdf(IdeaDTO ideaDTO) throws FileNotFoundException, DocumentException;

}
