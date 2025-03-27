package org.bpce.test.bpcetest.service;

import org.bpce.test.bpcetest.dto.MeetingDto;
import org.bpce.test.bpcetest.dto.PlanificationResponseDto;
import org.bpce.test.bpcetest.entity.Meeting;

import java.util.List;

public interface MeetingService {

    PlanificationResponseDto assignMeetingsToRooms(List<MeetingDto> meetingDtoList);

    Meeting assignMeetingToRoom(MeetingDto meetingDto);

    List<Meeting> getAllMeetings(String roomName, String startTime, String endTime);

}
