package com.example.maschinefactory.impl;

import com.example.maschinefactory.subassembly.SubassemblyControllerImpl;
import com.example.maschinefactory.subassembly.SubassemblyEntity;
import com.example.maschinefactory.subassembly.SubassemblyService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@WebMvcTest(SubassemblyControllerImpl.class)
class SubassemblyControllerImplTest {

    @Mock
    private SubassemblyService subassemblyService;

    @InjectMocks
    private SubassemblyControllerImpl subassemblyController;

    private final MockMvc mockMvc;

    private final ObjectMapper objectMapper;

    public SubassemblyControllerImplTest(MockMvc mockMvc, ObjectMapper objectMapper) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
    }

    @Test
    void testCreateSubassembly() throws Exception {
        SubassemblyEntity subassemblyEntity = new SubassemblyEntity();
        subassemblyEntity.setSubassemblyName("Subassembly1");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1.0/subassemblies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(subassemblyEntity)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Subassembly created successfully"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true));

        verify(subassemblyService, times(1)).createSubassembly(subassemblyEntity);
    }

    @Test
    void testUpdateSubassembly() throws Exception {
        Long subassemblyId = 1L;
        SubassemblyEntity updatedSubassemblyEntity = new SubassemblyEntity();
        updatedSubassemblyEntity.setSubassemblyName("Subassembly2");

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1.0/subassemblies/{id}", subassemblyId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedSubassemblyEntity)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Subassembly updated successfully"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true));

        verify(subassemblyService, times(1)).updateSubassembly(anyLong(), any(SubassemblyEntity.class));
    }

    @Test
    void testGetAllSubassemblies() throws Exception {
        int page = 0;
        int size = 10;
        List<SubassemblyEntity> subassemblies = Arrays.asList(new SubassemblyEntity(), new SubassemblyEntity());

        when(subassemblyService.getAllSubassemblies(page, size)).thenReturn(subassemblies);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1.0/subassemblies")
                        .param("page", String.valueOf(page))
                        .param("size", String.valueOf(size)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(subassemblies.size()));
    }

    @Test
    void testGetSubassemblyById() throws Exception {
        Long subassemblyId = 1L;
        SubassemblyEntity subassemblyEntity = new SubassemblyEntity();
        subassemblyEntity.setId(subassemblyId);
        subassemblyEntity.setSubassemblyName("Subassembly3");

        when(subassemblyService.getSubassemblyById(subassemblyId)).thenReturn(subassemblyEntity);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1.0/subassemblies/{id}", subassemblyId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(subassemblyEntity.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.subassemblyName").value(subassemblyEntity.getSubassemblyName()));
    }

    @Test
    void testDeleteSubassemblyById() throws Exception {
        Long subassemblyId = 1L;

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1.0/subassemblies/{id}", subassemblyId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Subassembly deleted successfully"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true));

        verify(subassemblyService, times(1)).deleteSubassemblyById(subassemblyId);
    }
}