package ru.practicum.event.model;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import ru.practicum.event.dto.EventShortDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DateComparator implements Comparator<EventShortDto> {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public int compare(EventShortDto o1, EventShortDto o2) {
        LocalDateTime date1 = LocalDateTime.parse(o1.getEventDate(), formatter);
        LocalDateTime date2 = LocalDateTime.parse(o2.getEventDate(), formatter);

        if (date1.isAfter(date2)) {
            return 3;
        } else if (date1.isBefore(date2)) {
            return -3;
        } else {
            return 0;
        }
    }
}
