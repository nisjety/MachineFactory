package com.example.maschinefactory.part;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PartRepository extends JpaRepository<Part, Long> {

    Page<Part> findByPartName(String partName, Pageable pageable);

    Optional<Part> findByDescription(String description);

    void deletePartByPartId(Long partId);
}