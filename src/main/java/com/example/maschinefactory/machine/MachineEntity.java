package com.example.maschinefactory.machine;

import com.example.maschinefactory.order.OrderEntity;
import com.example.maschinefactory.subassembly.SubassemblyEntity;
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
@Table(name = "machine")
public class MachineEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String machineName;

    @ManyToOne
    @JoinColumn(name = "orderId")
    private OrderEntity order;

    @OneToMany(mappedBy = "machine", cascade = CascadeType.ALL)
    private List<SubassemblyEntity> subassemblies;

}
