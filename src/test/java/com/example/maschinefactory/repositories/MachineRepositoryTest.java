package com.example.maschinefactory.repositories;

import com.example.maschinefactory.machine.MachineEntity;
import com.example.maschinefactory.machine.MachineRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
class MachineRepositoryTest {

    @Autowired
    private MachineRepository machineRepository;

    @Test
    void testSaveAndGetMachine() {
        // Arrange
        MachineEntity machine = new MachineEntity();
        machine.setMachineName("Machine A");

        // Act
        machineRepository.save(machine);
        MachineEntity savedMachine = machineRepository.findById(machine.getId()).orElse(null);

        // Assert
        assertNotNull(savedMachine);
        assertEquals("Machine A", savedMachine.getMachineName());
    }

    @Test
    @DirtiesContext
    void testFindAllMachines() {
        MachineEntity machine1 = new MachineEntity();
        machine1.setMachineName("Machine A");

        MachineEntity machine2 = new MachineEntity();
        machine2.setMachineName("Machine B");

        machineRepository.saveAll(List.of(machine1, machine2));
        List<MachineEntity> machines = machineRepository.findAll();
        assertNotNull(machines);
        assertEquals(2, machines.size());
    }
}