package com.example.maschinefactory.address;

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
public class AddressDataLoader implements CommandLineRunner {
    private static final Log LOG = LogFactory.getLog(AddressDataLoader.class);

    private final ObjectMapper objectMapper;
    private final AddressRepository addressRepository ;

    @Autowired
    public AddressDataLoader(ObjectMapper objectMapper, AddressRepository addressRepository) {
        this.objectMapper = objectMapper;
        this.addressRepository = addressRepository;
    }

    @Override
    public void run(String... args) {
        if (addressRepository.count() == 0) {
            String addressJson = "/FakeData/addresses.json";
            LOG.info("Loading address into database from JSON: " + addressJson);
            try (InputStream inputStream = TypeReference.class.getResourceAsStream(addressJson)) {
                Addresses response = objectMapper.readValue(inputStream, Addresses.class);
                addressRepository.saveAll(response.addresses());
                if (inputStream == null) {
                    throw new RuntimeException("Cannot find file: " + addressJson);
                }
            } catch (IOException e) {
                LOG.error("Failed to read JSON data", e);
                throw new RuntimeException("Failed to read JSON data", e);
            }
        }
    }
}
