package com.nulvora.nulvora.address.repository;

import com.nulvora.nulvora.address.entity.Address;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
    boolean existsByUserIdAndZipCodeAndStreetAndNumberAndComplement(
            Long userId,
            String zipCode,
            String street,
            String number,
            String complement
    );

    boolean existsByUserIdAndZipCodeAndStreetAndNumberAndComplementAndIdNot(
            Long userId,
            String zipCode,
            String street,
            String number,
            String complement,
            Long addressId
    );


    Page<Address> findByUserId(Long userId, Pageable pageable);

    Optional<Address> findByIdAndUserId(Long addressId, Long userId);
}
