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
    private final Configuration templates;

    public String fillTemplate(String name, Map<String, String> values) {
        try {
            Template template = templates.getTemplate(name + ".ftl");
            StringWriter stringWriter = new StringWriter();
            template.process(values, stringWriter);
            return stringWriter.toString();
        } catch (IOException | TemplateException e) {
            log.error(e.getLocalizedMessage());
            return "Error occurred! Please contact with developer";
        }
    }

}
