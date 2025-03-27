package org.bpce.test.bpcetest.dto;

import lombok.Data;
import org.bpce.test.bpcetest.entity.Meeting;

import java.util.List;

@Data
public class PlanificationResponseDto {
    private List<Meeting> scheduledMeetings;
    private List<ErrorMeetingDto> errorMeetingDtos;

    public PlanificationResponseDto(List<Meeting> scheduledMeetings, List<ErrorMeetingDto> failedMeetings) {
        this.scheduledMeetings = scheduledMeetings;
        this.errorMeetingDtos = failedMeetings;
    }
}
