package ru.practicum.event.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "events_locations")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class Location {
    private float lot;
    private float lon;
}
