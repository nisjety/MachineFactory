package com.example.maschinefactory.subassembly;

import com.example.maschinefactory.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subassemblies")
public interface SubassemblyController {
    @PostMapping("")
    ResponseEntity<ApiResponse> createSubassembly(@RequestBody SubassemblyEntity subassemblyEntity);
    @PutMapping("/{id}")
    ResponseEntity<ApiResponse> updateSubassembly(@PathVariable("id") Long id, @RequestBody SubassemblyEntity subassemblyEntity);
    @GetMapping("")
    ResponseEntity<List<SubassemblyEntity>> getAllSubassemblies(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size);
    @GetMapping("/{id}")
    ResponseEntity<SubassemblyEntity> getSubassemblyById(@PathVariable("id") Long id);
    @DeleteMapping("/{id}")
    ResponseEntity<ApiResponse> deleteSubassemblyById(@PathVariable("id") Long id);
}
