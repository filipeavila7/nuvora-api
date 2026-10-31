package com.nulvora.nulvora.shipmentEvent.entity;

import com.nulvora.nulvora.common.entity.BaseEntity;
import com.nulvora.nulvora.shipment.entity.Shipment;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ShipmentEvent extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "shipment_id")
    private Shipment shipment;

    private String status;

    private String location;

    private String description;

    private LocalDateTime eventDate;
}
