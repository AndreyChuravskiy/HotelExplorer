package org.example.hotelexplorer.repository;

import org.example.hotelexplorer.entity.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HotelRepository extends JpaRepository<Hotel, Long> {
    List<Hotel> findByNameContainingIgnoreCase(String name);

    List<Hotel> findByBrandContainingIgnoreCase(String brand);

    @Query("SELECT h FROM Hotel h WHERE LOWER(h.address.city) LIKE LOWER(CONCAT('%', :city, '%'))")
    List<Hotel> findByCity(@Param("city") String city);

    @Query("SELECT h FROM Hotel h WHERE LOWER(h.address.country) LIKE LOWER(CONCAT('%', :country, '%'))")
    List<Hotel> findByCountry(@Param("country") String country);

    @Query("SELECT DISTINCT h FROM Hotel h JOIN h.amenities a WHERE a.name IN :amenities")
    List<Hotel> findByAmenities(@Param("amenities") List<String> amenities);
}