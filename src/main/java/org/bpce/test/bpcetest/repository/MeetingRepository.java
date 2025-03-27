package org.bpce.test.bpcetest.repository;

import org.bpce.test.bpcetest.entity.Meeting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.List;

@Repository
public interface MeetingRepository extends JpaRepository<Meeting, Long> {

    List<Meeting> findByStartTimeBetween(LocalTime start, LocalTime end);

    List<Meeting> findByRoom_NameAndStartTimeBetween(String roomName, LocalTime start, LocalTime end);


}
