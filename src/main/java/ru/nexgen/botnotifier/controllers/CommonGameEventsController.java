package ru.nexgen.botnotifier.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.nexgen.botnotifier.models.requests.PushNotifyRq;
import ru.nexgen.botnotifier.services.DbService;
import ru.nexgen.botnotifier.services.MsgSender;
import ru.nexgen.botnotifier.services.TemplatesService;

import java.util.Map;

/**
 * Created by nikolay.mikutskiy
 * Date: 10.07.2020
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class CommonGameEventsController {
    private final DbService dbService;
    private final MsgSender msgSender;
    private final TemplatesService templatesService;

    /**
     * Пример вызова:
     * curl -i -X POST localhost:8080/push/notification -d '{"text": "Рейд-босс на 6км убит!"}' --header 'Content-Type: application/json'
     */
    @PostMapping(value = "/push/notification")
    public void pushNotification(@RequestBody PushNotifyRq request) {
        log.info("Get post-request: {}", request);

        dbService.getChatsMapper().getAllActiveChatIds().forEach(id -> msgSender.send(request.getText(), id));
    }

    /**
     * Пример вызова:
     * curl -i -X POST localhost:8080/push/notification/template/1 -d '{"locationName": "2км", "clanName": "Тестовый Клан"}' --header 'Content-Type: application/json'
     */
    @PostMapping(value = "/push/notification/template/{templateId}")
    public void pushTemplateNotification(@PathVariable Integer templateId,
                                         @RequestBody Map<String, String> parameters) {
        log.info("Get post-request: {}", parameters);

        dbService.getChatsMapper().getAllActiveChatIds().forEach(id ->
                msgSender.send(templatesService.fillTemplate(templateId, parameters), id));
    }

    @PostMapping(value = "/ban/chat/{id}/minutes/5")
    public String addBanTime(@PathVariable Long id) {
        log.info("Get post-request /ban/chat/{}/minutes/5", id);

        dbService.getChatsMapper().updateBanTime(id);
        return "OK";
    }
}
