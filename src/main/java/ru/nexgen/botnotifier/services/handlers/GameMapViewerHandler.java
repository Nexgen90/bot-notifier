package ru.nexgen.botnotifier.services.handlers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.nexgen.botnotifier.services.MsgSender;
import ru.nexgen.botnotifier.services.map.GameMapService;

/**
 * Created by nikolay.mikutskiy
 * Date: 10.11.2020
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class GameMapViewerHandler implements MessageHandler {
    public static final String GET_LOCATION_PREFFIX = "GET LOCATION ";
    private final MsgSender msgSender;
    private final GameMapService mapService;

    @Override
    public boolean isValid(Update receivedMessage) {
        return receivedMessage.hasMessage() && receivedMessage.getMessage().hasText()
                && receivedMessage.getMessage().getText().toUpperCase().startsWith(GET_LOCATION_PREFFIX);
    }

    @Override
    public void handle(Update receivedMessage) {
        int locationId = Integer.parseInt(receivedMessage.getMessage().getText().substring(GET_LOCATION_PREFFIX.length()));
        msgSender.send("Loaded game map: " + mapService.getLocationById(locationId), receivedMessage.getMessage().getChatId());
    }
}
