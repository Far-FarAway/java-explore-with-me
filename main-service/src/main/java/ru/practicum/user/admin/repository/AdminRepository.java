package ru.practicum.user.admin.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.user.model.User;


import java.util.List;

public interface AdminRepository extends JpaRepository<User, Long> {
    boolean existsById(Long userId);

    Page<User> findByIdIn(List<Long> ids, Pageable pageable);

    List<User> findByIdIn(List<Long> ids);
}
