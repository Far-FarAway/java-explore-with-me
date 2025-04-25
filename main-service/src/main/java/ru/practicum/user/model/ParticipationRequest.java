package ru.practicum.user.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Entity
@Table(name = "requests")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
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
