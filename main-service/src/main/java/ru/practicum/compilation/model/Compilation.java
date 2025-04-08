package ru.practicum.compilation.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.event.model.Event;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "compilations")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class Compilation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "compilation_id")
    private Long id;
    @Column
    private boolean pinned;
    @Column
    private String title;
    @ManyToMany
    @JoinTable(name = "compilations_events",
                joinColumns = @JoinColumn(name = "compilation_id"),
                inverseJoinColumns = @JoinColumn(name = "event_id"))
    private Set<Event> events;
}
