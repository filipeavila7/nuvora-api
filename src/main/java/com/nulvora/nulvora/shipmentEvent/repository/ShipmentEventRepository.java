package com.nulvora.nulvora.shipmentEvent.repository;

import com.nulvora.nulvora.shipmentEvent.entity.ShipmentEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShipmentEventRepository extends JpaRepository<ShipmentEvent, Long> {
}
