package ru.practicum.event.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.event.model.Event;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long>, QuerydslPredicateExecutor<Event> {
    boolean existsByCategory_Id(Long categoryId);

    List<Event> findByIdIn(List<Long> events);

    @Modifying
    @Transactional
    @Query("UPDATE Event e " +
            "SET e.confirmRequests = e.confirmRequests + 1 " +
            "WHERE e.id = ?1")
    void increaseConfirmRequests(Long eventId);

    @Modifying
    @Transactional
    @Query("UPDATE Event e " +
            "SET e.confirmRequests = e.confirmRequests - 1 " +
            "WHERE e.id = ?1")
    void decreaseConfirmRequests(Long eventId);

    List<Event> findByInitiator_Id(Long userId);
}
