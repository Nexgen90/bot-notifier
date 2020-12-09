package ru.nexgen.botnotifier.services;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.nexgen.botnotifier.services.handlers.AddedNewUserHandler;
import ru.nexgen.botnotifier.services.handlers.GameMapViewerHandler;
import ru.nexgen.botnotifier.services.handlers.HelloCallbackHandler;
import ru.nexgen.botnotifier.services.handlers.HelloHandler;
import ru.nexgen.botnotifier.services.handlers.RaidTimeHandler;

/**
 * Don't forget to add all handlers to test that your new handler does't affect to old
 */
@Configuration
public class AllHandlersConfiguration {
    @Bean
    AddedNewUserHandler addedNewUserHandler(MsgSender msgSender) {
        return new AddedNewUserHandler(null, msgSender);
    }

    @Bean
    GameMapViewerHandler gameMapViewerHandler(MsgSender msgSender) {
        return new GameMapViewerHandler(msgSender, null);
    }

    @Bean
    HelloCallbackHandler helloCallbackHandler(MsgSender msgSender) {
        return new HelloCallbackHandler(msgSender);
    }

    @Bean
    HelloHandler helloHandler(MsgSender msgSender) {
        return new HelloHandler(msgSender);
    }

    @Bean
    RaidTimeHandler raidTimeHandler(MsgSender msgSender) {
        return new RaidTimeHandler(null, msgSender, null);
    }
}
