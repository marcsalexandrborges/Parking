package me.dio.domain.repository;

import me.dio.domain.model.Parking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ParkingRepository extends JpaRepository<Parking, Long> {
    @Query("SELECT p FROM Parking p WHERE p.name = :name")
    Parking findByName(@Param("name") String name);

}