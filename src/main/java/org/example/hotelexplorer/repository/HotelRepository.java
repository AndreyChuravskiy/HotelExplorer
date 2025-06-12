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

    @Query("""
    SELECT h
    FROM Hotel h
    WHERE 
      (SELECT COUNT(DISTINCT a.name) 
       FROM Hotel h2 JOIN h2.amenities a 
       WHERE h2 = h AND LOWER(a.name) IN :amenities) = :#{#amenities.size()}
    """)
    List<Hotel> findByAmenities(@Param("amenities") List<String> amenities);

    @Query("SELECT h.brand AS param, COUNT(h) AS count FROM Hotel h GROUP BY h.brand")
    List<Object[]> getBrandHistogram();

    // Histogram by city
    @Query("SELECT h.address.city AS param, COUNT(h) AS count FROM Hotel h GROUP BY h.address.city")
    List<Object[]> getCityHistogram();

    // Histogram by country
    @Query("SELECT h.address.country AS param, COUNT(h) AS count FROM Hotel h GROUP BY h.address.country")
    List<Object[]> getCountryHistogram();

    // Histogram by amenities
    @Query("SELECT a.name AS param, COUNT(DISTINCT h.id) AS count FROM Hotel h JOIN h.amenities a GROUP BY a.name")
    List<Object[]> getAmenitiesHistogram();
}