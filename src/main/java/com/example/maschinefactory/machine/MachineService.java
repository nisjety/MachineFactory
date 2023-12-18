package com.example.maschinefactory.machine;

import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface MachineService {
    void createMachine(MachineEntity machineEntity);
    MachineEntity getMachineById(Long id);
    List<MachineEntity> getAllMachines(int page, int size);
    void updateMachine(Long id, MachineEntity updatedMachine);
    void deleteMachineById(Long id);
}
