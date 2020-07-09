package ru.nexgen.botnotifier.configuration.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.validation.annotation.Validated;

/**
 * Created by nikolay.mikutskiy
 * Date: 09.07.2020
 */
@Data
@Validated
@Configuration
@PropertySource("classpath:application.properties")
@ConfigurationProperties(prefix = "telegram")
public class TelegramProperties {

    private String botToken;
    private String botUserName;
    private String chatId;

}
