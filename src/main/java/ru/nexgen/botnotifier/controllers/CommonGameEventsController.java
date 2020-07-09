package ru.nexgen.botnotifier.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.nexgen.botnotifier.models.requests.PushNotifyRq;
import ru.nexgen.botnotifier.services.MsgSender;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nikolay.mikutskiy
 * Date: 10.07.2020
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class CommonGameEventsController {
    private final MsgSender msgSender;

    private static List<Long> chatIds = new ArrayList<>();
    static {
        chatIds.add(330432380L);
        chatIds.add(96553356L);
    }

    /**
     * Пример вызова:
     * curl -i -X POST localhost:8080/push/notification -d '{"text": "Рейд-босс на 6км убит!"}' --header 'Content-Type: application/json'
     */
    @PostMapping(value = "/push/notification")
    public void pushNotification(@RequestBody PushNotifyRq request) {
        log.info("Get post request: {}", request);

        chatIds.forEach(id -> msgSender.send(request.getText(), id));
    }
}
