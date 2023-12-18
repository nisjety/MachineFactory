package com.example.maschinefactory.configs;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class ModelMapperConfig {
    @Bean
    public ModelMapper getModelMapper() {
        return new ModelMapper();
    }
}
