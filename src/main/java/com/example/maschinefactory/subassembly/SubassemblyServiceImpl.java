package com.example.maschinefactory.subassembly;

import com.example.maschinefactory.exceptions.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubassemblyServiceImpl implements SubassemblyService {

    private final SubassemblyRepository subassemblyRepository;

    public SubassemblyServiceImpl(SubassemblyRepository subassemblyRepository) {
        this.subassemblyRepository = subassemblyRepository;
    }

    @Override
    public void createSubassembly(SubassemblyEntity subassemblyEntity) {
        subassemblyRepository.save(subassemblyEntity);
    }

    @Override
    public SubassemblyEntity getSubassemblyById(Long id) {
        return subassemblyRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Subassembly", "Id", id));
    }

    @Override
    public List<SubassemblyEntity> getAllSubassemblies(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<SubassemblyEntity> subassemblyPage = subassemblyRepository.findAll(pageRequest);
        return subassemblyPage.getContent();
    }

    @Override
    public void updateSubassembly(Long id, SubassemblyEntity updatedSubassembly) {
        SubassemblyEntity subassemblyEntity = subassemblyRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Subassembly", "Id", id));
        subassemblyEntity.setSubassemblyName(updatedSubassembly.getSubassemblyName());
        subassemblyRepository.save(subassemblyEntity);
    }

    @Override
    public void deleteSubassemblyById(Long id) {
        subassemblyRepository.deleteById(id);
    }
}
