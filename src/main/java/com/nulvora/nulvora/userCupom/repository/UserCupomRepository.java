package com.nulvora.nulvora.userCupom.repository;

import com.nulvora.nulvora.userCupom.entity.UserCupom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserCupomRepository extends JpaRepository<UserCupom, Long> {
}
