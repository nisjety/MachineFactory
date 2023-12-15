package com.example.maschinefactory.partTest;

import com.example.maschinefactory.part.*;
import com.example.maschinefactory.security.SecurityConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;


import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PartController.class)
@Import(SecurityConfig.class)
public class PartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PartService partService;

    private List<Part> parts;

    @BeforeEach
    void setUp() {
        parts = List.of(
                new Part(1L, "part1", "it does something"),
                new Part(2L, "part2", "it does nothing"),
                new Part(3L, "part3", "it for a subassembly")
        );
    }

    @Test
    @WithMockUser
    void shouldFindAllParts() throws Exception {
        int page =0;
        int size = 2;
        Pageable pageable = PageRequest.of(page, size);
        Page<Part> pagedResponse = new PageImpl<>(parts.subList(0, size), pageable, parts.size());

        when(partService.findAllParts(pageable)).thenReturn(pagedResponse);

        mockMvc.perform(get("/api/parts")
                        .param("page", String.valueOf(page))
                        .param("size", String.valueOf(size)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.totalPages", is(2)))
                .andExpect(jsonPath("$.totalElements", is(parts.size())));
    }
}
