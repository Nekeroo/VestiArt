package com.project.vestiart.models.dto;

import com.itextpdf.text.pdf.qrcode.ByteArray;
import lombok.*;

import java.awt.*;
import java.awt.image.BufferedImage;

@Builder
public record IdeaDTO(String title, String description, byte[] image) { }
