package ru.nexgen.botnotifier.services;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

/**
 * Created by nikolay.mikutskiy
 * Date: 13.07.2020
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TemplatesService {
    private final Configuration configuration;

    public String fillTemplate(int templateId, Map<String, String> values) {
        try {
            Template template = configuration.getTemplate(getTemplateById(templateId));
            StringWriter stringWriter = new StringWriter();
            template.process(values, stringWriter);
            return stringWriter.toString();
        } catch (IOException | TemplateException e) {
            log.error(e.getLocalizedMessage());
            return "Error occurred! Please contact with developer";
        }
    }

    private String getTemplateById(int id) {
        switch (id) {
            case 1:
                return "captured_railway_station.ftl";
            case 2:
                return "killed_raid_boss.ftl";
            case 3:
                return "killed_raid_boss_for_clan.ftl";
            default:
                log.error("Can't find template by id:{}", id);
                return "default.ftl";
        }
    }
}
