package ru.job4j.todo.service;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.TimeZone;

@Service
public class TimezoneService {

    public String format(LocalDateTime created, String zoneId) {
        ZoneId userZone = zoneId == null ? TimeZone.getDefault().toZoneId() : ZoneId.of(zoneId);
        return created
                .atZone(ZoneId.systemDefault())
                .withZoneSameInstant(userZone)
                .format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
    }

    public List<String> getAllZoneIds() {
        return Arrays.stream(TimeZone.getAvailableIDs())
                .sorted()
                .toList();
    }
}
