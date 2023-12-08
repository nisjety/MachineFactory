package com.example.maschinefactory.customer;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.io.InputStream;

@Component
public class CustomerDataLoader implements CommandLineRunner {
    private static final Log LOG = LogFactory.getLog(CustomerDataLoader.class);

    private final ObjectMapper objectMapper;
    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerDataLoader(ObjectMapper objectMapper, CustomerRepository customerRepository) {
        this.objectMapper = objectMapper;
        this.customerRepository = customerRepository;
    }

    @Override
    public void run(String... args) {
        if (customerRepository.count() == 0) {
            String customerJson = "/FakeData/customers.json";
            LOG.info("Loading customers into database from JSON: " + customerJson);
            try (InputStream inputStream = TypeReference.class.getResourceAsStream(customerJson)) {
                Customers response = objectMapper.readValue(inputStream, Customers.class);
                customerRepository.saveAll(response.customers());
                if (inputStream == null) {
                    throw new RuntimeException("Cannot find file: " + customerJson);
                }
            } catch (IOException e) {
                LOG.error("Failed to read JSON data", e);
                throw new RuntimeException("Failed to read JSON data", e);
            }
        }
    }
}
