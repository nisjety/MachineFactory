package com.example.maschinefactory.subassembly;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubassemblyRepository extends JpaRepository<SubassemblyEntity, Long> {
}
