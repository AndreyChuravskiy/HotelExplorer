package org.example.hotelexplorer.repository;

import org.example.hotelexplorer.entity.Amenity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;

public interface AmenityRepository extends JpaRepository<Amenity, Long> {
    Optional<Amenity> findByName(String name);

    List<Amenity> findAllByNameIn(List<String> names);
}