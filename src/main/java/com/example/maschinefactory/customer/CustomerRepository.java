package com.example.maschinefactory.customer;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends CrudRepository<Customer, Long> {

    List<Customer> findByName(String name);

    Optional<Customer> findByEmail(String email);

    List<Customer> findByActive(boolean active);

    Optional<Customer> findByPhoneNumber(String phoneNumber);

}
