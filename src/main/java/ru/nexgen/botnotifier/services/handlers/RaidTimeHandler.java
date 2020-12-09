package ru.nexgen.botnotifier.services.handlers;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.nexgen.botnotifier.configuration.properties.GameLogicProperties;
import ru.nexgen.botnotifier.services.MsgSender;
import ru.nexgen.botnotifier.services.TemplatesService;

import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by oleg.kurejko
 * Date: 16.07.2020
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class RaidTimeHandler implements MessageHandler {
    public static final String TEMPLATE_NAME = "raid_time_information";
    public static final String NEXT_RAID_TIME = "nextRaidTime";
    public static final String NEXT_DURATION = "nextDuration";
    public static final String ANOTHER_RAID_TIME = "anotherRaidTime";
    public static final String ANOTHER_DURATION = "anotherDuration";
    private final GameLogicProperties properties;
    private final MsgSender msgSender;
    private final TemplatesService templatesService;

    @Override
    public boolean isValid(Update receivedMessage) {
        log.debug("Start RaidTimeHandler isValid check");
        return receivedMessage.hasMessage() && receivedMessage.getMessage().hasText()
                && receivedMessage.getMessage().getText().startsWith("/raid_time");
    }

    @Override
    public void handle(Update receivedMessage) {
        log.info("Start RaidTimeHandler handle process");
        Map<String, String> parameters = new HashMap<>();
        LocalTime currentTime = LocalTime.now();
        List<LocalTime> raidTimes = properties.getRaidTimes().stream()
                .map(text -> LocalTime.parse(text, DateTimeFormatter.ofPattern("HH:mm:ss")))
                .collect(Collectors.toList());

        if (raidTimes.size() < 2) {
            log.error("Should be specified at least two raid times, but now is {}", raidTimes.size());
            return;
        }

        Pair result = extractTwoNearestRaidTimes(raidTimes, currentTime);

        parameters.put(NEXT_RAID_TIME, getRaidTimeString(result.getNext()));
        parameters.put(NEXT_DURATION, getDurationString(currentTime, result.getNext()));
        parameters.put(ANOTHER_RAID_TIME, getRaidTimeString(result.getOther()));
        parameters.put(ANOTHER_DURATION, getDurationString(currentTime, result.getOther()));
        msgSender.send(templatesService.fillTemplate(TEMPLATE_NAME, parameters), receivedMessage.getMessage().getChatId());
    }

    private Pair extractTwoNearestRaidTimes(List<LocalTime> raidTimes, LocalTime currentTime) {
        LocalTime next = null;
        LocalTime other = null;
        Collections.sort(raidTimes);
        for (int i = 0; i < raidTimes.size(); i++) {
            if (next != null && other != null) {
                break;
            }
            if (raidTimes.get(i).isBefore(currentTime)) {
                continue;
            }
            next = raidTimes.get(i);
            if (i == raidTimes.size() - 1) {
                other = raidTimes.get(0);
            } else {
                other = raidTimes.get(i + 1);
            }
        }

        if (next == null) {
            //сегодня рейдов больше нет, пишем время завтрашнего дня
            next = raidTimes.get(0);
            other = raidTimes.get(1);
        }

        return new Pair(next, other);
    }

    private static String getRaidTimeString(LocalTime raid) {
        return String.format("%d:%02d", raid.getHour(), raid.getMinute());
    }

    private static String getDurationString(LocalTime first, LocalTime second) {
        long extraTime = 0L;
        if (first.isAfter(second)) {
            extraTime = 24L;
        }
        Duration duration = Duration.between(first, second)
                .plusHours(extraTime);

        return String.format("%02d:%02d",
                duration.toHours(),
                duration.toMinutesPart());
    }

    @Data
    private static class Pair {
        private final LocalTime next;
        private final LocalTime other;
    }
}
