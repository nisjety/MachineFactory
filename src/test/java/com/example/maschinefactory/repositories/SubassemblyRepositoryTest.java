package com.example.maschinefactory.repositories;

import com.example.maschinefactory.subassembly.SubassemblyEntity;
import com.example.maschinefactory.subassembly.SubassemblyRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
class SubassemblyRepositoryTest {

    @Autowired
    private SubassemblyRepository subassemblyRepository;

    @Test
    void testSaveAndGetSubassembly() {
        SubassemblyEntity subassembly = new SubassemblyEntity();
        subassembly.setSubassemblyName("Subassembly A");
        subassemblyRepository.save(subassembly);
        SubassemblyEntity savedSubassembly = subassemblyRepository.findById(subassembly.getId()).orElse(null);
        assertNotNull(savedSubassembly);
        assertEquals("Subassembly A", savedSubassembly.getSubassemblyName());
    }

    @Test
    @DirtiesContext
    void testFindAllSubassemblies() {
        SubassemblyEntity subassembly1 = new SubassemblyEntity();
        subassembly1.setSubassemblyName("Subassembly A");

        SubassemblyEntity subassembly2 = new SubassemblyEntity();
        subassembly2.setSubassemblyName("Subassembly B");
        subassemblyRepository.saveAll(List.of(subassembly1, subassembly2));
        List<SubassemblyEntity> subassemblies = subassemblyRepository.findAll();

        assertNotNull(subassemblies);
        assertEquals(2, subassemblies.size());
    }

}