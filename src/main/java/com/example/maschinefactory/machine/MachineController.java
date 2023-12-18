package com.example.maschinefactory.machine;

import com.example.maschinefactory.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/machines")
public interface MachineController {
    @PostMapping("")
    ResponseEntity<ApiResponse> createMachine(@RequestBody MachineEntity machineEntity);
    @PutMapping("/{id}")
    ResponseEntity<ApiResponse> updateMachine(@PathVariable("id") Long id, @RequestBody MachineEntity machineEntity);
    @GetMapping("")
    ResponseEntity<List<MachineEntity>> getAllMachines(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size);
    @GetMapping("/{id}")
    ResponseEntity<MachineEntity> getAddressById(@PathVariable("id") Long id);
    @DeleteMapping("/{id}")
    ResponseEntity<ApiResponse> deleteAddressById(@PathVariable("id") Long id);
}
