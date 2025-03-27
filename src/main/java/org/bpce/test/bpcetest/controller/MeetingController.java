package org.bpce.test.bpcetest.controller;

import org.bpce.test.bpcetest.dto.MeetingDto;
import org.bpce.test.bpcetest.dto.PlanificationResponseDto;
import org.bpce.test.bpcetest.entity.Meeting;
import org.bpce.test.bpcetest.service.impl.MeetingServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/meetings")
public class MeetingController {


    private final MeetingServiceImpl meetingService;

    public MeetingController(MeetingServiceImpl meetingService) {
        this.meetingService = meetingService;
    }

    /**
     * Retourne la liste des meetings pour un créneau donné ou/et un nom de salle
     * ou alors retourne tous les meetings si pas de créneaux ni nom
     * @param startTime début du creneau (i.e 09:00:00)
     * @param endTime fin du créneau (i.e 12:00:00)
     * @param roomName nom de la salle
     * @return la liste des meetings
     */
    @GetMapping
    public List<Meeting> getMeetings(@RequestParam(required = false) String startTime,
                                     @RequestParam(required = false) String endTime,
                                     @RequestParam(required = false) String roomName) {
        return meetingService.getAllMeetings(startTime, endTime, roomName);
    }

    /**
     * endpoint à appeler pour effectuer la réservation des salles
     * @param meetingDtos liste des meetings à planifier
     */
    @PostMapping("/plan")
    public ResponseEntity<PlanificationResponseDto> planifierMeetings(@RequestBody List<MeetingDto> meetingDtos) {
        try {
            PlanificationResponseDto res = meetingService.assignMeetingsToRooms(meetingDtos);
            return ResponseEntity.ok(res);
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    /**
     * Permet d'effectuer une réservation de salle pour 1 SEUL meeting
     * @param meetingDto le meeting à planifier
     * @return
     */
    @PostMapping("/plan-single")
    public ResponseEntity<?> planifier1Meeting(@RequestBody MeetingDto meetingDto) {
        try {
            Meeting assignedMeeting = meetingService.assignMeetingToRoom(meetingDto);
            return ResponseEntity.ok(assignedMeeting);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}