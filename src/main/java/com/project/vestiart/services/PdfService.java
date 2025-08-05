package com.project.vestiart.services;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;
import com.project.vestiart.models.Idea;
import com.project.vestiart.models.PdfInfos;
import com.project.vestiart.services.interfaces.IPdfService;
import org.springframework.stereotype.Service;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class PdfService implements IPdfService {

    public PdfInfos generatePdf(Idea idea) throws IOException {
        try {
            byte[] pdfBytes = this.buildPDFFromIdea(idea);

            return PdfInfos.builder()
                    .pdf(pdfBytes)
                    .build();

        } catch (DocumentException | IOException e) {
            throw new RuntimeException(e);
        }
    }


    public byte[] buildPDFFromIdea(Idea idea) throws IOException, DocumentException {
        Document document = new Document(PageSize.A4, 50, 50, 50, 50);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        PdfWriter writer = PdfWriter.getInstance(document, out);
        writer.setPageEvent(new HeaderFooterPageEvent());

        document.open();

        // Title
        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, BaseColor.BLACK);
        Paragraph title = new Paragraph(idea.getTag1() + " Collection", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(20);
        document.add(title);

        // Metadata
        Font metaFont = FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.BLACK);
        Paragraph metadata = new Paragraph(
                "Character: " + idea.getTag1() + "\n" +
                        "Reference: " + idea.getTag2() + "\n" +
                        "Source Type: " + idea.getType(),
                metaFont
        );
        metadata.setSpacingAfter(15);
        document.add(metadata);

        // Image
        if (idea.getImage() != null && (idea.getImage().getData().length != 0)) {
            try {
                Image urlImage = Image.getInstance(idea.getImage().getData());
                urlImage.scaleToFit(300, 300);
                urlImage.setAlignment(Element.ALIGN_CENTER);
                document.add(urlImage);
                document.add(Chunk.NEWLINE);
            } catch (Exception e) {
                Paragraph error = new Paragraph("Image could not be loaded.",
                        FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 10, BaseColor.RED));
                error.setAlignment(Element.ALIGN_CENTER);
                document.add(error);
                document.add(Chunk.NEWLINE);
            }
        }

        // Description
        Font bodyFont = FontFactory.getFont(FontFactory.HELVETICA, 11, BaseColor.DARK_GRAY);
        Paragraph description = new Paragraph(idea.getDescription(), bodyFont);
        description.setAlignment(Element.ALIGN_JUSTIFIED);
        description.setSpacingBefore(10);
        document.add(description);

        document.close();
        return out.toByteArray();
    }

    // Helper class for header/footer
    private static class HeaderFooterPageEvent extends PdfPageEventHelper {
        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
        Font footerFont = FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 10);

        @Override
        public void onEndPage(PdfWriter writer, Document document) {
            PdfPTable header = new PdfPTable(1);
            PdfPTable footer = new PdfPTable(1);

            try {
                header.setWidths(new int[]{1});
                header.setTotalWidth(527);
                header.setLockedWidth(true);
                header.getDefaultCell().setBorder(Rectangle.NO_BORDER);
                header.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
                header.addCell(new Phrase("VestIArt", headerFont));
                header.writeSelectedRows(0, -1, 34, 820, writer.getDirectContent());

                footer.setWidths(new int[]{1});
                footer.setTotalWidth(527);
                footer.setLockedWidth(true);
                footer.getDefaultCell().setBorder(Rectangle.NO_BORDER);
                footer.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
                String dateStr = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
                footer.addCell(new Phrase(dateStr, footerFont));
                footer.writeSelectedRows(0, -1, 34, 30, writer.getDirectContent());

            } catch (DocumentException de) {
                throw new ExceptionConverter(de);
            }
        }
    }



}
