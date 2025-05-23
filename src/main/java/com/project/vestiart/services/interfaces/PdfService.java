package com.project.vestiart.services.interfaces;

import com.itextpdf.text.DocumentException;
import com.project.vestiart.dto.IdeaDTO;

import java.io.IOException;

public interface PdfService {

    byte[] generatePdf(IdeaDTO ideaDTO) throws IOException, DocumentException;

}
