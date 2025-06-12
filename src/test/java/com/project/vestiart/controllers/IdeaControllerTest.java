package com.project.vestiart.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.vestiart.dto.IdeaDTO;
import com.project.vestiart.models.Idea;
import com.project.vestiart.services.BucketService;
import com.project.vestiart.services.interfaces.IdeaService;
import com.project.vestiart.utils.mappers.IdeaMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class IdeaControllerTest {

    @Mock
    private IdeaMapper ideaMapper;

    @Mock
    private BucketService bucketService;

    @Mock
    private IdeaService ideaService;

    @InjectMocks
    private IdeaController ideaController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    private Idea testIdea;
    private IdeaDTO testIdeaDTO;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(ideaController).build();
        objectMapper = new ObjectMapper();

        // Setup test data
        testIdea = new Idea();
        testIdea.setIdExterne("test-uid-123");
        // Add other properties as needed based on your Idea model

        testIdeaDTO = IdeaDTO.builder()
                .idExterne("test-uid-123").build();
        // Add other properties as needed based on your IdeaDTO
    }

    @Test
    void retrieveAllIdea_ShouldReturnListOfIdeaDTOs() throws Exception {
        // Given
        List<Idea> ideaList = Arrays.asList(testIdea);
        List<IdeaDTO> expectedDTOList = Arrays.asList(testIdeaDTO);

        when(ideaService.retrieveAll()).thenReturn(ideaList);
        when(ideaMapper.mapIdeaToIdeaDTO(testIdea)).thenReturn(testIdeaDTO);

        // When & Then
        mockMvc.perform(get("/idea/retrieve/all"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1));

        verify(ideaService).retrieveAll();
        verify(ideaMapper).mapIdeaToIdeaDTO(testIdea);
    }

    @Test
    void retrieveAllIdea_WhenEmpty_ShouldReturnEmptyList() throws Exception {
        // Given
        when(ideaService.retrieveAll()).thenReturn(Arrays.asList());

        // When & Then
        mockMvc.perform(get("/idea/retrieve/all"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));

        verify(ideaService).retrieveAll();
        verifyNoInteractions(ideaMapper);
    }

    @Test
    void retrieveIdeaByUid_WhenIdeaExists_ShouldReturnIdeaDTO() throws Exception {
        // Given
        String uid = "test-uid-123";
        when(ideaService.getIdeaByIdExterne(uid)).thenReturn(Optional.of(testIdea));
        when(ideaMapper.mapIdeaToIdeaDTO(testIdea)).thenReturn(testIdeaDTO);

        // When & Then
        mockMvc.perform(get("/idea/retrieve/{uid}", uid))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(ideaService).getIdeaByIdExterne(uid);
        verify(ideaMapper).mapIdeaToIdeaDTO(testIdea);
    }

    @Test
    void retrieveIdeaByUid_WhenIdeaNotExists_ShouldReturnNull() throws Exception {
        // Given
        String uid = "non-existent-uid";
        when(ideaService.getIdeaByIdExterne(uid)).thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(get("/idea/retrieve/{uid}", uid))
                .andExpect(status().isOk());

        verify(ideaService).getIdeaByIdExterne(uid);
        verifyNoInteractions(ideaMapper);
    }

    @Test
    void addIdea_ShouldSaveIdeaAndReturnDTO() throws Exception {
        // Given
        when(ideaMapper.mapIdeaDTOToIdea(any(IdeaDTO.class))).thenReturn(testIdea);
        doNothing().when(ideaService).saveIdea(testIdea);

        // When & Then
        mockMvc.perform(post("/idea/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testIdeaDTO)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(ideaMapper).mapIdeaDTOToIdea(any(IdeaDTO.class));
        verify(ideaService).saveIdea(testIdea);
    }

    @Test
    void removeIdea_WhenIdeaExists_ShouldDeleteIdeaAndBucketDocument() throws Exception {
        // Given
        String uid = "test-uid-123";
        when(ideaService.getIdeaByIdExterne(uid)).thenReturn(Optional.of(testIdea));
        doNothing().when(ideaService).removeIdea(testIdea);
        doNothing().when(bucketService).deleteDocumentFromTheBucket(uid);

        // When & Then
        mockMvc.perform(delete("/idea/delete/{uid}", uid))
                .andExpect(status().isOk());

        verify(ideaService).getIdeaByIdExterne(uid);
        verify(ideaService).removeIdea(testIdea);
        verify(bucketService).deleteDocumentFromTheBucket(uid);
    }

    @Test
    void removeIdea_WhenIdeaNotExists_ShouldThrowRuntimeException() {
        // Given
        String uid = "non-existent-uid";
        when(ideaService.getIdeaByIdExterne(uid)).thenReturn(Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            ideaController.remove(uid);
        });

        assertEquals("Idea not found", exception.getMessage());
        verify(ideaService).getIdeaByIdExterne(uid);
        verifyNoMoreInteractions(ideaService);
        verifyNoInteractions(bucketService);
    }

    @Test
    void retrieveLastIdea_ShouldReturnSpecifiedNumberOfIdeas() throws Exception {
        // Given
        int numberOfElements = 5;
        List<Idea> ideaList = Arrays.asList(testIdea);
        when(ideaService.retrieveLastIdea(numberOfElements)).thenReturn(ideaList);
        when(ideaMapper.mapIdeaToIdeaDTO(testIdea)).thenReturn(testIdeaDTO);

        // When & Then
        mockMvc.perform(get("/idea/retrieve/last/{numberOfElements}", numberOfElements))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1));

        verify(ideaService).retrieveLastIdea(numberOfElements);
        verify(ideaMapper).mapIdeaToIdeaDTO(testIdea);
    }

    @Test
    void retrievePaginatedIdea_ShouldReturnPaginatedResults() throws Exception {
        // Given
        int page = 0;
        int size = 10;
        List<Idea> ideaList = Arrays.asList(testIdea);
        when(ideaService.retrievePaginatedIdea(page, size)).thenReturn(ideaList);
        when(ideaMapper.mapIdeaToIdeaDTO(testIdea)).thenReturn(testIdeaDTO);

        // When & Then
        mockMvc.perform(get("/idea/retrieve/{page}/{size}", page, size))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1));

        verify(ideaService).retrievePaginatedIdea(page, size);
        verify(ideaMapper).mapIdeaToIdeaDTO(testIdea);
    }

    @Test
    void retrievePaginatedIdea_WhenEmpty_ShouldReturnEmptyList() throws Exception {
        // Given
        int page = 0;
        int size = 10;
        when(ideaService.retrievePaginatedIdea(page, size)).thenReturn(Arrays.asList());

        // When & Then
        mockMvc.perform(get("/idea/retrieve/{page}/{size}", page, size))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));

        verify(ideaService).retrievePaginatedIdea(page, size);
        verifyNoInteractions(ideaMapper);
    }

    // Unit tests without MockMvc
    @Test
    void retrieveAllIdea_DirectCall_ShouldReturnMappedDTOs() {
        // Given
        List<Idea> ideaList = Arrays.asList(testIdea);
        when(ideaService.retrieveAll()).thenReturn(ideaList);
        when(ideaMapper.mapIdeaToIdeaDTO(testIdea)).thenReturn(testIdeaDTO);

        // When
        List<IdeaDTO> result = ideaController.retrieveAllIdea();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testIdeaDTO, result.get(0));
        verify(ideaService).retrieveAll();
        verify(ideaMapper).mapIdeaToIdeaDTO(testIdea);
    }

    @Test
    void retrieveIdeaByUid_DirectCall_WhenFound_ShouldReturnDTO() {
        // Given
        String uid = "test-uid-123";
        when(ideaService.getIdeaByIdExterne(uid)).thenReturn(Optional.of(testIdea));
        when(ideaMapper.mapIdeaToIdeaDTO(testIdea)).thenReturn(testIdeaDTO);

        // When
        ResponseEntity<?> result = ideaController.retrieIdeaByUid(uid);

        // Then
        assertNotNull(result);
        assertTrue(result.hasBody());
    }

    @Test
    void retrieveIdeaByUid_DirectCall_WhenNotFound_ShouldReturnNull() {
        // Given
        String uid = "non-existent-uid";
        when(ideaService.getIdeaByIdExterne(uid)).thenReturn(Optional.empty());

        // When
        ResponseEntity<?> result = ideaController.retrieIdeaByUid(uid);

        // Then
        assertFalse(result.hasBody());
    }

    @Test
    void removeIdea_DirectCall_WhenNotFound_ShouldThrowException() {
        // Given
        String uid = "non-existent-uid";
        when(ideaService.getIdeaByIdExterne(uid)).thenReturn(Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> ideaController.remove(uid));

        assertEquals("Idea not found", exception.getMessage());
        verify(ideaService).getIdeaByIdExterne(uid);
        verifyNoMoreInteractions(ideaService);
        verifyNoInteractions(bucketService);
    }
}