package org.bpce.test.bpcetest.service.impl;

import org.bpce.test.bpcetest.dto.ErrorMeetingDto;
import org.bpce.test.bpcetest.dto.MeetingDto;
import org.bpce.test.bpcetest.dto.PlanificationResponseDto;
import org.bpce.test.bpcetest.entity.Meeting;
import org.bpce.test.bpcetest.entity.Room;
import org.bpce.test.bpcetest.repository.MeetingRepository;
import org.bpce.test.bpcetest.repository.RoomRepository;
import org.bpce.test.bpcetest.service.MeetingService;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.*;

@Service
public class MeetingServiceImpl implements MeetingService {

    private final LocalTime OPENING_TIME = LocalTime.of(8, 0);
    private final LocalTime CLOSING_TIME = LocalTime.of(19, 0);

    private final MeetingRepository meetingRepository;

    private final RoomRepository roomRepository;

    public MeetingServiceImpl(MeetingRepository meetingRepository, RoomRepository roomRepository) {
        this.meetingRepository = meetingRepository;
        this.roomRepository = roomRepository;
    }


    /**
     * assigne une room pour un meeting si possible ou throw une erreur
     * @param meetingDtoList list des meetings nécessitant une room
     * returns PlanificationResponseDto contenant les meetings possibles et ceux impossibles
     */
    @Override
    public PlanificationResponseDto assignMeetingsToRooms(List<MeetingDto> meetingDtoList) {
        List<Meeting> plannedMeetings = new ArrayList<>();
        List<ErrorMeetingDto> errorMeetingDtos = new ArrayList<>();

        for (MeetingDto meetingDto : meetingDtoList) {
            try {
                if(LocalTime.parse(meetingDto.getStartTime()).isBefore(OPENING_TIME) || LocalTime.parse(meetingDto.getStartTime()).isAfter(CLOSING_TIME)) {
                    errorMeetingDtos.add(new ErrorMeetingDto(meetingDto, "On ne peut pas réserver une salle avant 8:00 ou après 20:00"));
                    continue;
                }
                Room assignedRoom = findBestRoomForMeeting(meetingDto);
                if (assignedRoom == null) {
                    errorMeetingDtos.add(new ErrorMeetingDto(meetingDto, "Aucune room disponible pour le meeting"));
                    continue;
                }
                Meeting meeting = new Meeting();
                meeting.setType(meetingDto.getType());
                meeting.setParticipants(meetingDto.getParticipants());
                meeting.setStartTime(LocalTime.parse(meetingDto.getStartTime()));
                meeting.setEndTime(LocalTime.parse(meetingDto.getStartTime()).plusHours(1));
                meeting.setRoom(assignedRoom);
                meetingRepository.save(meeting);
                plannedMeetings.add(meeting);
            }catch (Exception e) {
                errorMeetingDtos.add(new ErrorMeetingDto(meetingDto, "erreur création meeting" + e.getMessage()));
            }
        }
        return new PlanificationResponseDto(plannedMeetings, errorMeetingDtos);
    }

    @Override
    public Meeting assignMeetingToRoom(MeetingDto meetingDto) {
        Room assignedRoom = findBestRoomForMeeting(meetingDto);
        if (assignedRoom == null) {
            throw new RuntimeException("No available room for the meeting at " + meetingDto.getStartTime());
        }

        Meeting meeting = new Meeting();
        meeting.setType(meetingDto.getType());
        meeting.setParticipants(meetingDto.getParticipants());
        meeting.setStartTime(LocalTime.parse(meetingDto.getStartTime()));
        meeting.setEndTime(LocalTime.parse(meetingDto.getStartTime()).plusHours(1));
        meeting.setRoom(assignedRoom);

        return meetingRepository.save(meeting);
    }

    @Override
    public List<Meeting> getAllMeetings(String roomName, String startTime, String endTime) {
        LocalTime start = startTime != null ? LocalTime.parse(startTime) : LocalTime.MIN;
        LocalTime end = endTime != null ? LocalTime.parse(endTime) : LocalTime.MAX;

        if (roomName != null) {
            return meetingRepository.findByRoom_NameAndStartTimeBetween(roomName, start, end);
        }
        return meetingRepository.findByStartTimeBetween(start, end);
    }


    private Room findBestRoomForMeeting(MeetingDto meetingDto) {
        LocalTime startTime = LocalTime.parse(meetingDto.getStartTime());
        LocalTime endTime = startTime.plusHours(1);

        List<Room> availableRooms = roomRepository.findAvailableRooms(
                meetingDto.getParticipants(),
                meetingDto.getType().equals("VC") || meetingDto.getType().equals("RC"),
                meetingDto.getType().equals("VC") || meetingDto.getType().equals("RC"),
                meetingDto.getType().equals("SPEC") || meetingDto.getType().equals("RC"),
                meetingDto.getType().equals("VC"),
                startTime,
                endTime
        );

        return availableRooms.stream().min(Comparator
                        .comparingInt(Room::getCapacity)
                        .thenComparingInt(room -> countExtraEquipment(room, meetingDto)))
                .orElse(null);
    }

    /**
     * compte le nb d'equipment non nécessaire pour chaque room potentiel d'un meeting
     * le but étant de retourner la room avec le moins de matériel "inutile" pour le meeting
     * @param room potentiellement reservable
     * @param meetingDto le meeting avec les besoins
     * @return un int qui est le nb d'extra non utile pour la pièce
     */
    private int countExtraEquipment(Room room, MeetingDto meetingDto) {
        int extra = 0;

        boolean needsEcran = meetingDto.getType().equals("VC") || meetingDto.getType().equals("RC");
        boolean needsPieuvre = meetingDto.getType().equals("VC") || meetingDto.getType().equals("RC");
        boolean needsWebcam = meetingDto.getType().equals("VC");
        boolean needsTableau = meetingDto.getType().equals("SPEC") || meetingDto.getType().equals("RC");

        if (room.isEcran() && !needsEcran) extra++;
        if (room.isPieuvre() && !needsPieuvre) extra++;
        if (room.isWebcam() && !needsWebcam) extra++;
        if (room.isTableau() && !needsTableau) extra++;

        return extra;
    }
}
