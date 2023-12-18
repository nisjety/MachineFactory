package com.example.maschinefactory.subassembly;

import com.example.maschinefactory.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController
public class SubassemblyControllerImpl implements SubassemblyController {
    private final SubassemblyService subassemblyService;

    public SubassemblyControllerImpl(SubassemblyService subassemblyService) {
        this.subassemblyService = subassemblyService;
    }

    @Override
    public ResponseEntity<ApiResponse> createSubassembly(SubassemblyEntity subassemblyEntity) {
        subassemblyService.createSubassembly(subassemblyEntity);
        return new ResponseEntity<>(new ApiResponse("Subassembly created successfully", true, new Date()), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<ApiResponse> updateSubassembly(Long id, SubassemblyEntity subassemblyEntity) {
        subassemblyService.updateSubassembly(id, subassemblyEntity);
        return new ResponseEntity<>(new ApiResponse("Subassembly updated successfully", true, new Date()), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<SubassemblyEntity>> getAllSubassemblies(int page, int size) {
        return new ResponseEntity<>(subassemblyService.getAllSubassemblies(page, size), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<SubassemblyEntity> getSubassemblyById(Long id) {
        return new ResponseEntity<>(subassemblyService.getSubassemblyById(id), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ApiResponse> deleteSubassemblyById(Long id) {
        subassemblyService.deleteSubassemblyById(id);
        return new ResponseEntity<>(new ApiResponse("Subassembly deleted successfully", true, new Date()), HttpStatus.OK);
    }
}
