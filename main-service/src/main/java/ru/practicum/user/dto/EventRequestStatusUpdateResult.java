package ru.practicum.user.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class EventRequestStatusUpdateResult {
    ParticipationRequestDto confirmedRequests;
    ParticipationRequestDto rejectedRequests;
}
