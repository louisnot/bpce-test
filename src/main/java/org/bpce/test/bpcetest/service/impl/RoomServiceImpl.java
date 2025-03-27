package org.bpce.test.bpcetest.service.impl;

import org.bpce.test.bpcetest.entity.Room;
import org.bpce.test.bpcetest.repository.RoomRepository;
import org.bpce.test.bpcetest.service.RoomService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;

    public RoomServiceImpl(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }
    @Override
    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }
}
