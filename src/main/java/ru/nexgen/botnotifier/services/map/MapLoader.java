package ru.nexgen.botnotifier.services.map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by nikolay.mikutskiy
 * Date: 31.08.2020
 */

@Slf4j
@Service
public class MapLoader {

    private String mapsFolderPath = "/Users/nikolay.mikutskiy/git/bot-notifier/src/main/resources/game/map";
    private final ObjectMapper objectMapper = new ObjectMapper();

    @PostConstruct
    public void init() throws IOException {
        File folder = new File(mapsFolderPath);
        List<File> files = listFilesForFolder(folder);
        Map<Integer, Location> map = new HashMap<>();

        files.forEach(f -> {
            try {
                map.putAll(objectMapper.readValue(f, new TypeReference<HashMap<Integer, Location>>() {}));
            } catch (IOException e) {
                log.error("Error while load map file: {}", f.getName(), e);
            }
        });

        log.info("Map is loaded: {} locations", map.size());
    }

    public List<File> listFilesForFolder(final File folder) {
        List<File> mapFiles = new ArrayList<>();
        for (final File fileEntry : folder.listFiles()) {
            if (!fileEntry.isDirectory()) {
                log.info("Found map file: {}", fileEntry.getName());
                mapFiles.add(fileEntry);
            }
        }
        return mapFiles;
    }
}
