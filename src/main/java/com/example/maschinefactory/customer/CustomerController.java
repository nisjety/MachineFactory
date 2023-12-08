package com.example.maschinefactory.customer;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

   @GetMapping("")
    List<Customer> findAll() {
       return null;
   }

    // Additional mappings...
}
