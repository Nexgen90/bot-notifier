package ru.nexgen.botnotifier.services.map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.nexgen.botnotifier.configuration.properties.GameLogicProperties;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class MapLoaderServiceTest {

    @MockBean
    private GameLogicProperties properties;

    @Autowired
    private MapLoaderService service;

    @Test
    @DisplayName("Should Load Empty Map When File Path is Incorrect")
    void shouldLoadEmptyMapWhenFilePathIncorrect() {
        when(properties.getMapsFolderPath()).thenReturn("./wrong/path");

        Map<Integer, Location> locations = service.loadAndGetGameMap();
        assertTrue(locations.isEmpty());
    }

    @Test
    @DisplayName("Should Load Map From Json-files")
    void shouldLoadMapFromJson() {
        Path resourceDirectory = Paths.get("src", "test", "resources", "map");
        String absolutePath = resourceDirectory.toFile().getAbsolutePath();
        when(properties.getMapsFolderPath()).thenReturn(absolutePath);

        Map<Integer, Location> integerLocationMap = service.loadAndGetGameMap();

        assertThat(integerLocationMap.values().size(), equalTo(6));
    }

    @Configuration
    static class Config {
        @Bean
        MapLoaderService mapLoaderService(GameLogicProperties properties) {
            return new MapLoaderService(properties);
        }
    }
}