package ru.practicum.event.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.event.model.Event;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    boolean existsByCategory_Id(Long catId);

    List<Event> findByIdIn(List<Long> events);
}
