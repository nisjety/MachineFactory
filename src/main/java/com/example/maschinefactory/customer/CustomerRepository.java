package com.example.maschinefactory.customer;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Page<Customer> findByName(String name, Pageable pageable);

    Optional<Customer> findByEmail(String email);

    Page<Customer> findByActive(boolean active, Pageable pageable);

    Optional<Customer> findByPhoneNumber(String phoneNumber);

    void deleteByEmail(String email);
}


