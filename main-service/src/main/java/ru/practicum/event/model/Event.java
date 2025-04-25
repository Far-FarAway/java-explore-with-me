package ru.practicum.event.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.category.model.Category;
import ru.practicum.user.model.User;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "events")
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_id")
    Long id;
    @Column(length = 2000)
    String annotation;
    @Column(length = 120, nullable = false)
    String title;
    @Column(length = 7000, nullable = false)
    String description;
    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    Category category;
    @Column(name = "created_on", nullable = false)
    @Builder.Default
    LocalDateTime createdOn = LocalDateTime.now();
    @Column(name = "published_on")
    LocalDateTime publishedOn;
    @Column(name = "event_date", nullable = false)
    LocalDateTime eventDate;
    @ManyToOne
    @JoinColumn(name = "user_id")
    User initiator;
    @Column
    @Embedded
    Location location;
    @Column
    boolean paid = false;
    @Column(name = "participant_limit")
    @Builder.Default
    int participantLimit = 0;
    @Column(name = "confirmed_requests")
    int confirmRequests;
    @Column(name = "request_moderation")
    @Builder.Default
    boolean requestModeration = true;
    @Column
    @Enumerated(EnumType.STRING)
    EventState state;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return paid == event.paid && participantLimit == event.participantLimit &&
                confirmRequests == event.confirmRequests && requestModeration == event.requestModeration &&
                Objects.equals(id, event.id) && Objects.equals(annotation, event.annotation) &&
                Objects.equals(title, event.title) && Objects.equals(description, event.description) &&
                Objects.equals(category, event.category) && Objects.equals(createdOn, event.createdOn) &&
                Objects.equals(publishedOn, event.publishedOn) && Objects.equals(eventDate, event.eventDate) &&
                Objects.equals(initiator, event.initiator) && Objects.equals(location, event.location) &&
                state == event.state;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, annotation, title, description, category, createdOn, publishedOn, eventDate, initiator,
                location, paid, participantLimit, confirmRequests, requestModeration, state);
    }
}
