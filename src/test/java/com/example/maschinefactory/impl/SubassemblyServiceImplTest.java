package com.example.maschinefactory.impl;

import com.example.maschinefactory.exceptions.ResourceNotFoundException;
import com.example.maschinefactory.subassembly.SubassemblyEntity;
import com.example.maschinefactory.subassembly.SubassemblyRepository;
import com.example.maschinefactory.subassembly.SubassemblyService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class SubassemblyServiceImplTest {

    @Autowired
    private SubassemblyService subassemblyService;

    @MockBean
    private SubassemblyRepository subassemblyRepository;

    @Test
    public void testCreateSubassembly() {
        SubassemblyEntity subassemblyEntity = new SubassemblyEntity();
        subassemblyService.createSubassembly(subassemblyEntity);
        verify(subassemblyRepository, times(1)).save(subassemblyEntity);
    }

    @Test
    public void testGetSubassemblyById_ExistingId() {
        Long id = 1L;
        SubassemblyEntity subassemblyEntity = new SubassemblyEntity();
        when(subassemblyRepository.findById(id)).thenReturn(Optional.of(subassemblyEntity));
        SubassemblyEntity result = subassemblyService.getSubassemblyById(id);
        assertNotNull(result);
        assertEquals(subassemblyEntity, result);
    }

    @Test
    public void testGetSubassemblyById_NonexistentId() {
        Long id = 1L;
        when(subassemblyRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> subassemblyService.getSubassemblyById(id));
    }

    @Test
    public void testGetAllSubassemblies() {
        int page = 0;
        int size = 10;
        List<SubassemblyEntity> expectedSubassemblies = Arrays.asList(new SubassemblyEntity(), new SubassemblyEntity());
        PageImpl<SubassemblyEntity> pageResult = new PageImpl<>(expectedSubassemblies);
        when(subassemblyRepository.findAll(any(PageRequest.class))).thenReturn(pageResult);
        List<SubassemblyEntity> result = subassemblyService.getAllSubassemblies(page, size);
        assertNotNull(result);
        assertEquals(expectedSubassemblies.size(), result.size());
    }

    @Test
    public void testUpdateSubassembly_ExistingId() {
        Long id = 1L;
        SubassemblyEntity existingSubassembly = new SubassemblyEntity();
        SubassemblyEntity updatedSubassembly = new SubassemblyEntity();
        when(subassemblyRepository.findById(id)).thenReturn(Optional.of(existingSubassembly));
        subassemblyService.updateSubassembly(id, updatedSubassembly);
        verify(subassemblyRepository, times(1)).save(existingSubassembly);
        assertEquals(updatedSubassembly.getSubassemblyName(), existingSubassembly.getSubassemblyName());
    }

    @Test
    public void testUpdateSubassembly_NonexistentId() {
        Long id = 1L;
        SubassemblyEntity updatedSubassembly = new SubassemblyEntity();
        when(subassemblyRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> subassemblyService.updateSubassembly(id, updatedSubassembly));
    }

    @Test
    public void testDeleteSubassemblyById() {
        Long id = 1L;
        subassemblyService.deleteSubassemblyById(id);
        verify(subassemblyRepository, times(1)).deleteById(id);
    }
}