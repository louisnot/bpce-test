package org.bpce.test.bpcetest.service;

import org.bpce.test.bpcetest.entity.Room;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface RoomService {

    List<Room> getAllRooms();
}
