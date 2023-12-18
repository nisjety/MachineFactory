package com.example.maschinefactory.partTest;

import com.example.maschinefactory.subassembly.*;
import com.example.maschinefactory.part.*;
import com.example.maschinefactory.security.SecurityConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;


import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Import(SecurityConfig.class)
@Transactional
public class PartIntegratedTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PartRepository partRepository;

    @Autowired
    private PartService partService;

    /*
    @Autowired
    private SubassemblyRepository subassemblyRepository;

    @Autowired SubassemblyService subassemblyService
*/
    @BeforeEach
    void setUp() {

        //Create a new Part
        Part part = new Part(1L, "part1", "it does something");

        // Check if the part's subassembly list is initialized, if not, initialize it
        /*
        if (part.getSubassemblies() == null) {
            part.setSubassemblies(new ArrayList<>());
        }

        // Add the part to the subassembly list
        part.getSubassemblies().add(subassembly);

        // Save the subassembly and part to the repository
        subassemblyRepository.save(subassembly);
        partRepository.save(part);

         */
    }

    @AfterEach
    void cleanUp() {
        partRepository.deleteAll();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldFindAllParts() throws Exception {
        mockMvc.perform(get("/api/parts")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(3)))
                .andExpect(jsonPath("$.content[0].partName", is("part1")))
                .andExpect(jsonPath("$.content[1].partName", is("part2")))
                .andExpect(jsonPath("$.content[2].partName", is("part3")));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @Rollback
    public void shouldCreateNewPart() throws Exception {
        Part part = new Part(4L, "part4", "its a new part");


        ObjectMapper objectMapper = new ObjectMapper();
        String partJson = objectMapper.writeValueAsString(part);

        mockMvc.perform(post("/api/parts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(partJson))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.partName", is("part4")))
                .andExpect(jsonPath("$.description", is("its a new part")));
    }
}
