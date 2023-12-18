package com.example.maschinefactory.impl;

import com.example.maschinefactory.machine.MachineControllerImpl;
import com.example.maschinefactory.machine.MachineEntity;
import com.example.maschinefactory.machine.MachineRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MachineControllerImpl.class)
class MachineControllerImplTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MachineRepository machineRepository;

    @Test
    void testCreateMachine() throws Exception {
        MachineEntity machineEntity = new MachineEntity();
        machineEntity.setMachineName("Machine 1");
        // Add other fields as needed

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1.0/machines")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(machineEntity)))
                .andExpect(status().isOk());

        Mockito.verify(machineRepository, Mockito.times(1)).save(Mockito.any(MachineEntity.class));
    }

    @Test
    void testGetMachineById() throws Exception {
        Long machineId = 1L;
        MachineEntity machineEntity = new MachineEntity();
        machineEntity.setId(machineId);
        machineEntity.setMachineName("Machine 1");
        // Add other fields as needed

        Mockito.when(machineRepository.findById(machineId)).thenReturn(java.util.Optional.of(machineEntity));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1.0/machines/{id}", machineId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(machineEntity.getId()))
                .andExpect(jsonPath("$.machineName").value(machineEntity.getMachineName()));
        // Add more assertions for other fields if needed
    }

    @Test
    void testGetAllMachines() throws Exception {
        int page = 0;
        int size = 10;
        List<MachineEntity> machines = Arrays.asList(new MachineEntity(), new MachineEntity());
        // Add fields for machines as needed

        Mockito.when(machineRepository.findAll(Mockito.any(PageRequest.class))).thenReturn(new PageImpl<>(machines));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1.0/machines")
                        .param("page", String.valueOf(page))
                        .param("size", String.valueOf(size)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(machines.size()));
    }

    @Test
    void testUpdateMachine() throws Exception {
        Long machineId = 1L;
        MachineEntity updatedMachineEntity = new MachineEntity();
        updatedMachineEntity.setMachineName("Updated Machine");
        // Add other fields as needed

        Mockito.when(machineRepository.findById(machineId)).thenReturn(java.util.Optional.of(new MachineEntity()));

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1.0/machines/{id}", machineId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedMachineEntity)))
                .andExpect(status().isOk());

        Mockito.verify(machineRepository, Mockito.times(1)).save(Mockito.any(MachineEntity.class));
    }

    @Test
    void testDeleteMachineById() throws Exception {
        Long machineId = 1L;

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1.0/machines/{id}", machineId))
                .andExpect(status().isOk());

        Mockito.verify(machineRepository, Mockito.times(1)).deleteById(machineId);
    }
}