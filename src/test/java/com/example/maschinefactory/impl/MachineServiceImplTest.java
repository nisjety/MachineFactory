package com.example.maschinefactory.impl;

import com.example.maschinefactory.exceptions.ResourceNotFoundException;
import com.example.maschinefactory.machine.MachineEntity;
import com.example.maschinefactory.machine.MachineRepository;
import com.example.maschinefactory.machine.MachineService;
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
public class MachineServiceImplTest {

    @Autowired
    private MachineService machineService;

    @MockBean
    private MachineRepository machineRepository;

    @Test
    public void testCreateMachine() {
        MachineEntity machineEntity = new MachineEntity();
        machineService.createMachine(machineEntity);
        verify(machineRepository, times(1)).save(machineEntity);
    }

    @Test
    public void testGetMachineById_ExistingId() {
        Long id = 1L;
        MachineEntity machineEntity = new MachineEntity();
        when(machineRepository.findById(id)).thenReturn(Optional.of(machineEntity));
        MachineEntity result = machineService.getMachineById(id);
        assertNotNull(result);
        assertEquals(machineEntity, result);
    }

    @Test
    public void testGetMachineById_NonexistentId() {
        Long id = 1L;
        when(machineRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> machineService.getMachineById(id));
    }

    @Test
    public void testGetAllMachines() {
        int page = 0;
        int size = 10;
        List<MachineEntity> expectedMachines = Arrays.asList(new MachineEntity(), new MachineEntity());
        PageImpl<MachineEntity> pageResult = new PageImpl<>(expectedMachines);
        when(machineRepository.findAll(any(PageRequest.class))).thenReturn(pageResult);
        List<MachineEntity> result = machineService.getAllMachines(page, size);
        assertNotNull(result);
        assertEquals(expectedMachines.size(), result.size());
    }

    @Test
    public void testUpdateMachine_ExistingId() {
        Long id = 1L;
        MachineEntity existingMachine = new MachineEntity();
        MachineEntity updatedMachine = new MachineEntity();
        when(machineRepository.findById(id)).thenReturn(Optional.of(existingMachine));
        machineService.updateMachine(id, updatedMachine);
        verify(machineRepository, times(1)).save(existingMachine);
        assertEquals(updatedMachine.getMachineName(), existingMachine.getMachineName());
    }

    @Test
    public void testUpdateMachine_NonexistentId() {
        Long id = 1L;
        MachineEntity updatedMachine = new MachineEntity();
        when(machineRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> machineService.updateMachine(id, updatedMachine));
    }

    @Test
    public void testDeleteMachineById() {
        Long id = 1L;
        machineService.deleteMachineById(id);
        verify(machineRepository, times(1)).deleteById(id);
    }
}