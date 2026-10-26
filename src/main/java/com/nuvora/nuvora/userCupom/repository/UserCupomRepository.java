package com.nuvora.nuvora.userCupom.repository;

import com.nuvora.nuvora.userCupom.entity.UserCupom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserCupomRepository extends JpaRepository<UserCupom, Long> {
}
