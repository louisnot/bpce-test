package org.bpce.test.bpcetest.repository;

import org.bpce.test.bpcetest.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {

    @Query("SELECT r FROM Room r WHERE r.capacity >= :participants " +
            "AND (r.ecran = :besoinEcran OR :besoinEcran = false) " +
            "AND (r.pieuvre = :besoinPieuvre OR :besoinPieuvre = false) " +
            "AND (r.tableau = :besoinTableau OR :besoinTableau = false) " +
            "AND (r.webcam = :besoinWebcam OR :besoinWebcam = false) " +
            "AND r NOT IN (SELECT m.room FROM Meeting m WHERE " +
            "(m.startTime <= :endTime AND m.endTime >= :startTime))")
    List<Room> findAvailableRooms(
            @Param("participants") int participants,
            @Param("besoinEcran") boolean besoinEcran,
            @Param("besoinPieuvre") boolean besoinPieuvre,
            @Param("besoinTableau") boolean besoinTableau,
            @Param("besoinWebcam") boolean besoinWebcam,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime
    );
}
