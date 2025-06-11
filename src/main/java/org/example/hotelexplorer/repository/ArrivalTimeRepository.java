package org.example.hotelexplorer.repository;

import org.example.hotelexplorer.entity.ArrivalTime;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArrivalTimeRepository extends JpaRepository<ArrivalTime, Long> {
}