package com.nuvora.nuvora.cupom.repository;

import com.nuvora.nuvora.cupom.entity.Cupom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CupomRepository extends JpaRepository<Cupom, Long> {
}
