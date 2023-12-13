package com.example.maschinefactory.part;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class PartValidator {

    private final PartRepository partRepository;

    public PartValidator(PartRepository partRepository) {
        this.partRepository = partRepository;
    }

    public boolean validatePartData(Part part) {
        return !StringUtils.isEmpty(part.description());
    }

    public void validatePartCredentials(Long partId) {
        Part part = partRepository.findById(partId)
                .orElseThrow(PartNotFoundException::new);
    }
}

