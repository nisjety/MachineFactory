package com.example.maschinefactory.part;

import com.example.maschinefactory.subassembly.*;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;

import java.util.List;

@RestController
@RequestMapping("/api/parts")
public class PartController {

    private final PartService partService;

    public PartController(PartService partService) {
        this.partService = partService;
    }

    @GetMapping("")
    Page<Part> findAll(Pageable pageable){
        return partService.findAllParts(pageable);
    }

    @GetMapping("/{partId}")
    ResponseEntity<Part> findPartById(@PathVariable Long partId){
        return partService.findPartById(partId)
                .map(ResponseEntity::ok)
                .orElseThrow(PartNotFoundException:: new);//Error handling
    }

    //adding an extra et request
    @GetMapping("/description/{description}")
    ResponseEntity<Part> findPartByDescription(@PathVariable String description) {
        return partService.findPartByDescription(description)
                .map(ResponseEntity::ok)
                .orElseThrow(PartNotFoundException::new);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("")
    Part createPart(@RequestBody @Validated Part part) throws InvalidPartDataException {
        return partService.createPart(part);
    }


    @ResponseStatus(HttpStatus.ACCEPTED)
    @PutMapping("/{partId}")
    Part updatePart(@PathVariable Long partId, @RequestBody @Validated Part part) throws InvalidPartDataException {
        return partService.updatePart(partId, part);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{partId}")
    void deletePart(@PathVariable Long partId) throws PartNotFoundException{
        partService.deletePart(partId);
    }
}