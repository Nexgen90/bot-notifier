package ru.nexgen.botnotifier.services.map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = GameMapServiceTest.Config.class)
class GameMapServiceTest {
    private Map<Integer, Location> loadedMap = new HashMap<>();
    private Location location1 = new Location(1, MapType.CITY, "some code", "some nema", "asf", "gds", new ArrayList<>());

    @MockBean
    private MapLoaderService mapLoaderService;

    @Autowired
    private GameMapService gameMapService;

    @BeforeEach
    void setUp() throws Exception {
        loadedMap.put(1, location1);
        when(mapLoaderService.loadAndGetGameMap())
                .thenReturn(loadedMap);
        Method postConstruct = GameMapService.class.getDeclaredMethod("init", null);
        postConstruct.setAccessible(true);
        postConstruct.invoke(gameMapService);
    }

    @Test
    @DisplayName("Should Get Exist Location By Id")
    void shouldGetExistLocationById() {
        Location location = gameMapService.getLocationById(location1.getId());
        assertThat(location, equalTo(location1));
    }

    @Test
    @DisplayName("Should Return NULL If Location Does Not Exist")
    void shouldNotGetLocationById() {
        Location location = gameMapService.getLocationById(999);
        assertThat(location, equalTo(null));
    }

    @Configuration
    static class Config {

        @Bean
        GameMapService gameMapService(MapLoaderService mapLoaderService) {
            return new GameMapService(mapLoaderService);
        }
    }
}