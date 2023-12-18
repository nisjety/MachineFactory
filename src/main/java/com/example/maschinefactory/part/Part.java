package com.example.maschinefactory.part;

import com.example.maschinefactory.subassembly.SubassemblyEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;

@Entity
@Table(name = "parts")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Part {

        @Id
        private Long partId;

        @NotEmpty
        private String partName;

        private String description;

        @ManyToOne(fetch = FetchType.EAGER)
        @JoinColumn(name = "subassembly_id")
        private SubassemblyEntity subassembly;

        // Default constructor for JPA
        public Part() {
        }

        // Constructor with fields
        public Part(Long partId, String partName, String description) {
                this.partId = partId;
                this.partName = partName;
                this.description = description;
        }

        // Getters and setters
        public Long getPartId() {
                return partId;
        }

        public void setPartId(Long partId) {
                this.partId = partId;
        }

        public String getPartName() {
                return partName;
        }

        public void setPartName(String partName) {
                this.partName = partName;
        }

        public String getDescription() {
                return description;
        }

        public void setDescription(String description) {
                this.description = description;
        }

        @Override
        public String toString() {
                return "Part{" +
                        "partId=" + partId +
                        ", partName='" + partName + '\'' +
                        ", description='" + description + '\'' +
                        '}';
        }
}
