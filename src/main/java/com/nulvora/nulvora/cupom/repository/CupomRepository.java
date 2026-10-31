package com.nulvora.nulvora.cupom.repository;

import com.nulvora.nulvora.cupom.entity.Cupom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CupomRepository extends JpaRepository<Cupom, Long> {
}
