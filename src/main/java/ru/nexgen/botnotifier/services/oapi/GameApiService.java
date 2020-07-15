package ru.nexgen.botnotifier.services.oapi;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.nexgen.botnotifier.configuration.properties.GameServerProperties;

/**
 * Created by nikolay.mikutskiy
 * Date: 14.07.2020
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class GameApiService {
    public static final String CHECK_PLAYER_URL = "/gamelogic/check/player/";
    private final RestTemplate restTemplate;
    private final GameServerProperties gameServerProperties;

    public boolean isGamer(int userId) {
        String url = "http://" + gameServerProperties.getHost() + ":" + gameServerProperties.getPort() + CHECK_PLAYER_URL + userId;
        try {
            ResponseEntity<String> plainResponse = restTemplate.getForEntity(url, String.class);
            log.info("Url: <{}>, Response: {}", url, plainResponse);
            return plainResponse.getStatusCode().is2xxSuccessful();
        } catch (Exception e) {
            return false;
        }
    }


}
