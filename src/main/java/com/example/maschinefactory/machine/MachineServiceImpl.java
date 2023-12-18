package com.example.maschinefactory.machine;

import com.example.maschinefactory.exceptions.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MachineServiceImpl implements MachineService {

    private final MachineRepository machineRepository;

    public MachineServiceImpl(MachineRepository machineRepository) {
        this.machineRepository = machineRepository;
    }

    @Override
    public void createMachine(MachineEntity machineEntity) {
        machineRepository.save(machineEntity);
    }

    @Override
    public MachineEntity getMachineById(Long id) {
        return machineRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Machine", "Id", id));
    }

    @Override
    public List<MachineEntity> getAllMachines(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<MachineEntity> customerPage = machineRepository.findAll(pageRequest);
        return customerPage.getContent();
    }

    @Override
    public void updateMachine(Long id, MachineEntity updatedMachine) {
        MachineEntity machineEntity = machineRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Machine", "Id", id));
        machineEntity.setMachineName(updatedMachine.getMachineName());
        machineRepository.save(machineEntity);
    }

    @Override
    public void deleteMachineById(Long id) {
        machineRepository.deleteById(id);
    }
}