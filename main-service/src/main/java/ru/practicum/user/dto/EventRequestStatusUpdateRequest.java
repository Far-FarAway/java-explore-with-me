package ru.practicum.user.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.user.model.RequestStatus;

import java.util.List;

@Builder
@Data
public class EventRequestStatusUpdateRequest {
    private List<Long> requestIds;
    private RequestStatus status;
}
