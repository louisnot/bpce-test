package org.bpce.test.bpcetest.dto;

import lombok.Data;

@Data
public class ErrorMeetingDto {
    private MeetingDto meeting;
    private String reason;

    public ErrorMeetingDto(MeetingDto meeting, String reason) {
        this.meeting = meeting;
        this.reason = reason;
    }
}
