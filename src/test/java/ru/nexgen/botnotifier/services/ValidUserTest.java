package ru.nexgen.botnotifier.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import ru.nexgen.botnotifier.configuration.properties.TelegramProperties;
import ru.nexgen.botnotifier.mapper.Chat;
import ru.nexgen.botnotifier.mapper.ChatsMapper;
import ru.nexgen.botnotifier.telegram.utils.UserNameExtractor;

import java.time.ZonedDateTime;

import static java.time.temporal.ChronoUnit.DAYS;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = ValidUserTest.Config.class)
class ValidUserTest {
    private static final long SOME_CHAT_ID = 1234L;
    private Chat chat;

    @Mock
    private Update incomingUpdate;
    @Mock
    private Message message;
    @Mock
    private User user;
    @MockBean
    private DbService dbService;
    @MockBean
    private ChatsMapper chatsMapper;
    @MockBean
    private UserNameExtractor userNameExtractor;

    @Autowired
    private AuthService authService;

    @BeforeEach
    void setUp() {
        chat = new Chat();
        chat.setChatId(SOME_CHAT_ID);
        when(dbService.getChatsMapper()).thenReturn(chatsMapper);
        when(chatsMapper.getChat(anyLong())).thenReturn(chat);
        when(incomingUpdate.getMessage()).thenReturn(message);
    }

    @Test
    @DisplayName("Check not in ban, when last ban time is expired")
    void shouldBeValidAfterExpiredBan() {
        when(message.getLeftChatMember()).thenReturn(user);
        when(user.getUserName()).thenReturn("some user name");
        chat.setBanTime(ZonedDateTime.now().minus(1, DAYS));
        chat.setActive(true);

        boolean isValidUser = authService.isValid(incomingUpdate);
        assertThat(isValidUser, is(true));
    }

    @Test
    @DisplayName("Check not in ban, when user does not have bans")
    void shouldBeValidWithoutBanAtAll() {
        when(message.getLeftChatMember()).thenReturn(user);
        when(user.getUserName()).thenReturn("some user name");
        chat.setBanTime(null);
        chat.setActive(true);

        boolean isValidUser = authService.isValid(incomingUpdate);
        assertThat(isValidUser, is(true));
    }

    @Test
    @DisplayName("Check banned user")
    void shouldBeInvalidAfterBan() {
        when(message.getLeftChatMember()).thenReturn(user);
        when(user.getUserName()).thenReturn("some user name");
        chat.setBanTime(ZonedDateTime.now().plus(1, DAYS));
        chat.setActive(true);

        boolean isValidUser = authService.isValid(incomingUpdate);
        assertThat(isValidUser, is(false));
    }

    @Test
    @DisplayName("Check inactive user")
    void shouldBeInactive() {
        when(message.getLeftChatMember()).thenReturn(user);
        when(user.getUserName()).thenReturn("some user name");
        chat.setActive(false);

        boolean isValidUser = authService.isValid(incomingUpdate);
        assertThat(isValidUser, is(false));
    }

    @Configuration
    static class Config {

        @Bean
        TelegramProperties telegramProperties() {
            TelegramProperties telegramProperties = new TelegramProperties();
            telegramProperties.setBotUserName("some bot name");
            return telegramProperties;
        }

        @Bean
        AuthService authService(TelegramProperties telegramProperties, DbService dbService, UserNameExtractor userNameExtractor) {
            return new AuthService(telegramProperties, dbService, userNameExtractor);
        }
    }
}