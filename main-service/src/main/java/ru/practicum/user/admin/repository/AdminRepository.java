package ru.practicum.user.admin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.user.model.User;

import java.util.List;

@Repository
public interface AdminRepository extends JpaRepository<User, Long> {
    public boolean existsById(Long userId);

    public List<User> findByIdIn(List<Long> ids);
}
