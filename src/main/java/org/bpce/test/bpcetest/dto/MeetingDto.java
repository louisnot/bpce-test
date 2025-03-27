package org.bpce.test.bpcetest.dto;

import lombok.Data;

@Data
public class MeetingDto {
    private String type;
    private int participants;
    private String startTime;
}
