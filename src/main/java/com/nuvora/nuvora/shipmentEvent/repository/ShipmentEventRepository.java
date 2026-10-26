package com.nuvora.nuvora.shipmentEvent.repository;

import com.nuvora.nuvora.shipmentEvent.entity.ShipmentEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShipmentEventRepository extends JpaRepository<ShipmentEvent, Long> {
}
