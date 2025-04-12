package ru.practicum.user.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Data
@Builder
public class SearchProperties {
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
