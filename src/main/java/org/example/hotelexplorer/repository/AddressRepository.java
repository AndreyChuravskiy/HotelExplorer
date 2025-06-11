package org.example.hotelexplorer.repository;

import org.example.hotelexplorer.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {
}