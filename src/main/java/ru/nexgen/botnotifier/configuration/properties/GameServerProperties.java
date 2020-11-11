package ru.nexgen.botnotifier.configuration.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.validation.annotation.Validated;

/**
 * Created by nikolay.mikutskiy
 * Date: 15.07.2020
 */
@Data
@Validated
@Configuration
@PropertySource("classpath:application.properties")
@ConfigurationProperties(prefix = "game-server")
public class GameServerProperties {
    private String host;
    private String port;
}
