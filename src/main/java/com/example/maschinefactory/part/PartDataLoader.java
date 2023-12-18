package com.example.maschinefactory.part;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;

@Component
public class PartDataLoader implements CommandLineRunner{

        private static final Log LOG = LogFactory.getLog(com.example.maschinefactory.address.AddressDataLoader.class);

        private final ObjectMapper objectMapper;
        private final PartRepository partRepository ;

        @Autowired
        public PartDataLoader(ObjectMapper objectMapper, PartRepository partRepository) {
            this.objectMapper = objectMapper;
            this.partRepository = partRepository;
        }

        @Override
        public void run(String... args) {
            if (partRepository.count() == 0) {
                String partJson = "/FakeData/parts.json";
                LOG.info("Loading part into database from JSON: " + partJson);
                try (InputStream inputStream = TypeReference.class.getResourceAsStream(partJson)) {
                    Parts response = objectMapper.readValue(inputStream, Parts.class);
                    partRepository.saveAll(response.parts());
                    if (inputStream == null) {
                        throw new RuntimeException("Cannot find file: " + partJson);
                    }
                } catch (IOException e) {
                    LOG.error("Failed to read JSON data", e);
                    throw new RuntimeException("Failed to read JSON data", e);
                }
            }
        }
    }
