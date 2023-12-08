package com.example.maschinefactory.repositories;

import com.example.maschinefactory.domains.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    List<Customer> findByName(String name);

    List<Customer> findByActive(boolean active);

    List<Customer> findByEmail(String email);

    List<Customer> findByNumber(int number);

}