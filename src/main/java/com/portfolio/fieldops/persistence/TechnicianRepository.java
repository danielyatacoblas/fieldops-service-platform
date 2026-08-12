package com.portfolio.fieldops.persistence;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TechnicianRepository extends JpaRepository<TechnicianJpaEntity,UUID> {
    @Query(value="""
        SELECT id, name, latitude, longitude,
          ST_Distance(location, ST_SetSRID(ST_MakePoint(:longitude,:latitude),4326)::geography) AS "distanceMeters"
        FROM technicians
        WHERE available = true
          AND ST_DWithin(location, ST_SetSRID(ST_MakePoint(:longitude,:latitude),4326)::geography, :radius)
        ORDER BY "distanceMeters" LIMIT :limit
        """, nativeQuery=true)
    List<NearbyTechnician> findNearby(
        @Param("latitude") double latitude,
        @Param("longitude") double longitude,
        @Param("radius") double radiusMeters,
        @Param("limit") int limit);

    interface NearbyTechnician {
        UUID getId();
        String getName();
        double getLatitude();
        double getLongitude();
        double getDistanceMeters();
    }
}
