package ru.practicum.user.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class SearchProperties {
    List<Long> users;
    List<String> states;
    String text;
    List<Long> categories;
    Boolean paid;
    String rangeStart;
    String rangeEnd;
    boolean onlyAvailable;
    String sort;
    int from;
    int size;
}
