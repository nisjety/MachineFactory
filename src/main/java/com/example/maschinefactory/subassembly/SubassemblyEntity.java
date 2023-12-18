package com.example.maschinefactory.subassembly;

import com.example.maschinefactory.machine.MachineEntity;
import com.example.maschinefactory.part.*;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "subassembly")
public class SubassemblyEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String subassemblyName;

    @ManyToOne
    @JoinColumn(name = "machine_id")
    private MachineEntity machine;

    @OneToMany(mappedBy = "subassembly", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Part> parts;
}

