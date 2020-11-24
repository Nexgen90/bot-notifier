package ru.nexgen.botnotifier.services.map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;

@TestPropertySource(properties = {
        "game.map.mapsFolderPath=./some/path",
})
@ExtendWith(SpringExtension.class)
class MapLoaderServiceTest {

    @Autowired
    private MapLoaderService service;

    @Test
    @DisplayName("Should Load Empty Map When File Path is Incorrect")
    void shouldLoadEmptyMapWhenFilePathIncorrect() {
        Map<Integer, Location> locations = service.loadAndGetGameMap();
        assertTrue(locations.isEmpty());
    }

    @Configuration
    static class Config {
        @Bean
        MapLoaderService mapLoaderService() {
            return new MapLoaderService();
        }
    }
}