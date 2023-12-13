package com.example.maschinefactory.part;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PartService {

    private final PartRepository partRepository;

    private final PartValidator partValidator;

    public PartService(PartRepository partRepository, PartValidator partValidator) {
        this.partRepository = partRepository;
        this.partValidator = partValidator;
    }

    Page<Part> findAllParts(Pageable pageable){
        return partRepository.findAll(pageable);
    }

    Optional<Part> findPartById(Long partId){
        return partRepository.findById(partId);
    }

    Page<Part> findPartByPartName(String partName, Pageable pageable){
        return partRepository.findByPartName(partName, pageable);
    }

    Optional<Part> findPartByDescription(String description){
        return partRepository.findByDescription(description);
    }

    Part createPart(Part part) throws InvalidPartDataException{
        if(partValidator.validatePartData(part)){
            return partRepository.save(part);
        }else{
            try{
                throw new InvalidPartDataException();
            }catch (InvalidPartDataException e){
                throw new RuntimeException(e);
            }
        }
    }

        Part updatePart(Long partId, Part part)throws InvalidPartDataException{
        if (partValidator.validatePartData(part)) {
            Optional<Part> existing = partRepository.findById(partId);
            if (existing.isPresent()) {
                Part updatedPart = new Part(
                        existing.get().partId(),
                        existing.get().partName(),
                        part.description()
                );
                return partRepository.save(updatedPart);
            } else {
                throw new PartNotFoundException();
            }
        }else{
                try {
                    throw new InvalidPartDataException();
                } catch (InvalidPartDataException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        public void deletePart(Long partId){
        partRepository.deletePartByPartId(partId);
        }

}
