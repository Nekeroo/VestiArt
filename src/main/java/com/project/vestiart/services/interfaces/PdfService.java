package com.project.vestiart.services.interfaces;

import com.itextpdf.text.DocumentException;
import com.project.vestiart.models.BucketInfos;
import com.project.vestiart.models.dto.IdeaDTO;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;

public interface PdfService {

    byte[] generatePdf(IdeaDTO ideaDTO) throws IOException, DocumentException;

}
