package com.homedepot.travel.repository;

import com.homedepot.travel.model.TravelPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TravelPlanRepository extends JpaRepository<TravelPlan, Long> {
    
    List<TravelPlan> findByTravelerName(String travelerName);
    
    @Query("SELECT tp FROM TravelPlan tp WHERE tp.startDate >= :startDate AND tp.endDate <= :endDate")
    List<TravelPlan> findByDateRange(@Param("startDate") java.time.LocalDate startDate, 
                                   @Param("endDate") java.time.LocalDate endDate);
    
    @Query("SELECT tp FROM TravelPlan tp WHERE tp.travelerName = :travelerName AND tp.startDate >= :startDate")
    List<TravelPlan> findByTravelerAndFutureStartDate(@Param("travelerName") String travelerName, 
                                                    @Param("startDate") java.time.LocalDate startDate);
}
