package ru.nexgen.botnotifier.configuration.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.validation.annotation.Validated;

import java.util.List;

/**
 * Created by nikolay.mikutskiy
 * Date: 15.07.2020
 */
@Data
@Validated
@Configuration
@PropertySource("classpath:application.properties")
@ConfigurationProperties(prefix = "game-logic")
public class GameLogicProperties {
    private List<String> raidTimes;
    private String mapsFolderPath;
}
