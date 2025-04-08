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
@Builder
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_id")
    Long id;
    @Column
    String annotation;
    @Column
    String title;
    @Column
    String description;
    @Column
    @ManyToOne(fetch = FetchType.LAZY)
    Category category;
    @Column(name = "created_on")
    LocalDateTime createdOn;
    @Column(name = "published_on")
    LocalDateTime publishedOn;
    @Column(name = "event_date")
    LocalDateTime eventDate;
    @Column
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    User initiator;
    @Column
    Location location;
    @Column
    boolean paid;
    @Column(name = "participant_limit")
    int participantLimit = 0;
    int confirmRequests;
    @Column(name = "request_moderation")
    boolean requestModeration = true;
    @Column
    @Enumerated(EnumType.STRING)
    EventState state;
    @Column
    Long views = 0L;

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
                state == event.state && Objects.equals(views, event.views);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, annotation, title, description, category, createdOn, publishedOn, eventDate, initiator,
                location, paid, participantLimit, confirmRequests, requestModeration, state, views);
    }
}
