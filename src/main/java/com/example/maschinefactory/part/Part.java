package com.example.maschinefactory.part;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;

@Entity
@Table(name = "parts")
@JsonInclude(JsonInclude.Include.NON_NULL)
public record Part(
        @Id
        Long partId,
        @NotEmpty
        String partName,
        String description
) {
}
