package com.nuvora.nuvora.address.repository;

import com.nuvora.nuvora.address.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
    boolean existsByUserIdAndZipCodeAndStreetAndNumberAndComplement(
            Long userId,
            String zipCode,
            String street,
            String number,
            String complement
    );
}
