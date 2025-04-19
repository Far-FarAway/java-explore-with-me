package ru.practicum.user.admin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.user.model.User;

import java.util.List;

public interface AdminRepository extends JpaRepository<User, Long> {
    public boolean existsById(Long userId);

    public List<User> findByIdIn(List<Long> ids);
}
