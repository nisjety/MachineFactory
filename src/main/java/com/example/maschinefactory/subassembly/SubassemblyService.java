package com.example.maschinefactory.subassembly;

import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface SubassemblyService {
    void createSubassembly(SubassemblyEntity subassemblyEntity);
    SubassemblyEntity getSubassemblyById(Long id);
    List<SubassemblyEntity> getAllSubassemblies(int page, int size);
    void updateSubassembly(Long id, SubassemblyEntity updatedSubassembly);
    void deleteSubassemblyById(Long id);
}
