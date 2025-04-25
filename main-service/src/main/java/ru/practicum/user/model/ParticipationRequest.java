package ru.practicum.user.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "requests")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ParticipationRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "request_id")
    Long id;
    @Column(nullable = false)
    LocalDateTime created;
    @Column(name = "event_id", nullable = false)
    Long eventId;
    @Column(name = "user_id", nullable = false)
    Long requester;
    @Column(nullable = false)
    RequestStatus status;
}
