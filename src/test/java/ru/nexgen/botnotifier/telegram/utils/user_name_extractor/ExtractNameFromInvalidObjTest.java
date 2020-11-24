package ru.nexgen.botnotifier.telegram.utils.user_name_extractor;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.nexgen.botnotifier.telegram.utils.UserNameExtractor;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = ExtractNameFromInvalidObjTest.Config.class)
class ExtractNameFromInvalidObjTest {
    private static final String DEFAULT_NAME = "UNDEFINED_USER";

    @Mock
    private Update incomingUpdate;

    @Autowired
    private UserNameExtractor userNameExtractor;

    @Test
    @DisplayName("Should Extract Name From <UpdateId> when can't find another way")
    void shouldExtractNameFromUpdateId() {
        String name = userNameExtractor.extractUserName(incomingUpdate);
        assertThat(name, equalTo(DEFAULT_NAME));
    }

    @Configuration
    static class Config {

        @Bean
        UserNameExtractor userNameExtractor() {
            return new UserNameExtractor();
        }
    }
}