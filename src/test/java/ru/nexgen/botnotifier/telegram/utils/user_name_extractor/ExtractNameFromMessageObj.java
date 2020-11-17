package ru.nexgen.botnotifier.telegram.utils.user_name_extractor;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import ru.nexgen.botnotifier.telegram.utils.UserNameExtractor;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = ExtractNameFromMessageObj.Config.class)
class ExtractNameFromMessageObj {
    private static final String NAME = "some name";
    private static final int SOME_USER_ID = 62346;

    @Mock
    private Update incomingUpdate;
    @Mock
    private Message message;
    @Mock
    private User user;

    @Autowired
    private UserNameExtractor userNameExtractor;

    @BeforeEach
    void setUp() {
        when(incomingUpdate.hasMessage()).thenReturn(true);
        when(incomingUpdate.getMessage()).thenReturn(message);
        when(message.getFrom()).thenReturn(user);
    }

    @Test
    @DisplayName("Should Extract <UserName> From User of Message object")
    void shouldExtractUserNameFromMessage() {
        when(user.getUserName()).thenReturn(NAME);

        String name = userNameExtractor.extractUserName(incomingUpdate);
        assertThat(name, equalTo(NAME));
    }

    @Test
    @DisplayName("Should Extract <FirstName> From User of Message object")
    void shouldExtractFirstNameFromMessage() {
        when(user.getFirstName()).thenReturn(NAME);

        String name = userNameExtractor.extractUserName(incomingUpdate);
        assertThat(name, equalTo(NAME));
    }

    @Test
    @DisplayName("Should Extract <LastName> From User of Message object")
    void shouldExtractLastNameFromMessage() {
        when(user.getLastName()).thenReturn(NAME);

        String name = userNameExtractor.extractUserName(incomingUpdate);
        assertThat(name, equalTo(NAME));
    }

    @Test
    @DisplayName("Should Extract <UserId> From User of Message object")
    void shouldExtractUserIdAsNameFromMessage() {
        when(user.getId()).thenReturn(SOME_USER_ID);

        String name = userNameExtractor.extractUserName(incomingUpdate);
        assertThat(name, equalTo(String.valueOf(SOME_USER_ID)));
    }

    @Configuration
    static class Config {

        @Bean
        UserNameExtractor userNameExtractor() {
            return new UserNameExtractor();
        }
    }
}