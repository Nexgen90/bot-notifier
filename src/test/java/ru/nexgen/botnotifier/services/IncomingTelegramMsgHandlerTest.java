package ru.nexgen.botnotifier.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.nexgen.botnotifier.configuration.properties.GameLogicProperties;
import ru.nexgen.botnotifier.services.handlers.MessageHandler;
import ru.nexgen.botnotifier.services.handlers.RaidTimeHandler;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = IncomingTelegramMsgHandlerTest.Config.class)
class IncomingTelegramMsgHandlerTest {

    @Mock
    private Update incomingUpdate;
    @Mock
    private Message message;
    @MockBean
    private AuthService authService;
    @MockBean
    private MsgSender msgSender;
    @Captor
    private ArgumentCaptor<String> messageCaptor;
    @Captor
    private ArgumentCaptor<Long> chatIdCaptor;
    @MockBean
    TemplatesService templatesService;
    @MockBean
    GameLogicProperties properties;
    @Autowired
    private IncomingTelegramMsgHandler incomingTelegramMsgHandler;

    @Test
    @DisplayName("Should handle more than two raidTimes")
    void shouldHandleRaidTimeRequest() {
        when(authService.isValid(any(Update.class))).thenReturn(true);
        when(incomingUpdate.hasMessage()).thenReturn(true);
        when(incomingUpdate.getMessage()).thenReturn(message);
        when(message.hasText()).thenReturn(true);
        when(message.getText()).thenReturn("/raid_time мне пожалуйста");
        when(properties.getRaidTimes()).thenReturn(List.of("05:30:00", "08:30:00", "12:30:00"));

        incomingTelegramMsgHandler.handle(incomingUpdate);
        verify(msgSender, times(1)).send(messageCaptor.capture(), chatIdCaptor.capture());
//        assertThat(messageCaptor.getValue(), containsString("08:30:00"));
    }

    @Configuration
    @Import(value = {AllHandlersConfiguration.class})
    static class Config {

        @Bean
        RaidTimeHandler raidTimeHandler(MsgSender msgSender, TemplatesService templatesService, GameLogicProperties properties) {
            return new RaidTimeHandler(properties, msgSender, templatesService);
        }

        @Bean
        IncomingTelegramMsgHandler incomingTelegramMsgHandler(AuthService authService, List<MessageHandler> msgHandlers) {
            return new IncomingTelegramMsgHandler(authService, msgHandlers);
        }

    }
}