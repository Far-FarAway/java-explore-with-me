package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.model.Stat;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsRepository extends JpaRepository<Stat, Long> {
    @Query("SELECT COUNT(s)" +
            "FROM Stat s " +
            "WHERE s.uri = ?1 " +
            "GROUP BY s.uri ")
    public Long findHitsByUrl(String uri);

    @Query("SELECT COUNT(DISTINCT s.ip)" +
            "FROM Stat s " +
            "WHERE s.uri = ?1")
    public Long findUniqueHitsByUrl(String uri);

    public boolean existsByUri(String uri);

    @Query("SELECT s " +
            "FROM Stat s " +
            "WHERE (s.timestamp BETWEEN ?1 AND ?2) " +
            "AND s.uri IN ?3")
    public List<Stat> getStatsByUris(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query("SELECT s " +
            "FROM Stat s " +
            "WHERE (s.timestamp BETWEEN ?1 AND ?2) " +
            "AND s.uri LIKE CONCAT(?3, '%')")
    public List<Stat> getStatsByUris(LocalDateTime start, LocalDateTime end, String uris);

    @Query("SELECT s " +
            "FROM Stat s " +
            "WHERE s.timestamp BETWEEN ?1 AND ?2")
    public List<Stat> getStats(LocalDateTime start, LocalDateTime end);
}
