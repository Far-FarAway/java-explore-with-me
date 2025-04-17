package ru.practicum.event.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import ru.practicum.event.model.Event;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long>, QuerydslPredicateExecutor<Event> {
    boolean existsByCategory_Id(Long catId);

    List<Event> findByIdIn(List<Long> events);

    @Query("UPDATE Event e " +
            "SET e.confirmRequests = e.confirmRequests + 1 " +
            "WHERE e.id = ?1")
    void increaseConfirmRequests(Long eventId);

    @Query("UPDATE Event e " +
            "SET e.confirmRequests = e.confirmRequests - 1 " +
            "WHERE e.id = ?1")
    void decreaseConfirmRequests(Long eventId);
}
