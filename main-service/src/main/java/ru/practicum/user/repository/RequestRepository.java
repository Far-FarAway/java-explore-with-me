package ru.practicum.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.user.model.ParticipationRequest;

import java.util.List;

public interface RequestRepository extends JpaRepository<ParticipationRequest, Long> {
    boolean existsByEventIdAndRequester(Long eventId, Long userId);

    List<ParticipationRequest> findByRequester(Long userId);
}
