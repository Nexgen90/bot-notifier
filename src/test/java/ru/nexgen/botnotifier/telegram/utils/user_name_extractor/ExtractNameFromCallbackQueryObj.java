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
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import ru.nexgen.botnotifier.telegram.utils.UserNameExtractor;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = ExtractNameFromCallbackQueryObj.Config.class)
class ExtractNameFromCallbackQueryObj {
    private static final String NAME = "some name";
    private static final int SOME_USER_ID = 62346;

    @Mock
    private Update incomingUpdate;
    @Mock
    private CallbackQuery callbackQuery;
    @Mock
    private User user;

    @Autowired
    private UserNameExtractor userNameExtractor;

    @BeforeEach
    void setUp() {
        when(incomingUpdate.hasCallbackQuery()).thenReturn(true);
        when(incomingUpdate.getCallbackQuery()).thenReturn(callbackQuery);
        when(callbackQuery.getFrom()).thenReturn(user);
    }

    @Test
    @DisplayName("Should Extract <UserName> From User of CallbackQuery object")
    void shouldExtractUserNameFromCallbackQuery() {
        when(user.getUserName()).thenReturn(NAME);

        String name = userNameExtractor.extractUserName(incomingUpdate);
        assertThat(name, equalTo(NAME));
    }

    @Test
    @DisplayName("Should Extract <FirstName> From User of CallbackQuery object")
    void shouldExtractFirstNameFromCallbackQuery() {
        when(user.getFirstName()).thenReturn(NAME);

        String name = userNameExtractor.extractUserName(incomingUpdate);
        assertThat(name, equalTo(NAME));
    }

    @Test
    @DisplayName("Should Extract <LastName> From User of CallbackQuery object")
    void shouldExtractLastNameFromCallbackQuery() {
        when(user.getLastName()).thenReturn(NAME);

        String name = userNameExtractor.extractUserName(incomingUpdate);
        assertThat(name, equalTo(NAME));
    }

    @Test
    @DisplayName("Should Extract <UserId> From User of CallbackQuery object")
    void shouldExtractUserIdAsNameFromCallbackQuery() {
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