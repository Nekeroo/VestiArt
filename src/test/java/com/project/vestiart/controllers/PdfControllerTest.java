package com.project.vestiart.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itextpdf.text.DocumentException;
import com.project.vestiart.dto.IdeaDTO;
import com.project.vestiart.models.BucketInfos;
import com.project.vestiart.services.AsyncService;
import com.project.vestiart.services.BucketService;
import com.project.vestiart.services.interfaces.PdfService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.zip.ZipInputStream;
import java.io.ByteArrayInputStream;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
import java.util.zip.ZipEntry;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class PdfControllerTest {

    @Mock
    private PdfService pdfService;

    @Mock
    private BucketService bucketService;

    @Mock
    private AsyncService asyncService;

    private PdfController pdfController;
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        pdfController = new PdfController(pdfService, bucketService, asyncService);
        mockMvc = MockMvcBuilders.standaloneSetup(pdfController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void testGeneratePdf_SingleIdea_ReturnsPdfBytes() throws Exception {
        // Arrange
        IdeaDTO ideaDTO = createIdeaDTO("tag1", "tag2");
        List<IdeaDTO> ideaDTOList = Collections.singletonList(ideaDTO);
        byte[] expectedPdfBytes = "PDF content".getBytes();

        when(pdfService.generatePdf(ideaDTO)).thenReturn(expectedPdfBytes);
        when(bucketService.uploadFileFromGeneration(
                eq("tag1"), eq("tag2"), eq(expectedPdfBytes), eq("pdf")))
                .thenReturn(BucketInfos.builder().build()); // Assuming BucketInfos has a default constructor

        // Mock AsyncService to return a CompletableFuture with the expected bytes
        when(asyncService.runAsync(any(Supplier.class))).thenAnswer(invocation -> {
            Supplier<byte[]> supplier = invocation.getArgument(0);
            return CompletableFuture.completedFuture(supplier.get());
        });

        // Act
        ResponseEntity<byte[]> response = pdfController.generatePdf(ideaDTOList);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertArrayEquals(expectedPdfBytes, response.getBody());
        assertEquals(MediaType.APPLICATION_OCTET_STREAM, response.getHeaders().getContentType());
        assertEquals("form-data; name=\"attachment\"; filename=\"document.pdf\"",
                response.getHeaders().getFirst(HttpHeaders.CONTENT_DISPOSITION));

        verify(pdfService, times(1)).generatePdf(ideaDTO);
        verify(bucketService, times(1)).uploadFileFromGeneration("tag1", "tag2", expectedPdfBytes, "pdf");
        verify(asyncService, times(1)).runAsync(any(Supplier.class));
    }

    @Test
    void testGeneratePdf_MultipleIdeas_ReturnsZipFile() throws Exception {
        // Arrange
        IdeaDTO idea1 = createIdeaDTO("tag1_1", "tag2_1");
        IdeaDTO idea2 = createIdeaDTO("tag1_2", "tag2_2");
        List<IdeaDTO> ideaDTOList = Arrays.asList(idea1, idea2);

        byte[] pdf1Bytes = "PDF content 1".getBytes();
        byte[] pdf2Bytes = "PDF content 2".getBytes();

        when(pdfService.generatePdf(idea1)).thenReturn(pdf1Bytes);
        when(pdfService.generatePdf(idea2)).thenReturn(pdf2Bytes);

        when(bucketService.uploadFileFromGeneration(anyString(), anyString(), any(byte[].class), eq("pdf")))
                .thenReturn(BucketInfos.builder().build());

        // Mock AsyncService to return CompletableFutures with the expected bytes
        when(asyncService.runAsync(any(Supplier.class))).thenAnswer(invocation -> {
            Supplier<byte[]> supplier = invocation.getArgument(0);
            return CompletableFuture.completedFuture(supplier.get());
        });

        // Act
        ResponseEntity<byte[]> response = pdfController.generatePdf(ideaDTOList);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(MediaType.APPLICATION_OCTET_STREAM, response.getHeaders().getContentType());
        assertEquals("form-data; name=\"attachment\"; filename=\"documents.zip\"",
                response.getHeaders().getFirst(HttpHeaders.CONTENT_DISPOSITION));

        // Verify ZIP content
        verifyZipContent(response.getBody(), Arrays.asList(pdf1Bytes, pdf2Bytes));

        verify(pdfService, times(1)).generatePdf(idea1);
        verify(pdfService, times(1)).generatePdf(idea2);
        verify(bucketService, times(2)).uploadFileFromGeneration(anyString(), anyString(), any(byte[].class), eq("pdf"));
        verify(asyncService, times(2)).runAsync(any(Supplier.class));
    }

    @Test
    void testGeneratePdf_WithMockMvc_SingleIdea() throws Exception {
        // Arrange
        IdeaDTO ideaDTO = createIdeaDTO("tag1", "tag2");
        List<IdeaDTO> ideaDTOList = Collections.singletonList(ideaDTO);
        byte[] expectedPdfBytes = "PDF content".getBytes();

        when(pdfService.generatePdf(any(IdeaDTO.class))).thenReturn(expectedPdfBytes);
        when(bucketService.uploadFileFromGeneration(anyString(), anyString(), any(byte[].class), eq("pdf")))
                .thenReturn(BucketInfos.builder().build());
        when(asyncService.runAsync(any(Supplier.class))).thenAnswer(invocation -> {
            Supplier<byte[]> supplier = invocation.getArgument(0);
            return CompletableFuture.completedFuture(supplier.get());
        });

        // Act & Assert
        mockMvc.perform(post("/pdf/generate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ideaDTOList)))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE))
                .andExpect(header().string(HttpHeaders.CONTENT_DISPOSITION, "form-data; name=\"attachment\"; filename=\"document.pdf\""))
                .andExpect(content().bytes(expectedPdfBytes));
    }

    @Test
    void testGeneratePdf_WithMockMvc_MultipleIdeas() throws Exception {
        // Arrange
        IdeaDTO idea1 = createIdeaDTO("tag1_1", "tag2_1");
        IdeaDTO idea2 = createIdeaDTO("tag1_2", "tag2_2");
        List<IdeaDTO> ideaDTOList = Arrays.asList(idea1, idea2);

        when(pdfService.generatePdf(any(IdeaDTO.class))).thenReturn("PDF content".getBytes());
        when(bucketService.uploadFileFromGeneration(anyString(), anyString(), any(byte[].class), eq("pdf")))
                .thenReturn(BucketInfos.builder().build());
        when(asyncService.runAsync(any(Supplier.class))).thenAnswer(invocation -> {
            Supplier<byte[]> supplier = invocation.getArgument(0);
            return CompletableFuture.completedFuture(supplier.get());
        });

        // Act & Assert
        mockMvc.perform(post("/pdf/generate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ideaDTOList)))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE))
                .andExpect(header().string(HttpHeaders.CONTENT_DISPOSITION, "form-data; name=\"attachment\"; filename=\"documents.zip\""));
    }

    @Test
    void testGeneratePdf_PdfServiceThrowsDocumentException() throws Exception {
        // Arrange
        IdeaDTO ideaDTO = createIdeaDTO("tag1", "tag2");
        List<IdeaDTO> ideaDTOList = Collections.singletonList(ideaDTO);

        when(pdfService.generatePdf(ideaDTO)).thenThrow(new DocumentException("PDF generation failed"));
        when(asyncService.runAsync(any(Supplier.class))).thenAnswer(invocation -> {
            Supplier<byte[]> supplier = invocation.getArgument(0);
            return CompletableFuture.supplyAsync(supplier);
        });

        // Act & Assert
        assertThrows(RuntimeException.class, () -> pdfController.generatePdf(ideaDTOList));

        verify(pdfService, times(1)).generatePdf(ideaDTO);
        verify(bucketService, never()).uploadFileFromGeneration(anyString(), anyString(), any(byte[].class), anyString());
    }

    @Test
    void testGeneratePdf_PdfServiceThrowsIOException() throws Exception {
        // Arrange
        IdeaDTO ideaDTO = createIdeaDTO("tag1", "tag2");
        List<IdeaDTO> ideaDTOList = Collections.singletonList(ideaDTO);

        when(pdfService.generatePdf(ideaDTO)).thenThrow(new IOException("IO error"));
        when(asyncService.runAsync(any(Supplier.class))).thenAnswer(invocation -> {
            Supplier<byte[]> supplier = invocation.getArgument(0);
            return CompletableFuture.supplyAsync(supplier);
        });

        // Act & Assert
        assertThrows(RuntimeException.class, () -> pdfController.generatePdf(ideaDTOList));

        verify(pdfService, times(1)).generatePdf(ideaDTO);
        verify(bucketService, never()).uploadFileFromGeneration(anyString(), anyString(), any(byte[].class), anyString());
    }

    @Test
    void testGeneratePdf_BucketServiceThrowsException() throws Exception {
        // Arrange
        IdeaDTO ideaDTO = createIdeaDTO("tag1", "tag2");
        List<IdeaDTO> ideaDTOList = Collections.singletonList(ideaDTO);
        byte[] pdfBytes = "PDF content".getBytes();

        when(pdfService.generatePdf(ideaDTO)).thenReturn(pdfBytes);
        when(bucketService.uploadFileFromGeneration(anyString(), anyString(), any(byte[].class), eq("pdf")))
                .thenThrow(new RuntimeException("Bucket upload failed"));
        when(asyncService.runAsync(any(Supplier.class))).thenAnswer(invocation -> {
            Supplier<byte[]> supplier = invocation.getArgument(0);
            return CompletableFuture.supplyAsync(supplier);
        });

        // Act & Assert
        assertThrows(RuntimeException.class, () -> pdfController.generatePdf(ideaDTOList));

        verify(pdfService, times(1)).generatePdf(ideaDTO);
        verify(bucketService, times(1)).uploadFileFromGeneration("tag1", "tag2", pdfBytes, "pdf");
    }

    @Test
    void testGeneratePdf_EmptyList_ThrowsException() throws Exception {
        // Arrange
        List<IdeaDTO> emptyList = Collections.emptyList();

        // Act & Assert
        // The controller will fail when trying to call getFirst() on an empty list
        assertThrows(Exception.class, () -> pdfController.generatePdf(emptyList));

        verify(pdfService, never()).generatePdf(any(IdeaDTO.class));
        verify(bucketService, never()).uploadFileFromGeneration(anyString(), anyString(), any(byte[].class), anyString());
        verify(asyncService, never()).runAsync(any(Supplier.class));
    }

    @Test
    void testGeneratePdf_LargeNumberOfIdeas() throws Exception {
        // Arrange - Test with more than 10 ideas to verify thread pool sizing
        List<IdeaDTO> largeList = Collections.nCopies(15, createIdeaDTO("tag1", "tag2"));
        byte[] pdfBytes = "PDF content".getBytes();

        when(pdfService.generatePdf(any(IdeaDTO.class))).thenReturn(pdfBytes);
        when(bucketService.uploadFileFromGeneration(anyString(), anyString(), any(byte[].class), eq("pdf")))
                .thenReturn(BucketInfos.builder().build());
        when(asyncService.runAsync(any(Supplier.class))).thenAnswer(invocation -> {
            Supplier<byte[]> supplier = invocation.getArgument(0);
            return CompletableFuture.completedFuture(supplier.get());
        });

        // Act
        ResponseEntity<byte[]> response = pdfController.generatePdf(largeList);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("form-data; name=\"attachment\"; filename=\"documents.zip\"",
                response.getHeaders().getFirst(HttpHeaders.CONTENT_DISPOSITION));

        verify(pdfService, times(15)).generatePdf(any(IdeaDTO.class));
        verify(bucketService, times(15)).uploadFileFromGeneration(anyString(), anyString(), any(byte[].class), eq("pdf"));
    }

    private IdeaDTO createIdeaDTO(String tag1, String tag2) {
        // Assuming IdeaDTO is a record with tag1 and tag2 parameters
        // Adjust this method based on your actual IdeaDTO structure
        return IdeaDTO.builder().tag1(tag1).tag2(tag2).build();
    }

    private void verifyZipContent(byte[] zipBytes, List<byte[]> expectedPdfContents) throws IOException {
        try (ZipInputStream zis = new ZipInputStream(new ByteArrayInputStream(zipBytes))) {
            int entryCount = 0;
            ZipEntry entry;

            while ((entry = zis.getNextEntry()) != null) {
                assertTrue(entry.getName().startsWith("document_"));
                assertTrue(entry.getName().endsWith(".pdf"));

                byte[] entryContent = zis.readAllBytes();
                assertTrue(expectedPdfContents.contains(entryContent) ||
                        Arrays.equals(entryContent, expectedPdfContents.get(entryCount)));

                entryCount++;
                zis.closeEntry();
            }

            assertEquals(expectedPdfContents.size(), entryCount);
        }
    }
}