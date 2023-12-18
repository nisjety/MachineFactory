package com.example.maschinefactory.machine;

import com.example.maschinefactory.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController
public class MachineControllerImpl implements MachineController {
    private final MachineService machineService;

    public MachineControllerImpl(MachineService machineService) {
        this.machineService = machineService;
    }

    @Override
    public ResponseEntity<ApiResponse> createMachine(MachineEntity machineEntity) {
        machineService.createMachine(machineEntity);
        return new ResponseEntity<>(new ApiResponse("Machine created successfully", true, new Date()), HttpStatus.CREATED);

    }

    @Override
    public ResponseEntity<ApiResponse> updateMachine(Long id, MachineEntity machineEntity) {
        machineService.updateMachine(id, machineEntity);
        return new ResponseEntity<>(new ApiResponse("Machine updated successfully", true, new Date()), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<MachineEntity>> getAllMachines(int page, int size) {
        return new ResponseEntity<>(machineService.getAllMachines(page, size), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<MachineEntity> getAddressById(Long id) {
        return new ResponseEntity<>(machineService.getMachineById(id), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ApiResponse> deleteAddressById(Long id) {
        machineService.deleteMachineById(id);
        return new ResponseEntity<>(new ApiResponse("Machine updated successfully", true, new Date()), HttpStatus.OK);
    }
}
