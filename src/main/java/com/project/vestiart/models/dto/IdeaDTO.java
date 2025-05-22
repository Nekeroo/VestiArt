package com.project.vestiart.models.dto;

import com.itextpdf.text.pdf.qrcode.ByteArray;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Builder
public record IdeaDTO(String title, String description, String image) { }
